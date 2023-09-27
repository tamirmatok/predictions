package ui.app.controller;

import dto.impl.simulation.SimulationDTO;
import engine.impl.Engine;
import engine.schema.generated.PRDWorld;
import javafx.animation.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ui.app.subComponents.header.controller.HeaderComponentController;
import ui.app.subComponents.details.controller.DetailsComponentController;
import ui.app.subComponents.newExecution.controller.NewExecutionComponentController;
import ui.app.subComponents.results.controller.ResultsComponentController;

import java.util.HashMap;

public class AppController {

    @FXML
    private TabPane mainTab;
    //View components
    @FXML
    private Tab detailsTab;
    @FXML
    private Tab newExecutionTab;
    @FXML
    private Tab resultsTab;
    @FXML
    private GridPane headerComponent;
    @FXML
    private ScrollPane detailsComponent;
    @FXML
    private GridPane newExecutionComponent;
    @FXML
    private GridPane resultsComponent;


    //Controllers
    @FXML
    private HeaderComponentController headerComponentController;
    @FXML
    private DetailsComponentController detailsComponentController;
    @FXML
    private NewExecutionComponentController newExecutionComponentController;
    @FXML
    private ResultsComponentController resultsComponentController;


    //Model
    private final Engine engine;


    // Properties
    private SimpleBooleanProperty isSystemLoaded;
    private SimpleBooleanProperty isSystemRunning;


    public AppController() {
        this.engine = new Engine();
        this.isSystemLoaded = new SimpleBooleanProperty(false);
        this.isSystemRunning = new SimpleBooleanProperty(false);

    }

    @FXML
    public void initialize() {

        //Set the main controller to the sub controllers
        // TODO: add the other controllers
        if (headerComponentController != null) {
            headerComponentController.setMainController(this);
        }
        if (detailsComponentController != null) {
            detailsComponentController.setMainController(this);
        }
        if (newExecutionComponentController != null) {
            newExecutionComponentController.setMainController(this);
        }
        if (resultsComponentController != null) {
            resultsComponentController.setMainController(this);
        }
        // Binding different properties
        detailsTab.disableProperty().bind(this.isSystemLoaded.not());
        newExecutionTab.disableProperty().bind(this.isSystemLoaded.not());
        resultsTab.disableProperty().bind(this.isSystemRunning.not());

    }

    public Engine getEngine() {
        return engine;
    }

    public void setSystemLoaded(boolean isSystemLoaded) {
        this.isSystemLoaded.set(isSystemLoaded);
    }

    public void setSystemRunning(boolean isSystemRunning) {
        this.isSystemRunning.set(isSystemRunning);
    }

    public void setDetails(PRDWorld prdWorld) {
        this.detailsComponentController.setDetails(prdWorld);
        addAnimation(-1);
    }

    public void setNewExecution() {
        this.newExecutionComponentController.setEntitiesPopulationForms();
        this.newExecutionComponentController.setEnvironmentForms();
    }

    public void switchToResultsTab() {
        this.mainTab.getSelectionModel().select(this.resultsTab);
    }

    public void runSimulation(HashMap<String, String> envPropertyNameToEnvPropertyValue, HashMap<String, String> entityNameToEntityPopulation) {
        SimulationDTO simulationDTO = engine.runSimulation(envPropertyNameToEnvPropertyValue, entityNameToEntityPopulation);
        if (simulationDTO.isSuccess()) {
            this.newExecutionComponentController.setInfo("Simulation " + simulationDTO.getSimulationExecutionDetails().getSimulationId() + " started successfully !");
            setSystemRunning(true);
            switchToResultsTab();
            this.addAnimation(simulationDTO.getSimulationExecutionDetails().getSimulationId());
            resultsComponentController.setSimulation(simulationDTO);
        }
        else{
            this.newExecutionComponentController.setInfo(simulationDTO.getErrorMessage());
        }
    }

    private void addAnimation(int SimulationId) {
        if (SimulationId == -1) {
            // No time to do it clearer
            Text text = new Text("Welcome to the world!!!!");
        }
        Text text = new Text("Simulation " + SimulationId + " started successfully !");
        text.setFont(new Font(14));
        text.setFill(Color.BLACK);

        // Create a FadeTransition to make the text disappear after 3 seconds
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), text);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        // Create a TranslateTransition to move the text from left to right
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(3), text);
        translateTransition.setFromX(0);
        translateTransition.setToX(400); // Move the text to the right

        resultsComponent.getChildren().add(text);

        // Play both animations concurrently
        ParallelTransition parallelTransition = new ParallelTransition(fadeOut, translateTransition);
        parallelTransition.play();


        // Schedule the removal of the text from the pane after 3 seconds
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        javafx.application.Platform.runLater(() -> {
                            resultsComponent.getChildren().remove(text);
                        });
                    }
                },
                3000 // Delay in milliseconds (3 seconds)
        );

    }

    public SimpleBooleanProperty isSystemLoaded() {
        return isSystemLoaded;
    }

    public void updateQueueManagement(int runningCount, int terminatedCount, int waitingCount) {
        headerComponentController.updateQueueManagement(runningCount, waitingCount, terminatedCount);
    }
}
