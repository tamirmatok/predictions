package ui.app.subComponents.header.controller;

import dto.impl.MessageDTO;
import dto.impl.PrdWorldDTO;
import engine.impl.Engine;
import engine.schema.generated.PRDWorld;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.app.controller.AppController;
import java.io.File;

public class HeaderComponentController {
    private AppController mainController;
    @FXML private Label filePathLabel;
    @FXML private  Label infoLabel;
    @FXML private TextField finishedTasksTextArea;
    @FXML private TextField runningTasksTextArea;
    @FXML private TextField waitingTasksTextArea;
    @FXML private TextField queueSizeTextArea;
    @FXML private Label queueSizeLabel;
    @FXML private Label runningTasksLabel;
    @FXML private Label waitingTasksLabel;
    @FXML private Label finishedTasksLabel;
    private final SimpleStringProperty filaPathProperty;
    private final SimpleIntegerProperty queueSizeProperty;
    private final SimpleIntegerProperty runningTasksProperty;
    private final SimpleIntegerProperty waitingTasksProperty;
    private final SimpleIntegerProperty finishedTasksProperty;
    private boolean failedToLoadFile;


    public HeaderComponentController() {

        this.filaPathProperty = new SimpleStringProperty("Please load xml file!");
        this.queueSizeProperty = new SimpleIntegerProperty();
        this.runningTasksProperty = new SimpleIntegerProperty();
        this.waitingTasksProperty = new SimpleIntegerProperty();
        this.finishedTasksProperty = new SimpleIntegerProperty();
        this.failedToLoadFile = false;
    }

    @FXML
    public void initialize() {
        this.filePathLabel.textProperty().bind(this.filaPathProperty);
        this.queueSizeTextArea.textProperty().bind(this.queueSizeProperty.asString());
        this.runningTasksTextArea.textProperty().bind(this.runningTasksProperty.asString());
        this.waitingTasksTextArea.textProperty().bind(this.waitingTasksProperty.asString());
        this.finishedTasksTextArea.textProperty().bind(this.finishedTasksProperty.asString());
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
            PRDWorld prdWorld = engine.getLoadedWorldDetails().getPrdWorld();
            this.setQueueManagement(prdWorld.getPRDThreadCount(), 0,0,0);
            if (messageDTO.isSuccess()) {
                PrdWorldDTO prdWorldDTO = this.mainController.getEngine().getLoadedWorldDetails();
                if (prdWorldDTO.isSuccess()) {
                    this.mainController.setSystemLoaded(true);
                    this.mainController.setDetails(prdWorldDTO.getPrdWorld());
                    this.mainController.setNewExecution();
                    infoLabel.setText("File loaded successfully");
                    // set Color back to -fx-background-color: #cbf8f7;
                    infoLabel.setStyle("-fx-background-color: #cbf8f7;");
                }
            }
            else{
                this.setErrorInfo(messageDTO.getMessage());
            }
        }
    }

    private void setErrorInfo(String message) {
        infoLabel.setText(message);
        infoLabel.setTextFill(Color.BLACK);
        infoLabel.setStyle("-fx-background-color: red;");
        this.failedToLoadFile = true;
    }

    public void setQueueManagement(int queueSize, int runningTasks, int waitingTasks, int finishedTasks){
        this.queueSizeProperty.set(queueSize);
        this.runningTasksProperty.set(runningTasks);
        this.waitingTasksProperty.set(waitingTasks);
        this.finishedTasksProperty.set(finishedTasks);
    }

    public void updateQueueManagement(int runningTasks, int waitingTasks, int finishedTasks) {
        this.runningTasksProperty.set(runningTasks);
        this.waitingTasksProperty.set(waitingTasks);
        this.finishedTasksProperty.set(finishedTasks);
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
        this.queueSizeTextArea.disableProperty().bind(this.mainController.isSystemLoaded().not());
        this.runningTasksTextArea.disableProperty().bind(this.mainController.isSystemLoaded().not());
        this.waitingTasksTextArea.disableProperty().bind(this.mainController.isSystemLoaded().not());
        this.finishedTasksTextArea.disableProperty().bind(this.mainController.isSystemLoaded().not());
        this.queueSizeLabel.disableProperty().bind(this.mainController.isSystemLoaded().not());
        this.runningTasksLabel.disableProperty().bind(this.mainController.isSystemLoaded().not());
        this.waitingTasksLabel.disableProperty().bind(this.mainController.isSystemLoaded().not());
        this.finishedTasksLabel.disableProperty().bind(this.mainController.isSystemLoaded().not());
    }

}