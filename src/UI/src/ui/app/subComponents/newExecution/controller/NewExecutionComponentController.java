package ui.app.subComponents.newExecution.controller;

import dto.impl.MessageDTO;
import dto.impl.PrdWorldDTO;
import engine.impl.Engine;
import engine.schema.generated.PRDEntity;
import engine.schema.generated.PRDEnvProperty;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import ui.app.controller.AppController;
import ui.app.subComponents.newExecution.forms.entity.EntityFormController;
import ui.app.subComponents.newExecution.forms.envProperty.EnvPropertyFormController;

import java.io.IOException;
import java.util.HashMap;

public class NewExecutionComponentController {
    @FXML
    private Button startBtn;
    @FXML
    private TextField infoTextField;
    private AppController mainController;
    @FXML
    private FlowPane entitiesPopulationArea;
    @FXML
    private FlowPane envPropertiesArea;

    // Properties
    @FXML
    private final SimpleStringProperty infoTextProperty;


    // Maps
    HashMap<String, EntityFormController> entityNameToEntityFormController = new HashMap<>();
    HashMap<String, EnvPropertyFormController> envPropertyNameToEnvPropertyFormController = new HashMap<>();


    @FXML
    public void initialize() {
        infoTextField.textProperty().bind(infoTextProperty);
    }


    public NewExecutionComponentController() {
        this.mainController = null;
        this.entityNameToEntityFormController = new HashMap<>();
        this.envPropertyNameToEnvPropertyFormController = new HashMap<>();
        this.infoTextProperty = new SimpleStringProperty();
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setEntitiesPopulationForms() {
        this.entityNameToEntityFormController = new HashMap<>();
        entitiesPopulationArea.getChildren().clear();
        Engine engine = this.mainController.getEngine();
        PrdWorldDTO prdWorldDTO = engine.getLoadedWorldDetails();
        for (PRDEntity prdEntity : prdWorldDTO.getPrdWorld().getPRDEntities().getPRDEntity()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/app/subComponents/newExecution/forms/entity/entityForm.fxml"));
                GridPane entityFormLayout = loader.load();
                EntityFormController entityFormController = loader.getController();
                entityFormController.setEntityForm(prdEntity);
                entityNameToEntityFormController.put(prdEntity.getName(), entityFormController);
                entitiesPopulationArea.getChildren().add(entityFormLayout);
            } catch (Error | Exception e) {
                this.setInfo("Failed to load entity form: " + e.toString());
            }
        }
    }

    public void setEnvironmentForms() {
        this.envPropertyNameToEnvPropertyFormController = new HashMap<>();
        envPropertiesArea.getChildren().clear();
        Engine engine = this.mainController.getEngine();
        PrdWorldDTO prdWorldDTO = engine.getLoadedWorldDetails();
        for (PRDEnvProperty prdEnvProperty : prdWorldDTO.getPrdWorld().getPRDEnvironment().getPRDEnvProperty()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/app/subComponents/newExecution/forms/envProperty/envPropertyForm.fxml"));
                GridPane envPropertyFormLayout = loader.load();
                EnvPropertyFormController envPropertyFormController = loader.getController();
                envPropertyFormController.setEnvPropertyForm(prdEnvProperty);
                envPropertyNameToEnvPropertyFormController.put(prdEnvProperty.getPRDName(), envPropertyFormController);
                envPropertiesArea.getChildren().add(envPropertyFormLayout);
            } catch (Error | Exception e) {
                this.setInfo("Failed to load environment property form: " + e.toString());
            }
        }
    }

    public void startBtnHandler(ActionEvent actionEvent) {
        try {
            HashMap<String, String> envPropertyNameToEnvPropertyValue = new HashMap<>();
            HashMap<String, String> entityNameToEntityPopulation = new HashMap<>();
            for (String entityName : entityNameToEntityFormController.keySet()) {
                EntityFormController entityFormController = entityNameToEntityFormController.get(entityName);
                entityNameToEntityPopulation.put(entityName, entityFormController.getPopulation());
            }
            for (String envPropertyName : envPropertyNameToEnvPropertyFormController.keySet()) {
                EnvPropertyFormController envPropertyFormController = envPropertyNameToEnvPropertyFormController.get(envPropertyName);
                envPropertyNameToEnvPropertyValue.put(envPropertyName, envPropertyFormController.getValue());
            }
            this.mainController.runSimulation(envPropertyNameToEnvPropertyValue, entityNameToEntityPopulation);
        } catch (Exception e) {
            this.setInfo("Failed to start simulation: " + e.toString());
        }
    }

    public void setInfo(String message) {
        this.infoTextProperty.set(message);
    }

    public void clearBtnHandler(ActionEvent actionEvent) {
        for (String entityName : entityNameToEntityFormController.keySet()) {
            EntityFormController entityFormController = entityNameToEntityFormController.get(entityName);
            entityFormController.reset();
        }
        for (String envPropertyName : envPropertyNameToEnvPropertyFormController.keySet()) {
            EnvPropertyFormController envPropertyFormController = envPropertyNameToEnvPropertyFormController.get(envPropertyName);
            envPropertyFormController.reset();
        }
    }
}
