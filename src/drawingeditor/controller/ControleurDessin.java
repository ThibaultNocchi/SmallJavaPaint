package drawingeditor.controller;

import drawingeditor.model.Dessin;
import drawingeditor.model.DessinImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
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
    public Spinner width;
    @FXML
    public Spinner height;
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
    }
}
