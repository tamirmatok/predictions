package ui.app.subComponents.newExecution.forms.entity;

import engine.schema.generated.PRDEntity;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EntityFormController {

    @FXML
    private TextField entityNameTextArea;
    @FXML
    private TextField setPopulationTextArea;

    public void setEntityForm(PRDEntity entity) {
        entityNameTextArea.setText(entity.getName());
    }
    public String getPopulation(){
        return setPopulationTextArea.getText();
    }

    public void reset() {
        setPopulationTextArea.setText("");
    }
}
