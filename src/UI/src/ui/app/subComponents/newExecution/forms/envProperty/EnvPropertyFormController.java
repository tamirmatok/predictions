package ui.app.subComponents.newExecution.forms.envProperty;

import engine.schema.generated.PRDEnvProperty;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EnvPropertyFormController {
    @FXML private Label setValueLabel;
    @FXML private TextField propertyRangeTextField;
    @FXML private TextField propertyTypeTextField;
    @FXML private TextField propertyNameTextField;
    @FXML private TextField setValueTextField;
    @FXML private CheckBox setRandomCheckBox;
    @FXML private SimpleBooleanProperty setRandomProperty;


    public EnvPropertyFormController() {
        this.setRandomProperty = new SimpleBooleanProperty(true);
    }

    @FXML
    public void initialize() {
        setValueTextField.disableProperty().bind(setRandomCheckBox.selectedProperty());
        setValueLabel.disableProperty().bind(setRandomCheckBox.selectedProperty());
        // bind boolean property with yes or no string
        setRandomCheckBox.textProperty().bind(
                Bindings.when(setRandomCheckBox.selectedProperty())
                        .then("Yes")
                        .otherwise("No"));
    }

    public void setEnvPropertyForm(PRDEnvProperty envProperty) {
        propertyNameTextField.setText(envProperty.getPRDName());
        propertyTypeTextField.setText(envProperty.getType());
        if (envProperty.getPRDRange() != null){
            double from = envProperty.getPRDRange().getFrom();
            double to = envProperty.getPRDRange().getTo();
            propertyRangeTextField.setText(from + " - " + to);
        }
        else{
            propertyRangeTextField.setText("No Range");
        }
    }

    public String getValue() {
        if (setRandomCheckBox.isSelected()){
            return null;
        }
        else{
            return setValueTextField.getText();
        }
    }

    public void reset() {
        setValueTextField.setText("");
        setRandomCheckBox.setSelected(true);
    }
}
