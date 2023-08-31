package ui.app.subComponents.details.controller;

import engine.schema.generated.PRDEntities;
import engine.schema.generated.PRDEnvironment;
import engine.schema.generated.PRDWorld;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.*;
import ui.app.controller.AppController;
import ui.app.subComponents.details.cards.entity.EntityCardController;
import ui.app.subComponents.details.cards.grid.GridCardController;
import ui.app.subComponents.details.cards.property.controller.PropertyCardController;

import java.io.IOException;
import java.util.HashMap;

public class DetailsComponentController {

    @FXML private TitledPane detailsViewTitle;
    @FXML private FlowPane detailsViewContainer;
    @FXML private TreeView<String> treeView;

    private HashMap<String, FlowPane> detailComponentToDetailsView;
    AppController mainController;


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public DetailsComponentController() {
        this.detailComponentToDetailsView = new HashMap<String, FlowPane>();
    }

    @FXML
    public void initialize() {
        this.setTreeItems();
        detailsViewTitle.textProperty().bind(treeView.getSelectionModel().selectedItemProperty().getValue().valueProperty());
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String selectedComponent = newValue.getValue();
            detailsViewContainer.getChildren().clear();
            detailsViewContainer.getChildren().add(detailComponentToDetailsView.get(selectedComponent));
            detailsViewContainer.hgapProperty().set(10);
            detailsViewContainer.vgapProperty().set(10);
        });
    }
    public void setDetails(PRDWorld prdWorld){
        setEnvironmentView(prdWorld.getPRDEnvironment());
        setGridView(prdWorld.getPRDGrid());
        setEntitiesView(prdWorld.getPRDEntities());
//        setRulesView(prdWorld.getPRDRules());
    }

    private void setEntitiesView(PRDEntities prdEntities) {
        FlowPane entitiesDetailsView = new FlowPane();
        prdEntities.getPRDEntity().forEach(prdEntity -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/app/subComponents/details/cards/entity/entityCard.fxml"));
                GridPane entityCardLayout = loader.load();
                EntityCardController entityCardController = loader.getController();
                entityCardController.setEntity(prdEntity);
                entitiesDetailsView.getChildren().add(entityCardLayout);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        detailComponentToDetailsView.put("Entities", entitiesDetailsView);
    }

    private void setEnvironmentView(PRDEnvironment prdEnvironment){
        FlowPane environmentDetailsView = new FlowPane();
        environmentDetailsView.hgapProperty().set(10);
        environmentDetailsView.vgapProperty().set(10);
        prdEnvironment.getPRDEnvProperty().forEach(prdEnvProperty -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/app/subComponents/details/cards/property/propertyCard.fxml"));
                GridPane propertyCardLayout = loader.load();
                PropertyCardController propertyCardController = loader.getController();
                propertyCardController.setPropertyName(prdEnvProperty.getPRDName());
                propertyCardController.setPropertyType(prdEnvProperty.getType());
                if (prdEnvProperty.getPRDRange() != null){
                    double from = prdEnvProperty.getPRDRange().getFrom();
                    double to = prdEnvProperty.getPRDRange().getTo();
                    propertyCardController.setPropertyRange(from + " - " + to);
                }
                else{
                    propertyCardController.setPropertyRange("No range");
                }
                environmentDetailsView.getChildren().add(propertyCardLayout);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        detailComponentToDetailsView.put("Environment", environmentDetailsView);
    }


    private void setGridView(PRDWorld.PRDGrid prdGrid){
        String rowNumber = String.valueOf(prdGrid.getRows());
        String columnNumber = String.valueOf(prdGrid.getColumns());
        FlowPane gridDetailsView = new FlowPane();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/app/subComponents/details/cards/grid/gridCard.fxml"));
            GridPane gridCardLayout = loader.load();
            GridCardController gridCardController = loader.getController();
            gridCardController.setRowNumber(rowNumber);
            gridCardController.setColumnNumber(columnNumber);
            gridDetailsView.getChildren().add(gridCardLayout);
            detailComponentToDetailsView.put("Grid", gridDetailsView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void setTreeItems(){
        TreeItem<String> rootItem = new TreeItem<>("World");
        TreeItem<String> environmentTreeItem = new TreeItem<>("Environment");
        TreeItem<String> gridTreeItem = new TreeItem<>("Grid");
        TreeItem<String> entitiesTreeItem = new TreeItem<>("Entities");
        TreeItem<String> rulesTreeItem = new TreeItem<>("Rules");
        TreeItem<String> terminationTreeItem = new TreeItem<>("Termination");
        rootItem.getChildren().addAll(environmentTreeItem, gridTreeItem, entitiesTreeItem, rulesTreeItem, terminationTreeItem);
        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);
        treeView.getSelectionModel().select(rootItem);
    }

}
