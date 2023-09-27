package ui.app.subComponents.results.executionDetails;

import dto.impl.simulation.EntityReport;
import dto.impl.simulation.SimulationExecutionDetails;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import ui.app.controller.AppController;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

public class ExecutionDetailsController {


    @FXML private TextField causeOfTerminationTextField;
    @FXML private Button resumeBtn;
    @FXML private Button pauseBtn;
    @FXML private Button stopBtn;
    @FXML private ProgressBar simulationProgressBar;
    @FXML private GridPane entityNameToEntityPopulationGridPane;
    @FXML private TextField simulationIdTextArea;
    @FXML private TextField simulationStatusTextArea;
    @FXML private TextField simulationCurrTickTextArea;
    @FXML private TextField simulationCurrSecondTextArea;
    @FXML private Label simulationPercentsLabel;
    @FXML private GridPane executionDetailsLayout;


    private AppController mainController;


    // Properties
    private final SimpleStringProperty simulationId;
    private final SimpleStringProperty simulationStatus;
    private final SimpleStringProperty simulationCurrTick;
    private final SimpleStringProperty simulationCurrSecond;
    private final SimpleDoubleProperty simulationProgressPercents;
    private final SimpleStringProperty causeOfTerminationProperty;


    public ExecutionDetailsController() {
        this.simulationId = new SimpleStringProperty();
        this.simulationStatus = new SimpleStringProperty();
        this.simulationCurrTick = new SimpleStringProperty();
        this.simulationCurrSecond = new SimpleStringProperty();
        this.simulationProgressPercents = new SimpleDoubleProperty(0);
        this.causeOfTerminationProperty = new SimpleStringProperty();
    }

    @FXML
    public void initialize() {
        simulationIdTextArea.textProperty().bind(simulationId);
        simulationStatusTextArea.textProperty().bind(simulationStatus);
        simulationCurrTickTextArea.textProperty().bind(simulationCurrTick);
        simulationCurrSecondTextArea.textProperty().bind(simulationCurrSecond);
        simulationProgressBar.progressProperty().bind(simulationProgressPercents);
        simulationPercentsLabel.textProperty().bind(simulationProgressBar.progressProperty().asString());
        causeOfTerminationTextField.textProperty().bind(causeOfTerminationProperty);
        causeOfTerminationTextField.disableProperty().bind(simulationStatus.isNotEqualTo("Terminated"));
    }
    public void update(SimulationExecutionDetails simulationExecutionDetails) {
        try {
            if (simulationExecutionDetails.getSimulationStatus().equals("Terminated")) {
                causeOfTerminationProperty.set(simulationExecutionDetails.getCauseOfTermination());
            }
            simulationId.set(String.valueOf(simulationExecutionDetails.getSimulationId()));
            simulationStatus.set(simulationExecutionDetails.getSimulationStatus());
            simulationCurrTick.set(String.valueOf(simulationExecutionDetails.getCurrentTick()));
            simulationCurrSecond.set(String.valueOf(simulationExecutionDetails.getCurrentSecond()));
            simulationProgressPercents.set(simulationExecutionDetails.getSimulationPercents());

            //update entities count table view
            int rowInd = 1;
            for (EntityReport entityReport : simulationExecutionDetails.getEntityReports()) {
                String entityName = entityReport.getEntityName();
                Integer entityPopulation = entityReport.getPopulation();
                TextField entityNameTextField = new TextField(entityName);
                TextField entityPopulationTextField = new TextField(String.valueOf(entityPopulation));
                entityNameTextField.setEditable(false);
                entityPopulationTextField.setEditable(false);
                entityNameToEntityPopulationGridPane.add(entityNameTextField, 0, rowInd);
                entityNameToEntityPopulationGridPane.add(entityPopulationTextField, 1, rowInd);
                rowInd++;
            }
        }
        catch (Exception e){
            causeOfTerminationProperty.set(e.getMessage());
        }
    }

    public GridPane getLayout() {
        return executionDetailsLayout;
    }

    public void simulationPauseBtnHandler(ActionEvent actionEvent) {
        mainController.getEngine().pauseSimulation(Integer.parseInt(simulationId.get()));
    }

    public void simulationResumeBtnHandler(ActionEvent actionEvent) {
        mainController.getEngine().resumeSimulation(Integer.parseInt(simulationId.get()));
    }

    public void simulationStopBtnHandler(ActionEvent actionEvent) {
        pauseBtn.setDisable(true);
        resumeBtn.setDisable(true);
        stopBtn.setDisable(true);
        mainController.getEngine().stopSimulation(Integer.parseInt(simulationId.get()));
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }


}
