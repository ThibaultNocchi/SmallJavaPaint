package drawingeditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Point d'entrée au programme.
 */
public class DrawingEditor extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("view/principale.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Super application v2");
        primaryStage.show();
        primaryStage.centerOnScreen();
        
    }

    public static void main(String[] args){
        launch(args);
    }

}
