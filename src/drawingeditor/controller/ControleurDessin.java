package drawingeditor.controller;

import drawingeditor.model.*;
import javafx.beans.binding.*;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControleurDessin implements Initializable {

    @FXML public ToggleButton rect;
    @FXML public ToggleButton ell;
    @FXML public ToggleButton del;
    @FXML public ToggleButton move;
    @FXML public ToggleButton resize;
    @FXML public ToggleButton color;
    @FXML public ToggleButton draw;

    @FXML public Button clear;
    @FXML public Button back;
    @FXML public Button forward;
    @FXML public Button setBg;

    @FXML public ColorPicker colorpicker;

    @FXML public Label x;
    @FXML public Label y;
    @FXML public Spinner<Double> width;
    @FXML public Spinner<Double> height;

    @FXML public TextField filename;
    @FXML public Button save;
    @FXML public Button load;

    @FXML public Pane pane;
    @FXML public BorderPane borderPane;
    @FXML public ScrollPane scrollPane;

    private Dessin dessin;
    private EventList eventList;
    private int historyDrawing;
    private double drawX, drawY;

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
                else if(color.isSelected()){
                    Paint before = forme.getCouleur();
                    forme.setCouleur(colorpicker.getValue());
                    eventList.add(new EventFormeColor(forme, before, forme.getCouleur()));
                }
            }
        });
        new DnDToMoveShape(shape);

        return shape;
        
    }

    private void addRectAtMouse(MouseEvent event){
        Rect rect = new Rect(event.getX(),event.getY(),this.width.getValueFactory().getValue(),this.height.getValueFactory().getValue(),this.colorpicker.getValue());
        this.dessin.ajouterForme(rect);
        this.eventList.add(new EventFormeAdd(rect));
    }

    private void addEllAtMouse(MouseEvent event){
        this.addEllAtCoords(event.getX(), event.getY());
    }

    private void addEllAtCoords(double x, double y){
        Ell ell = new Ell(x,y,this.width.getValueFactory().getValue(),this.height.getValueFactory().getValue(),this.colorpicker.getValue());
        this.dessin.ajouterForme(ell);
        EventFormeAdd ev = new EventFormeAdd(ell);
        if(this.draw.isSelected()) ev.setHistory(this.historyDrawing);
        this.eventList.add(ev);
        this.updateSizePane(ell);
    }

    private void removeForme(Forme forme){
        this.dessin.supprimerForme(forme);
        this.eventList.add(new EventFormeDelete(forme));
    }

    private void rollBack(){
        this.eventList.rollback(this.dessin);
    }

    private void rollFoarward(){
        this.eventList.rollforward(this.dessin);
    }

    private void updateSizePane(Forme forme){

        double xMax, yMax;
        double xPane = this.pane.getPrefWidth();
        double yPane = this.pane.getPrefHeight();
        double newX = forme.getPositionX();
        double newY = forme.getPositionY();
        double width = forme.getWidth();
        double height = forme.getHeight();

        double tx = 0;
        double ty = 0;
        double finalXPane;
        double depassement;

        depassement = forme.isOutRight(xPane);
        if(depassement != 0) this.pane.setPrefWidth(xPane+depassement);

        depassement = forme.isOutBottom(yPane);
        if(depassement != 0) this.pane.setPrefHeight(yPane+depassement);

        depassement = forme.isOutLeft();
        if(depassement != 0){
            tx = depassement;
            this.pane.setPrefWidth(xPane + tx);
        }

        /*if(newY - height/2 < 0){
            ty = -(newY - height/2);
            this.pane.setPrefHeight(yPane + ty);
        }*/
        depassement = forme.isOutTop();
        if(depassement != 0){
            ty = depassement;
            this.pane.setPrefHeight(yPane + ty);
        }

        if(tx != 0 || ty != 0) {
            this.dessin.deplacerToutesFormes(tx, ty);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.rect.setSelected(true);    // Sélectionne par défaut l'outil pour dessiner des rectangles.
        this.colorpicker.setValue(javafx.scene.paint.Color.RED);    // Sélectionne par défaut la couleur rouge.
        this.x.setVisible(false);   // Cache le label des coordonnées X et Y pour quand on bouge une forme.
        this.y.setVisible(false);
//        this.pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));   // Colorie le fond en blanc.
        this.dessin = new DessinImpl();     // Initialise l'implémentation de notre "tableau".
        this.eventList = new EventList();   // Historique des modifications.
        this.pane.backgroundProperty().bind(Bindings.createObjectBinding(()->{
            BackgroundFill bf = new BackgroundFill(this.dessin.getBg(), null, null);
            return new Background(bf);
        }, this.dessin.bgProperty()));

        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {     // Event qui gère lorsque l'on clique quelque part dans le pane.
            @Override
            public void handle(MouseEvent event) {
                // Si le bouton rectangle est activé, on ajoute une forme rectangulaire au dessin aux coordonnées de la souris et avec les paramètres de taille et de couleur spécifiés dans les contrôles.
                if(rect.isSelected()) addRectAtMouse(event);
                // De même l'on dessine une ellipse si le bouton ellipse est activé.
                else if(ell.isSelected()) addEllAtMouse(event);
            }
        });

        pane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(draw.isSelected()){
                    historyDrawing = eventList.getLastHistory()+1;
                    drawX = event.getX();
                    drawY = event.getY();
                }
            }
        });

        pane.setOnMouseDragged(new EventHandler<MouseEvent>() {     // Event gérant un outil "crayon".
            @Override
            public void handle(MouseEvent event) {
                if(draw.isSelected()){
                    double currentX = drawX;
                    double currentY = drawY;
                    double finalX = event.getX();
                    double finalY = event.getY();
                    drawX = finalX;
                    drawY = finalY;
                    double stepX = width.getValue() / 3;
                    double stepY = height.getValue() / 3;
                    while(currentX != finalX && currentY != finalY){
                        addEllAtCoords(currentX, currentY);
                        if(currentX <= finalX - stepX) currentX += stepX;
                        else if(currentX >= finalX + stepX) currentX -= stepX;
                        else currentX = finalX;
                        if(currentY <= finalY - stepY) currentY += stepY;
                        else if(currentY >= finalY + stepY) currentY -= stepY;
                        else currentY = finalY;
                    }
                    addEllAtCoords(currentX, currentY);

                }
            }
        });

        clear.setOnAction(new EventHandler<ActionEvent>() {     // Si l'on clique sur le bouton de clear, on vide le dessin et l'historique.
            @Override
            public void handle(ActionEvent event) {
                dessin.viderDessin();
                eventList.clear();
                pane.setPrefSize(1200,720);
            }
        });

        setBg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dessin.setBg(colorpicker.getValue());
            }
        });

        back.setOnAction(new EventHandler<ActionEvent>() {  // Demande un rollback au gestionnaire d'historique.
            @Override
            public void handle(ActionEvent event) {
                rollBack();
            }
        });

        forward.setOnAction(new EventHandler<ActionEvent>() {   // Demande un retour en avant au gestionnaire d'historique.
            @Override
            public void handle(ActionEvent event) {
                rollFoarward();
            }
        });

        final KeyCombination keyCombinationZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        final KeyCombination keyCombinationY = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
        borderPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(keyCombinationZ.match(event)){
                    rollBack();
                }else if(keyCombinationY.match(event)){
                    rollFoarward();
                }
            }
        });

        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Paint paint = pane.getBackground().getFills().get(0).getFill();
                    System.out.println("Dessin sauvegardé dans "+dessin.save(filename.getText(), pane.getPrefWidth(), pane.getPrefHeight()));
                } catch (FileNotFoundException e) {
                    System.out.println("Problème à l'écriture du fichier.");
                    e.printStackTrace();
                }
            }
        });

        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    double[] paneSizes = dessin.load(filename.getText());
                    if(paneSizes[0] != 0 && paneSizes[1] != 0) pane.setPrefSize(paneSizes[0], paneSizes[1]);
                } catch (IOException e) {
                    System.out.println("Problème à la lecture du fichier.");
                    e.printStackTrace();
                }
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

                if(move.isSelected() || resize.isSelected()){
                    this.txTotal = 0;
                    this.tyTotal = 0;
                    this.pressPositionX = evt.getSceneX();
                    this.pressPositionY = evt.getSceneY();
                }

                if(move.isSelected()) {
                    x.setVisible(true);
                    y.setVisible(true);
                    final StringExpression xBinding = Bindings.convert(((FormeImpl) view.getUserData()).positionXProperty());
                    final StringExpression yBinding = Bindings.convert(((FormeImpl) view.getUserData()).positionYProperty());
                    x.textProperty().bind(Bindings.createStringBinding(() -> "x: " + ((FormeImpl) view.getUserData()).positionXProperty().get(), ((FormeImpl) view.getUserData()).positionXProperty()));
                    y.textProperty().bind(Bindings.createStringBinding(() -> "y: " + ((FormeImpl) view.getUserData()).positionYProperty().get(), ((FormeImpl) view.getUserData()).positionYProperty()));
                }

            });

            view.setOnMouseDragged(evt -> {

                double tx = 0, ty = 0;

                if(move.isSelected() || resize.isSelected()){
                    tx = evt.getSceneX() - this.pressPositionX;
                    ty = evt.getSceneY() - this.pressPositionY;
                    this.txTotal += tx;
                    this.tyTotal += ty;
                    this.pressPositionX = evt.getSceneX();
                    this.pressPositionY = evt.getSceneY();
                }

                if(move.isSelected()){
                    ((FormeImpl) view.getUserData()).deplacer(tx,ty);
                }else if(resize.isSelected()){
                    ((FormeImpl) view.getUserData()).redimensionner(tx, ty);
                }

                if(move.isSelected() || resize.isSelected()){
                    updateSizePane((Forme)view.getUserData());
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
                    eventList.add(new EventFormeMove(this.txTotal, this.tyTotal, (FormeImpl) view.getUserData()));
                }else if(resize.isSelected()){
                    eventList.add(new EventFormeResize((Forme) view.getUserData(), this.txTotal, this.tyTotal));
                }
            });

        }

    }

}
