package ui.app.subComponents.details.cards.rule.controller;

import engine.schema.generated.PRDAction;
import engine.schema.generated.PRDActions;
import engine.schema.generated.PRDRule;
import engine.schema.generated.PRDRules;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import ui.app.subComponents.details.cards.rule.action.controller.ActionCardController;

import java.io.IOException;
import java.util.HashMap;

public class RuleCardController {


    @FXML
    private ListView actionList;
    @FXML
    private GridPane actionCardArea;
    @FXML
    private TextField ruleNameTextField;

    private final HashMap<String, GridPane> actionNameToActionCardLayout;

    public RuleCardController() {
        this.actionNameToActionCardLayout = new HashMap<String, GridPane>();
    }

    @FXML
    public void initialize() {
        this.actionList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.actionCardArea.getChildren().remove(this.actionNameToActionCardLayout.get(oldValue));
            this.actionCardArea.add(this.actionNameToActionCardLayout.get(newValue), 0, 0);
        });
    }

    public void setRule(PRDRule prdRule) throws IOException {
        int actionIndex = 1;
        ObservableList<String> actionOrderedName = FXCollections.observableArrayList();
        ruleNameTextField.setText(prdRule.getName());
        for (PRDAction prdAction : prdRule.getPRDActions().getPRDAction()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/app/subComponents/details/cards/rule/action/actionCard.fxml"));
            String actionUniqueName = Integer.toString(actionIndex) + ". " + prdAction.getType();
            GridPane actionCardLayout = loader.load();
            ActionCardController actionCardController = loader.getController();
            actionCardController.setAction(prdAction);
            this.actionNameToActionCardLayout.put(actionUniqueName, actionCardLayout);
            actionIndex++;
            actionOrderedName.add(actionUniqueName);
        }
        this.actionList.setItems(actionOrderedName);
        this.actionCardArea.getChildren().clear();
        this.actionCardArea.getChildren().add(this.actionNameToActionCardLayout.get(actionOrderedName.get(0)));
    }

}
