package drawingeditor.controller;

import drawingeditor.model.*;
import javafx.beans.binding.*;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControleurDessin implements Initializable {

    @FXML public ToggleButton rect;
    @FXML public ToggleButton ell;
    @FXML public ToggleButton del;
    @FXML public ToggleButton move;
    @FXML public ColorPicker colorpicker;
    @FXML public Label x;
    @FXML public Label y;
    @FXML public Spinner<Double> width;
    @FXML public Spinner<Double> height;
    @FXML public Pane pane;
    @FXML public Button clear;

    private Dessin dessin;

    public ControleurDessin(){};

    private Shape createViewShapeFromShape(final Forme forme){

        Shape shape = null;

        if(forme instanceof Ell){
            Ellipse ell = new Ellipse();
            ell.centerXProperty().bind(forme.positionXProperty());
            ell.centerYProperty().bind(forme.positionYProperty());
            ell.fillProperty().bind(forme.couleurProperty());
            final NumberBinding widthBinding = Bindings.divide(forme.widthProperty(),2);
            final NumberBinding heightBinding = Bindings.divide(forme.heightProperty(), 2);
            ell.radiusXProperty().bind(widthBinding);
            ell.radiusYProperty().bind(heightBinding);
            shape = ell;
        }else if(forme instanceof Rect){
            Rectangle rect = new Rectangle();
            rect.xProperty().bind(forme.positionXProperty());
            rect.yProperty().bind(forme.positionYProperty());
            rect.widthProperty().bind(forme.widthProperty());
            rect.heightProperty().bind(forme.heightProperty());
            rect.fillProperty().bind(forme.couleurProperty());
            shape = rect;
        }

        shape.setUserData(forme);
        shape.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(del.isSelected()) dessin.supprimerForme(forme);
            }
        });
        new DnDToMoveShape(shape);

        return shape;
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.rect.setSelected(true);    // Sélectionne par défaut l'outil pour dessiner des rectangles.
        this.colorpicker.setValue(javafx.scene.paint.Color.RED);    // Sélectionne par défaut la couleur rouge.
        this.x.setVisible(false);   // Cache le label des coordonnées X et Y pour quand on bouge une forme.
        this.y.setVisible(false);
        this.pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));   // Colorie le fond en blanc.
        this.dessin = new DessinImpl();     // Initialise l'implémentation de notre "tableau".

        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(rect.isSelected()) dessin.ajouterForme(new Rect(event.getX(),event.getY(),width.getValueFactory().getValue(),height.getValueFactory().getValue(),colorpicker.getValue()));
                else if(ell.isSelected()) dessin.ajouterForme(new Ell(event.getX(),event.getY(),width.getValueFactory().getValue(),height.getValueFactory().getValue(),colorpicker.getValue()));
            }
        });

        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dessin.viderDessin();
            }
        });

        this.dessin.getFormes().addListener(new ListChangeListener<Forme>() {
            @Override
            public void onChanged(Change<? extends Forme> event) {
                if(event.next()){
                    if(event.wasAdded()){
                        // TODO
                        List<? extends Forme> liste = event.getAddedSubList();
                        for(Forme forme : liste){
                            Shape shape = createViewShapeFromShape(forme);
                            pane.getChildren().add(shape);
                        }
                    }else if(event.wasRemoved()){
                        // TODO
                        List<? extends Forme> liste = event.getRemoved();
                        for(Forme forme : liste){
                            Node toDelete = null;
                            for(Node affiche : pane.getChildren()){
                                if(affiche.getUserData() == forme) toDelete = affiche;
                            }
                            if(toDelete != null) pane.getChildren().remove(toDelete);
                        }
                    }
                }
            }
        });

    }

    private class DnDToMoveShape{

        private double pressPositionX;
        private double pressPositionY;

        public DnDToMoveShape(Shape view){

            view.setOnMousePressed(evt -> {
                if(move.isSelected()) {
                    this.pressPositionX = evt.getSceneX();
                    this.pressPositionY = evt.getSceneY();
                    x.setVisible(true);
                    y.setVisible(true);
                    final StringExpression xBinding = Bindings.convert(((FormeImpl) view.getUserData()).positionXProperty());
                    final StringExpression yBinding = Bindings.convert(((FormeImpl) view.getUserData()).positionYProperty());
                    x.textProperty().bind(Bindings.createStringBinding(() -> "x: " + ((FormeImpl) view.getUserData()).positionXProperty().get(), ((FormeImpl) view.getUserData()).positionXProperty()));
                    y.textProperty().bind(Bindings.createStringBinding(() -> "y: " + ((FormeImpl) view.getUserData()).positionYProperty().get(), ((FormeImpl) view.getUserData()).positionYProperty()));
                }
            });

            view.setOnMouseDragged(evt -> {
                if(move.isSelected()){
                    final double tx = evt.getSceneX() - this.pressPositionX;
                    final double ty = evt.getSceneY() - this.pressPositionY;
                    ((FormeImpl) view.getUserData()).deplacer(tx,ty);
                    this.pressPositionX = evt.getSceneX();
                    this.pressPositionY = evt.getSceneY();
                }
            });

            view.setOnMouseReleased(evt -> {
                x.setVisible(false);
                y.setVisible(false);
                x.textProperty().unbind();
                y.textProperty().unbind();
                x.setText("");
                y.setText("");
            });

        }

    }

}
