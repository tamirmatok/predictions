package ui.app.subComponents.results.controller;

import dto.impl.simulation.SimulationDTO;
import dto.impl.simulation.SimulationExecutionDetails;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import ui.app.controller.AppController;
import ui.app.subComponents.results.executionDetails.ExecutionDetailsController;
import ui.app.subComponents.results.executionResults.ExecutionResultsController;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ResultsComponentController {
    @FXML private Label executionResultsLabel;
    @FXML private GridPane executionDetailsArea;
    @FXML private GridPane executionResultsArea;
    @FXML private ListView executionList;
    private final HashMap<Integer, ExecutionDetailsController> simulationIdToSimulationExecutionDetailsController;
    private final HashMap<Integer, ExecutionResultsController> simulationIdToExecutionResultsController;

    private final HashMap<Integer, Boolean> simulationIdToIsSimulationTerminated;
    private AppController mainController;

    public ResultsComponentController() {
        this.simulationIdToSimulationExecutionDetailsController = new HashMap<>();
        this.simulationIdToExecutionResultsController = new HashMap<>();
        this.simulationIdToIsSimulationTerminated = new HashMap<>();
    }

    @FXML
    public void initialize() {
        executionList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Integer selectedSimulationId = (Integer) newValue;
            ExecutionDetailsController executionDetailsController = simulationIdToSimulationExecutionDetailsController.get(selectedSimulationId);
            ExecutionResultsController executionResultsController = simulationIdToExecutionResultsController.get(selectedSimulationId);

            executionDetailsArea.getChildren().clear();
            executionDetailsArea.getChildren().add(executionDetailsController.getLayout());
            executionResultsArea.getChildren().clear();
            executionResultsArea.getChildren().add(executionResultsController.getLayout());
        });
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(this::updateSimulationExecutionDetails, 0, 200, TimeUnit.MILLISECONDS);
    }

    private void updateSimulationExecutionDetails() {
        AtomicInteger runningCount = new AtomicInteger();
        AtomicInteger terminatedCount =  new AtomicInteger();
        AtomicInteger waitingCount =  new AtomicInteger();

        if (mainController != null){
            Platform.runLater(() -> {
                HashMap<Integer, SimulationExecutionDetails> simulationIdToSED = mainController.getEngine().getSimulationsExecutionDetails();
                if (simulationIdToSED == null) {
                    return;
                }
                for (Map.Entry<Integer, SimulationExecutionDetails> entry : simulationIdToSED.entrySet()) {
                    SimulationExecutionDetails simulationExecutionDetails = entry.getValue();
                    switch(simulationExecutionDetails.getSimulationStatus()){
                        case "Running":
                        case "Paused":
                            runningCount.getAndIncrement();
                            break;
                        case "Terminated":
                            terminatedCount.getAndIncrement();
                            break;
                        case "Pending":
                            waitingCount.getAndIncrement();
                            break;
                        default:
                            break;
                    }
                    mainController.updateQueueManagement(runningCount.get(), terminatedCount.get(), waitingCount.get());
                    this.updateSimulationExecutionDetails(simulationExecutionDetails);
                }
            });
        }
    }

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    public void setSimulation(SimulationDTO simulationDTO) {
        if (simulationDTO.isSuccess()) {
            try {
                SimulationExecutionDetails simulationExecutionDetails = simulationDTO.getSimulationExecutionDetails();
                if (simulationExecutionDetails == null) {
                    return;
                }
                this.setExecutionDetailsController(simulationExecutionDetails);
                this.setExecutionResultsController(simulationExecutionDetails);
                this.simulationIdToIsSimulationTerminated.put(simulationExecutionDetails.getSimulationId(), false);

            } catch (Exception e) {
                System.out.println("Error while trying to set simulation");
            }
        }
    }

    private void setExecutionDetailsController(SimulationExecutionDetails simulationExecutionDetails) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/app/subComponents/results/executionDetails/executionDetails.fxml"));
            loader.load();
            ExecutionDetailsController executionDetailsController = loader.getController();
            executionDetailsController.setMainController(mainController);
            simulationIdToSimulationExecutionDetailsController.put(simulationExecutionDetails.getSimulationId(), executionDetailsController);
            executionList.getItems().add(simulationExecutionDetails.getSimulationId());
            executionList.getSelectionModel().select(simulationExecutionDetails.getSimulationId());
        } catch (Exception e) {
            System.out.println("Error in ResultsComponentController.setExecutionDetailsController");
        }
    }

    private void setExecutionResultsController(SimulationExecutionDetails simulationExecutionDetails) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/app/subComponents/results/executionResults/executionResults.fxml"));
            loader.load();
            ExecutionResultsController executionResultsController = loader.getController();
            executionResultsController.setMainController(mainController);
            simulationIdToExecutionResultsController.put(simulationExecutionDetails.getSimulationId(), executionResultsController);
        } catch (Exception e) {
            System.out.println("Error in ResultsComponentController.setExecutionResultsController");
        }
    }

    public void updateSimulationExecutionDetails(SimulationExecutionDetails simulationExecutionDetails) {
        if (simulationExecutionDetails == null) {
            return;
        }
        int simulationId = simulationExecutionDetails.getSimulationId();

        ExecutionDetailsController executionDetailsController = simulationIdToSimulationExecutionDetailsController.get(simulationId);
        ExecutionResultsController executionResultsController = simulationIdToExecutionResultsController.get(simulationId);
        executionDetailsController.update(simulationExecutionDetails);

        // we should update results only if the simulation is terminated and we didn't update the results yet
        if (simulationExecutionDetails.getSimulationStatus().equals("Terminated") && !simulationIdToIsSimulationTerminated.get(simulationId)) {
            simulationIdToIsSimulationTerminated.put(simulationId, true);
            executionResultsController.setResults(simulationExecutionDetails);
        }
    }
}
