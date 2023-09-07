package ui.app.subComponents.details.cards.termination.controller;

import engine.schema.generated.PRDTermination;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class TerminationCardController {
    @FXML
    private TextField byUserTextArea;
    @FXML
    private TextField byTicksTextArea;
    @FXML
    private TextField bySecondsTextArea;

    public void setByUser(String byUser) {
        this.byUserTextArea.setText(byUser);
    }

    public void setByTicks(String byTicks) {
        this.byTicksTextArea.setText(byTicks);
    }

    public void setBySeconds(String bySeconds) {
        this.bySecondsTextArea.setText(bySeconds);
    }


    public void setTermination(PRDTermination prdTermination) {
        if (prdTermination.getPRDByUser() == null) {
            this.setByTicks(prdTermination.getPRDBySecondOrPRDByTicks().get(0).toString());
            this.setBySeconds(prdTermination.getPRDBySecondOrPRDByTicks().get(1).toString());
        }
        else{
            this.setByUser("True");
            this.setByTicks("False");
            this.setBySeconds("False");
        }
    }

}
