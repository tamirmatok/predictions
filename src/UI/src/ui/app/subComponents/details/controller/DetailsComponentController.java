package ui.app.subComponents.details.controller;

import engine.execution.instance.grid.Grid;
import engine.schema.generated.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import ui.app.controller.AppController;
import ui.app.subComponents.details.cards.entity.EntityCardController;
import ui.app.subComponents.details.cards.grid.GridCardController;
import ui.app.subComponents.details.cards.property.controller.PropertyCardController;
import ui.app.subComponents.details.cards.rule.controller.RuleCardController;
import ui.app.subComponents.details.cards.termination.controller.TerminationCardController;

import java.io.IOException;
import java.util.HashMap;

public class DetailsComponentController {

    AppController mainController;
    @FXML private ScrollPane detailsViewScrollPane;
    @FXML private TitledPane detailsViewTitle;
    @FXML private TreeView<String> treeView;
    private GridPane terminationLayout;
    private GridPane gridLayout;
    private final HashMap<String, GridPane> entityNameToEntityCardLayout;
    private final HashMap<String, GridPane> ruleNameToRuleCardLayout;
    private final HashMap<String, GridPane> envPropertyNameToEnvPropertyCardLayout;


    public DetailsComponentController() {
        this.entityNameToEntityCardLayout = new HashMap<String, GridPane>();
        this.ruleNameToRuleCardLayout = new HashMap<String, GridPane>();
        this.envPropertyNameToEnvPropertyCardLayout = new HashMap<String, GridPane>();
    }

    @FXML
    public void initialize() {
        this.setTreeItems();
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String selectedComponent = newValue.getValue();
            switch (newValue.getParent().getValue()){
                case "Entities":
                    detailsViewTitle.setText("Entities");
                    entityNameToEntityCardLayout.get(selectedComponent);
                    detailsViewScrollPane.contentProperty().setValue(entityNameToEntityCardLayout.get(selectedComponent));
                    break;
                case "Rules":
                    detailsViewTitle.setText("Rules");
                    detailsViewScrollPane.contentProperty().setValue(ruleNameToRuleCardLayout.get(selectedComponent));
                    break;
                case "Environment":
                    detailsViewTitle.setText("Environment");
                    detailsViewScrollPane.contentProperty().setValue(envPropertyNameToEnvPropertyCardLayout.get(selectedComponent));
                    break;
                case "World":
                    switch(selectedComponent) {
                        case "Termination":
                            detailsViewTitle.setText("Termination");
                            detailsViewScrollPane.contentProperty().setValue(terminationLayout);
                            break;
                        case "Grid":
                            detailsViewTitle.setText("Grid");
                            detailsViewScrollPane.contentProperty().setValue(gridLayout);
                            break;
                    }
                default:
                    break;
            }
        });
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setDetails(PRDWorld prdWorld){
        this.setEnvironmentView(prdWorld.getPRDEnvironment());
        this.setGridView(prdWorld.getPRDGrid());
        this.setEntitiesView(prdWorld.getPRDEntities());
        this.setRulesView(prdWorld.getPRDRules());
        this.setTermination(prdWorld.getPRDTermination());
    }

    private void setTermination(PRDTermination prdTermination) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/app/subComponents/details/cards/termination/terminationCard.fxml"));
            terminationLayout = loader.load();
            TerminationCardController terminationCardController = loader.getController();
            terminationCardController.setTermination(prdTermination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setEntitiesView(PRDEntities prdEntities) {
        prdEntities.getPRDEntity().forEach(prdEntity -> {
            try {
                String entityName = prdEntity.getName();
                CheckBoxTreeItem<String> entityTreeItem = new CheckBoxTreeItem<>(entityName);
                treeView.getRoot().getChildren().get(1).getChildren().add(entityTreeItem);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/app/subComponents/details/cards/entity/entityCard.fxml"));
                GridPane entityCardLayout = loader.load();
                EntityCardController entityCardController = loader.getController();
                entityCardController.setEntity(prdEntity);
                entityNameToEntityCardLayout.put(entityName, entityCardLayout);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setEnvironmentView(PRDEnvironment prdEnvironment){
        prdEnvironment.getPRDEnvProperty().forEach(prdEnvProperty -> {
            try {
                String envPropertyName = prdEnvProperty.getPRDName();
                //Add to tree view
                CheckBoxTreeItem<String> envPropertyTreeItem = new CheckBoxTreeItem<>(envPropertyName);
                treeView.getRoot().getChildren().get(0).getChildren().add(envPropertyTreeItem);
                //Load card layout
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
                propertyCardController.setIsRandom("No");
                envPropertyNameToEnvPropertyCardLayout.put(envPropertyName, propertyCardLayout);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void setGridView(PRDWorld.PRDGrid prdGrid){
        String rowNumber = String.valueOf(prdGrid.getRows());
        String columnNumber = String.valueOf(prdGrid.getColumns());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/app/subComponents/details/cards/grid/gridCard.fxml"));
            gridLayout = loader.load();
            GridCardController gridCardController = loader.getController();
            gridCardController.setRowNumber(rowNumber);
            gridCardController.setColumnNumber(columnNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void setRulesView(PRDRules prdRules){
        try {
            for (PRDRule prdRule : prdRules.getPRDRule()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/app/subComponents/details/cards/rule/ruleCard.fxml"));
            String ruleName = prdRule.getName();
            CheckBoxTreeItem<String> ruleTreeItem = new CheckBoxTreeItem<>(ruleName);
            treeView.getRoot().getChildren().get(2).getChildren().add(ruleTreeItem);
            GridPane gridCardLayout = loader.load();
            RuleCardController ruleCardController = loader.getController();
            ruleCardController.setRule(prdRule);
            ruleNameToRuleCardLayout.put(ruleName, gridCardLayout);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void setTreeItems(){
        TreeItem<String> rootItem = new TreeItem<>("World");
        TreeItem<String> environmentTreeItem = new TreeItem<>("Environment");
        TreeItem<String> entitiesTreeItem = new TreeItem<>("Entities");
        TreeItem<String> rulesTreeItem = new TreeItem<>("Rules");
        TreeItem<String> gridTreeItem = new TreeItem<>("Grid");
        TreeItem<String> terminationTreeItem = new TreeItem<>("Termination");
        rootItem.getChildren().addAll(environmentTreeItem,entitiesTreeItem, rulesTreeItem, gridTreeItem,  terminationTreeItem);
        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);
        treeView.getSelectionModel().select(rootItem);
    }

}
