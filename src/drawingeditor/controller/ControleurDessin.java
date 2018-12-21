package drawingeditor.controller;

import drawingeditor.model.*;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControleurDessin implements Initializable {

    @FXML
    public ToggleButton rect;
    @FXML
    public ToggleButton ell;
    @FXML
    public ToggleButton del;
    @FXML
    public ToggleButton move;
    @FXML
    public ColorPicker colorpicker;
    @FXML
    public Label x;
    @FXML
    public Label y;
    @FXML
    public Spinner<Double> width;
    @FXML
    public Spinner<Double> height;
    @FXML
    public Pane pane;

    private Dessin dessin;

    public ControleurDessin(){};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.rect.setSelected(true);
        this.colorpicker.setValue(javafx.scene.paint.Color.RED);
        this.x.setVisible(false);
        this.y.setVisible(false);
        this.pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        this.dessin = new DessinImpl();

        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(rect.isSelected()) dessin.ajouterForme(new Rect(event.getX(),event.getY(),width.getValueFactory().getValue(),height.getValueFactory().getValue(),colorpicker.getValue()));
                else if(ell.isSelected()) dessin.ajouterForme(new Ell(event.getX(),event.getY(),width.getValueFactory().getValue(),height.getValueFactory().getValue(),colorpicker.getValue()));
            }
        });

        this.dessin.getFormes().addListener(new ListChangeListener<Forme>() {
            @Override
            public void onChanged(Change<? extends Forme> event) {
                if(event.next()){
                    if(event.wasAdded()){
                        // TODO
                        List<? extends Forme> liste = event.getAddedSubList();
                    }else if(event.wasRemoved()){
                        // TODO
                        List<? extends Forme> liste = event.getRemoved();
                    }
                }
            }
        });

    }
}
