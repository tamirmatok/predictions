package ui.app.subComponents.results.executionResults;

import dto.impl.simulation.EntityReport;
import dto.impl.simulation.PropertyValueCount;
import dto.impl.simulation.SimulationExecutionDetails;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import ui.app.controller.AppController;

import java.util.ArrayList;
import java.util.HashMap;

public class ExecutionResultsController {

    public TextField selectedEntityTextField;
    public TextField selectedPropertyTextField;
    @FXML
    private GridPane executionResultLayout;
    @FXML
    private AnchorPane entityCountGraphArea;
    @FXML
    private AnchorPane propertyValueHistogramArea;
    @FXML
    private ListView entitiesNameListView;
    @FXML
    private ListView propertiesNamesListView;
    private AppController mainController;
    private final HashMap<String, LineChart> entityNameToEntityCountGraph;
    private final HashMap<String, HashMap<String, BarChart>> entityNameToPropertiesHistogram;


    //properties
    private final SimpleStringProperty selectedEntity;
    private final SimpleStringProperty selectedProperty;


    public ExecutionResultsController() {
        this.selectedEntity = new SimpleStringProperty();
        this.selectedProperty = new SimpleStringProperty();
        this.entityNameToEntityCountGraph = new HashMap<>();
        this.entityNameToPropertiesHistogram = new HashMap<>();
    }

    @FXML
    public void initialize() {
        selectedEntityTextField.textProperty().bind(selectedEntity);
        selectedPropertyTextField.textProperty().bind(selectedProperty);
        entitiesNameListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedEntity.setValue((String) newValue);
            // update entity graph
            entityCountGraphArea.getChildren().clear();
            entityCountGraphArea.getChildren().add(entityNameToEntityCountGraph.get(newValue));
            // update properties list view
            ObservableList<String> propertiesNames = FXCollections.observableArrayList();
            for (String propertyName : entityNameToPropertiesHistogram.get(newValue).keySet()) {
                propertiesNames.add(propertyName);
            }
            propertiesNamesListView.getItems().clear();
            propertiesNamesListView.setItems(propertiesNames);
        });
        propertiesNamesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedProperty.setValue((String) newValue);
            // update property histogram
            propertyValueHistogramArea.getChildren().clear();
            propertyValueHistogramArea.getChildren().add(entityNameToPropertiesHistogram.get(selectedEntity.getValue()).get(newValue));
        });
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setResults(SimulationExecutionDetails simulationExecutionDetails) {
        ObservableList<String> entitiesNames = FXCollections.observableArrayList();
        for (EntityReport entityReport : simulationExecutionDetails.getEntityReports()) {
            // update entities list
            entitiesNames.add(entityReport.getEntityName());

            // create entity graph
            LineChart entityCountGraph = createEntityCountGraph(entityReport);
            entityNameToEntityCountGraph.put(entityReport.getEntityName(), entityCountGraph);

            entityNameToPropertiesHistogram.put(entityReport.getEntityName(), new HashMap<>());
            ObservableList<String> propertiesNames = FXCollections.observableArrayList();
            for (PropertyValueCount propertyValueCount : entityReport.getPropertyValueCounts()) {
                String propertyName = propertyValueCount.getPropertyName();
                // create property histogram
                BarChart propertyHistogram = createPropertyHistogram(propertyValueCount);
                entityNameToPropertiesHistogram.get(entityReport.getEntityName()).put(propertyName, propertyHistogram);
            }
        }
        entitiesNameListView.setItems(entitiesNames);
        propertiesNamesListView.getSelectionModel().selectFirst();
    }

    private LineChart createEntityCountGraph(EntityReport entityReport) {
        ArrayList<Integer> tickPopulationCounts = entityReport.getTickPopulationCounts();
        // Create x and y axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        // Create the line chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Tick Count");

        // Populate the series with data
        ObservableList<XYChart.Data<Number, Number>> data = FXCollections.observableArrayList();
        for (int i = 0; i < tickPopulationCounts.size(); i++) {
            data.add(new XYChart.Data<>(i, tickPopulationCounts.get(i)));
        }
        series.setData(data);

        // Add the series to the chart
        lineChart.getData().add(series);
        // fit the size of the line chart to the size of the anchor pane even if resizing
        // make it cant be bigger than the anchor pane


        lineChart.prefWidthProperty().bind(entityCountGraphArea.widthProperty());
        lineChart.prefHeightProperty().bind(entityCountGraphArea.heightProperty());
//        lineChart.maxHeightProperty().bind(entityCountGraphArea.heightProperty());
//        lineChart.maxWidthProperty().bind(entityCountGraphArea.widthProperty());

        return lineChart;
    }


    private BarChart createPropertyHistogram(PropertyValueCount propertyValueCount) {

        // Create x and y axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();


        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Property Value Histogram");
        xAxis.setLabel("Property Value");
        yAxis.setLabel("Count");

        // Create the data series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(propertyValueCount.getPropertyName());

        // Add data points to the series
        for (String value : propertyValueCount.getPropertyValueCount().keySet()) {
            series.getData().add(new XYChart.Data<>(value, propertyValueCount.getPropertyValueCount().get(value)));
        }
        // Add the series to the chart
        barChart.getData().add(series);
        // fit the size of the bar chart to the size of the anchor pane
        barChart.prefWidthProperty().bind(propertyValueHistogramArea.widthProperty());
        barChart.prefHeightProperty().bind(propertyValueHistogramArea.heightProperty());
        return barChart;
    }

    public GridPane getLayout() {
        return executionResultLayout;
    }
}

