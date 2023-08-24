package ui.app.subComponents.header.controller;

import dto.impl.MessageDTO;
import engine.impl.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.app.controller.AppController;

import java.io.File;

public class HeaderController {
    @FXML
    private Label filePathLabel;

    @FXML private Label progressPercentLabel;
    private AppController mainController;

    @FXML private ProgressBar fileProgressBar;

    private SimpleBooleanProperty isFileLoaded;



    public HeaderController(){
        this.isFileLoaded = new SimpleBooleanProperty(false);

    }


    public void handleLoadNewFileBtn(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );
        fileChooser.setTitle("Open XML File");

        Stage stage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            String selectedFilePath = selectedFile.getAbsolutePath();
            filePathLabel.setText(selectedFilePath);

            //TODO: add try catch and open use threads;
            this.fileProgressBar.setProgress(0);
            Engine engine = this.mainController.getEngine();
            MessageDTO messageDTO = engine.loadSystemWorldFromXmlFile(selectedFilePath);
            boolean isSuccess = messageDTO.isSuccess();
            this.mainController.setSystemLoaded(isSuccess);
            if (isSuccess){
                this.fileProgressBar.setProgress(1);
            }

            //TODO: modify
        }

    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
        this.fileProgressBar.setProgress(0);
        progressPercentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        this.fileProgressBar.progressProperty(),
                                        100
                                )
                        ),
                        " %"
                ));
    };
}
