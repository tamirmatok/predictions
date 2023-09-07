package ui.app.subComponents.details.cards.entity;

import engine.schema.generated.PRDEntity;
import engine.schema.generated.PRDProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import ui.app.subComponents.details.cards.property.controller.PropertyCardController;

import java.io.IOException;
import java.util.HashMap;

public class EntityCardController {
    @FXML private GridPane entityCard;
    @FXML private ListView<String> entityPropertiesList;
    @FXML private TextField entityNameTextArea;
    @FXML private GridPane propertyCardArea;
    private final HashMap<String, GridPane> entityPropertyNameToPropertyCardLayout;

    public EntityCardController() {

        this.entityPropertyNameToPropertyCardLayout = new HashMap<String, GridPane>();
    }

    @FXML
    public void initialize() {
        this.entityPropertiesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.propertyCardArea.getChildren().remove(this.entityPropertyNameToPropertyCardLayout.get(oldValue));
            this.propertyCardArea.add(this.entityPropertyNameToPropertyCardLayout.get(newValue), 0, 0);
            this.propertyCardArea.getChildren().add(this.entityPropertyNameToPropertyCardLayout.get(newValue));
        });
    }

    public void setEntity(PRDEntity prdEntity) throws IOException {
        ObservableList<String> entityProperties = FXCollections.observableArrayList();
        this.setEntityName(prdEntity.getName());

        for (PRDProperty prdProperty : prdEntity.getPRDProperties().getPRDProperty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/app/subComponents/details/cards/property/propertyCard.fxml"));
            GridPane propertyCardLayout = loader.load();
            PropertyCardController propertyCardController = loader.getController();
            propertyCardController.setPropertyName(prdProperty.getPRDName());
            propertyCardController.setPropertyType(prdProperty.getType());
            if (prdProperty.getPRDRange() != null) {
                double from = prdProperty.getPRDRange().getFrom();
                double to = prdProperty.getPRDRange().getTo();
                propertyCardController.setPropertyRange(from + " - " + to);
            } else {
                propertyCardController.setPropertyRange("No range");
            }
            if (prdProperty.getPRDValue().isRandomInitialize()) {
                propertyCardController.setIsRandom("Yes");
            } else {
                propertyCardController.setIsRandom("No");
            }
            entityProperties.add(prdProperty.getPRDName());
            this.entityPropertyNameToPropertyCardLayout.put(prdProperty.getPRDName(), propertyCardLayout);
        }

        this.entityPropertiesList.setItems(entityProperties);
        this.entityPropertiesList.getSelectionModel().selectFirst();
        this.propertyCardArea.getChildren().clear();
        this.propertyCardArea.getChildren().add(this.entityPropertyNameToPropertyCardLayout.get(entityProperties.get(0)));
    }

    public void setEntityName(String entityName) {
        this.entityNameTextArea.setText(entityName);
    }


}
