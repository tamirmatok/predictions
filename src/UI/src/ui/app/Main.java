package ui.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {
    private BorderPane root;


    public static void main(String[] args) {
        launch(args);
//        userInterface ui = new UserInterfaceImpl();
//        ui.startInterface();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/ui/app/app.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        // Create a scene
        Scene scene = new Scene(root);

        // Set the scene on the primary stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Predictions System");

        // Show the stage
        primaryStage.show();
    }


}
