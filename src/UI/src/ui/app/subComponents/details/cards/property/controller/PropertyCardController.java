package ui.app.subComponents.details.cards.property.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PropertyCardController {
    public TextField isRandomTextField;
    @FXML private TextField propertyRangeTextField;
    @FXML private TextField propertyNameTextField;
    @FXML private TextField propertyTypeTextField;


    public void setPropertyName(String propertyName){
        propertyNameTextField.setText(propertyName);
    }
    public void setPropertyType(String propertyType){propertyTypeTextField.setText(propertyType);}

    public void setPropertyRange(String propertyRange){
        propertyRangeTextField.setText(propertyRange);
    }

    public void setIsRandom(String isRandom){
        isRandomTextField.setText(isRandom);
    }

    public Node getLayout() {
        return this.propertyNameTextField.getParent();
    }
}
