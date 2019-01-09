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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
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
import java.util.ArrayList;
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
    @FXML public ToggleButton draw;
    @FXML public Button back;

    private Dessin dessin;
    private EventList eventList;
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
                if(del.isSelected()) removeForme(forme);
            }
        });
        new DnDToMoveShape(shape);

        return shape;
        
    }

    private void addRectAtMouse(MouseEvent event){
        Rect rect = new Rect(event.getX(),event.getY(),this.width.getValueFactory().getValue(),this.height.getValueFactory().getValue(),this.colorpicker.getValue());
        this.dessin.ajouterForme(rect);
        this.eventList.add(new AddEvent(rect));
    }

    private void addEllAtMouse(MouseEvent event){
        Ell ell = new Ell(event.getX(),event.getY(),this.width.getValueFactory().getValue(),this.height.getValueFactory().getValue(),this.colorpicker.getValue());
        this.dessin.ajouterForme(ell);
        this.eventList.add(new AddEvent(ell));
    }

    private void removeForme(Forme forme){
        this.dessin.supprimerForme(forme);
        this.eventList.add(new DeleteEvent(forme));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.rect.setSelected(true);    // Sélectionne par défaut l'outil pour dessiner des rectangles.
        this.colorpicker.setValue(javafx.scene.paint.Color.RED);    // Sélectionne par défaut la couleur rouge.
        this.x.setVisible(false);   // Cache le label des coordonnées X et Y pour quand on bouge une forme.
        this.y.setVisible(false);
        this.pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));   // Colorie le fond en blanc.
        this.dessin = new DessinImpl();     // Initialise l'implémentation de notre "tableau".
        this.eventList = new EventList();   // Historique des modifications.

        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {     // Event qui gère lorsque l'on clique quelque part dans le pane.
            @Override
            public void handle(MouseEvent event) {
                // Si le bouton rectangle est activé, on ajoute une forme rectangulaire au dessin aux coordonnées de la souris et avec les paramètres de taille et de couleur spécifiés dans les contrôles.
                if(rect.isSelected()) addRectAtMouse(event);
                // De même l'on dessine une ellipse si le bouton ellipse est activé.
                else if(ell.isSelected()) addEllAtMouse(event);
            }
        });

        pane.setOnMouseDragged(new EventHandler<MouseEvent>() {     // Event gérant un outil "crayon".
            @Override
            public void handle(MouseEvent event) {
                if(draw.isSelected()) addEllAtMouse(event);
            }
        });

        clear.setOnAction(new EventHandler<ActionEvent>() {     // Si l'on clique sur le bouton de clear, on vide le dessin et l'historique.
            @Override
            public void handle(ActionEvent event) {
                dessin.viderDessin();
                eventList.clear();
            }
        });

        back.setOnAction(new EventHandler<ActionEvent>() {  // Demande un rollback au gestionnaire d'historique.
            @Override
            public void handle(ActionEvent event) {
                eventList.rollback(dessin);
            }
        });

        this.dessin.getFormes().addListener(new ListChangeListener<Forme>() {   // Listener surveillant le modèle.
            @Override
            public void onChanged(Change<? extends Forme> event) {  // Quand le modèle change
                if(event.next()){
                    if(event.wasAdded()){   // Si il y a eu des ajouts
                        List<? extends Forme> liste = event.getAddedSubList();  // On récupère la liste des formes ajoutées
                        for(Forme forme : liste){   // Pour chaque forme on lui créé une vue et on l'ajoute au pane JavaFX.
                            Shape shape = createViewShapeFromShape(forme);
                            pane.getChildren().add(shape);
                        }
                    }else if(event.wasRemoved()){   // Si il y a eu des suppressions
                        List<? extends Forme> liste = event.getRemoved();
                        for(Forme forme : liste){
                            Node toDelete = null;
                            for(Node affiche : pane.getChildren()){     // On parcourt toutes les formes affichées
                                if(affiche.getUserData() == forme) toDelete = affiche;  // Si la forme affichée correspond à la forme du modèle que l'on veut supprimer, on garde la vue de côté.
                            }
                            if(toDelete != null) pane.getChildren().remove(toDelete);   // Si on a trouvé la vue correspondant à la forme du modèle à supprimer, on la supprime.
                        }
                    }
                }
            }
        });

    }

    private class DnDToMoveShape{

        private double pressPositionX;
        private double pressPositionY;

        private double txTotal;
        private double tyTotal;

        public DnDToMoveShape(Shape view){

            view.setOnMousePressed(evt -> {
                if(move.isSelected()) {
                    this.txTotal = 0;
                    this.tyTotal = 0;
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
                    this.txTotal += tx;
                    this.tyTotal += ty;
                    ((FormeImpl) view.getUserData()).deplacer(tx,ty);
                    this.pressPositionX = evt.getSceneX();
                    this.pressPositionY = evt.getSceneY();
                }
            });

            view.setOnMouseReleased(evt -> {
                if(move.isSelected()) {
                    x.setVisible(false);
                    y.setVisible(false);
                    x.textProperty().unbind();
                    y.textProperty().unbind();
                    x.setText("");
                    y.setText("");
                    eventList.add(new MoveEvent(this.txTotal, this.tyTotal, (FormeImpl) view.getUserData()));
                }
            });

        }

    }

}
