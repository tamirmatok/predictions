package ui.app.subComponents.header.controller;

import dto.impl.MessageDTO;
import dto.impl.PrdWorldDTO;
import engine.impl.Engine;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.app.controller.AppController;
import java.io.File;

public class HeaderComponentController {
    private AppController mainController;
    @FXML private Label filePathLabel;
    @FXML private  Label infoLabel;
    private final SimpleStringProperty filaPathProperty;


    public HeaderComponentController() {

        this.filaPathProperty = new SimpleStringProperty("Please load xml file!");
    }

    @FXML
    public void initialize() {
        this.filePathLabel.textProperty().bind(this.filaPathProperty);
    }


    public void LoadNewFileAction(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );
        Stage stage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            filaPathProperty.set(selectedFile.getAbsolutePath());
            Engine engine = this.mainController.getEngine();
            MessageDTO messageDTO = engine.loadSystemWorldFromXmlFile(filaPathProperty.get());
            this.mainController.setSystemLoaded(messageDTO.isSuccess());
            if (messageDTO.isSuccess()) {
                PrdWorldDTO prdWorldDTO = this.mainController.getEngine().getLoadedWorldDetails();
                if (prdWorldDTO.isSuccess()) {
                    this.mainController.setDetails(prdWorldDTO.getPrdWorld());
                    infoLabel.setText("File loaded successfully");
                }
            }
            else{
                infoLabel.setText(messageDTO.getData());
            }
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}