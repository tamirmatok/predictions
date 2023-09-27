package ui.app.subComponents.details.cards.termination.controller;

import engine.schema.generated.PRDBySecond;
import engine.schema.generated.PRDByTicks;
import engine.schema.generated.PRDTermination;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;

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
            this.setByUser("No");
            List<Object> terminationConditions = prdTermination.getPRDBySecondOrPRDByTicks();
            for (Object terminationCondition : terminationConditions) {
                if (terminationCondition instanceof PRDByTicks) {
                    int ticks = ((PRDByTicks) terminationCondition).getCount();
                    this.setByTicks(String.valueOf(ticks));
                } else if (terminationCondition instanceof PRDBySecond) {
                    int seconds = ((PRDBySecond) terminationCondition).getCount();
                    this.setBySeconds(String.valueOf(seconds));
                }
            }
        }
        else{
            this.setByUser("Yes");
            this.setByTicks("unlimited");
            this.setBySeconds("unlimited");
        }
    }

}
