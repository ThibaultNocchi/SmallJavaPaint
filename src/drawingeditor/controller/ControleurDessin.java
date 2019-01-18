package drawingeditor.controller;

import drawingeditor.model.*;
import javafx.beans.binding.*;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Classe contrôleur de la fenêtre.
 */
@SuppressWarnings("unused")
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

    /**
     * Dessin contenant les formes.
     */
    private Dessin dessin;

    /**
     * Historique d'événements.
     */
    private EventList eventList;

    /**
     * Utilisée lors de dessin à main levé pour sauvegarde la valeur dans l'historique de la première forme et l'appliquer à toutes les suivantes du dessin.
     */
    private int historyDrawing;

    /**
     * Utilisées pour sauvegarder la position de départ d'un dessin au crayon, à main levée.
     */
    private double drawX, drawY;

    /**
     * Créé une vue pour une forme et associe tous les bindings nécessaires et les événements JavaFX.
     * @param forme La forme à gérer.
     * @return La vue JavaFX de la forme.
     */
    private Shape createViewShapeFromShape(final Forme forme){

        Shape shape = forme.createViewShape();

        shape.setUserData(forme);
        shape.setOnMouseClicked(event -> {
            if(del.isSelected()) removeForme(forme);
            else if(color.isSelected()){
                Paint before = forme.getCouleur();
                forme.setCouleur(colorpicker.getValue());
                EventFormeColor newColor = new EventFormeColor(forme, before, forme.getCouleur());
                eventList.add(newColor);
                eventList.changeColorOfAllFormeWithSameHistory(newColor);
            }
        });
        new DnDToMoveShape(shape);  // Assciation d'un objet surveillant les déplacements de la vue.

        return shape;
        
    }

    /**
     * Ajoute un rectangle à la position de la souris.
     * @param event Événement fournissant la position de la souris.
     */
    private void addRectAtMouse(MouseEvent event){
        Rect rect = new Rect(event.getX(),event.getY(),this.width.getValueFactory().getValue(),this.height.getValueFactory().getValue(),this.colorpicker.getValue());
        this.dessin.ajouterForme(rect);
        this.eventList.add(new EventFormeAdd(rect));
    }

    /**
     * Ajoute une ellipse à la position de la souris.
     * @param event Événement fournissant la position de la souris.
     */
    private void addEllAtMouse(MouseEvent event){
        this.addEllAtCoords(event.getX(), event.getY());
    }

    /**
     * Ajoute une ellipse aux coordonnées spécifiées.
     * @param x Position X.
     * @param y Position Y.
     */
    private void addEllAtCoords(double x, double y){
        Ell ell = new Ell(x,y,this.width.getValueFactory().getValue(),this.height.getValueFactory().getValue(),this.colorpicker.getValue());
        this.dessin.ajouterForme(ell);
        EventFormeAdd ev = new EventFormeAdd(ell);  // Ajout d'un événement dans l'historique.
        // Si l'outil de dessin est sélectionné, il faut que chaque ellipse ait le même numéro d'historique pour les traiter comme un seul trait.
        // On utilise donc la valeur mise dans this.historyDrawing au début du dessin par la méthode gérant le drag de la souris.
        if(this.draw.isSelected()) ev.setHistory(this.historyDrawing);
        this.eventList.add(ev);
        this.updateSizePane(ell);   // On resize le pane pour s'adapter à la nouvelle forme.
    }

    /**
     * Supprime une forme du dessin et ajoute un événement correspondant.
     * @param forme Forme à supprimer.
     */
    private void removeForme(Forme forme){
        this.dessin.supprimerForme(forme);
        this.eventList.add(new EventFormeDelete(forme));
    }

    /**
     * Retourne en arrière dans le dessin.
     */
    private void rollBack(){
        this.eventList.rollback(this.dessin);
    }

    /**
     * Retourne en avant (lol) dans le dessin.
     */
    private void rollFoarward(){
        this.eventList.rollforward(this.dessin);
    }

    /**
     * Met à jour la taille du pane par rapport à la forme en paramètre.
     * @param forme Forme dont il faut vérifier qu'elle ne dépasse pas.
     */
    private void updateSizePane(Forme forme){

        double xPane = this.pane.getPrefWidth();
        double yPane = this.pane.getPrefHeight();

        double tx = 0;
        double ty = 0;
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

        // Event qui gère lorsque l'on clique quelque part dans le pane.
        pane.setOnMouseClicked(event -> {
            // Si le bouton rectangle est activé, on ajoute une forme rectangulaire au dessin aux coordonnées de la souris et avec les paramètres de taille et de couleur spécifiés dans les contrôles.
            if(rect.isSelected()) addRectAtMouse(event);
            // De même l'on dessine une ellipse si le bouton ellipse est activé.
            else if(ell.isSelected()) addEllAtMouse(event);
        });

        // Event gérant le premier clic lors d'un dessin "crayon".
        pane.setOnMousePressed(event -> {
            if(draw.isSelected()){
                historyDrawing = eventList.getLastHistory()+1;  // On prépare la valeur d'historique pour toutes les formes qui vont être ajoutées.
                drawX = event.getX();   // Initialisation de la première position de la souris.
                drawY = event.getY();
            }
        });

        // Event gérant un outil "crayon".
// Cet événement ne dessine pas seulement une forme à l'endroit de la souris, car sinon des trop gros trous apparaissent si l'on bouge la souris trop vite.
// Du coup à chaque appel de l'événement, on dessine une sorte de trait entre la position précédente et actuelle, puis on sauvegarde la position actuelle pour le prochain coup.
// C'est l'utilité de drawX et drawY.
        pane.setOnMouseDragged(event -> {
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
        });

        // Si l'on clique sur le bouton de clear, on vide le dessin et l'historique.
        clear.setOnAction(event -> {
            dessin.viderDessin();
            eventList.clear();
            pane.setPrefSize(1200,720);
        });

        // Change la couleur de fond.
        setBg.setOnAction(event -> dessin.setBg(colorpicker.getValue()));

        // Demande un rollback au gestionnaire d'historique.
        back.setOnAction(event -> rollBack());

        // Demande un retour en avant au gestionnaire d'historique.
        forward.setOnAction(event -> rollFoarward());

        final KeyCombination keyCombinationZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);  // Ctrl + Z
        final KeyCombination keyCombinationY = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);  // Ctrl + Y
        // Gère l'appel des fonctions correspondant au ctrl z et ctrl y.
        borderPane.setOnKeyPressed(event -> {
            if(keyCombinationZ.match(event)){
                rollBack();
            }else if(keyCombinationY.match(event)){
                rollFoarward();
            }
        });

        // Gère la sauvegarde du dessin.
        save.setOnAction(event -> {
            try {
                System.out.println("Dessin sauvegardé dans "+dessin.save(filename.getText(), pane.getPrefWidth(), pane.getPrefHeight()));
            } catch (FileNotFoundException e) {
                System.out.println("Problème à l'écriture du fichier.");
                e.printStackTrace();
            }
        });

        // Gère le chargement d'un dessin.
        load.setOnAction(event -> {
            try {
                double[] paneSizes = dessin.load(filename.getText());
                if(paneSizes[0] != 0 && paneSizes[1] != 0) pane.setPrefSize(paneSizes[0], paneSizes[1]);    // Si le fichier spécifiait une pane size, on l'applique.
            } catch (IOException e) {
                System.out.println("Problème à la lecture du fichier.");
                e.printStackTrace();
            }
        });

        // Listener surveillant le modèle.
        this.dessin.getFormes().addListener((ListChangeListener<Forme>) event -> {  // Quand le modèle change
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
        });

    }

    /**
     * Classe s'associant à une vue d'une forme pour surveiller les clics dessus.
     */
    private class DnDToMoveShape{

        private double pressPositionX;
        private double pressPositionY;

        private double txTotal;
        private double tyTotal;

        /**
         * Initialise tous les événements liés à la vue de la forme.
         * @param view Vue à lier.
         */
        DnDToMoveShape(Shape view){

            view.setOnMousePressed(evt -> {     // Lorsque l'on clique sur une forme.

                if(move.isSelected() || resize.isSelected()){   // Si on a l'outil move ou resize d'activé, on sauvegarde la position du premier clic et on initialise un compteur de déplacement total (relatif) de la souris.
                    this.txTotal = 0;
                    this.tyTotal = 0;
                    this.pressPositionX = evt.getSceneX();
                    this.pressPositionY = evt.getSceneY();
                }

                if(move.isSelected()) {     // En plus, si move est choisi, on affiche les labels montrant la position de la forme en cours de déplacement, et on lie les propriétés.
                    x.setVisible(true);
                    y.setVisible(true);
                    final StringExpression xBinding = Bindings.convert(((FormeImpl) view.getUserData()).positionXProperty());
                    final StringExpression yBinding = Bindings.convert(((FormeImpl) view.getUserData()).positionYProperty());
                    x.textProperty().bind(Bindings.createStringBinding(() -> "x: " + ((FormeImpl) view.getUserData()).positionXProperty().get(), ((FormeImpl) view.getUserData()).positionXProperty()));
                    y.textProperty().bind(Bindings.createStringBinding(() -> "y: " + ((FormeImpl) view.getUserData()).positionYProperty().get(), ((FormeImpl) view.getUserData()).positionYProperty()));
                }

            });

            view.setOnMouseDragged(evt -> {     // Lorsque la souris est cliquée et bouge, on ajoute le déplacement relatif au compteur total et on bouge ou redimensionne la forme.

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

            view.setOnMouseReleased(evt -> {    // On unbind les labels si move était choisi, et dans tous les cas on ajoute un nouvel événement à l'historique grâce à nos déplacements relatifs totaux.
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
