package drawingeditor.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class ControleurDessin implements Initializable {

    public ToggleButton rect;
    public ToggleButton ell;
    public ToggleButton del;
    public ToggleButton move;
    public ColorPicker colorpicker;
    public Label x;
    public Label y;
    public Spinner width;
    public Spinner height;
    public Pane pane;

    public ControleurDessin(){};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}
