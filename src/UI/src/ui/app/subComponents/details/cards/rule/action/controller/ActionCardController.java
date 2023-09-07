package ui.app.subComponents.details.cards.rule.action.controller;

import engine.schema.generated.PRDAction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ActionCardController {
    @FXML private GridPane argumentsGridPane;
    @FXML private ScrollPane argumentsAreaScrollPane;
    @FXML private TextField actionTypeTextArea;
    @FXML private TextField primaryEntityTextArea;
    @FXML private TextField secondaryEntityTextArea;

    public void setAction(PRDAction prdAction) {
        this.actionTypeTextArea.setText(prdAction.getType());
        this.primaryEntityTextArea.setText(prdAction.getEntity());
        if (prdAction.getPRDSecondaryEntity() != null)
            this.secondaryEntityTextArea.setText(prdAction.getPRDSecondaryEntity().getEntity());
        else{
            this.secondaryEntityTextArea.setText("No secondary entity");
        }
        String actionType = prdAction.getType();
        switch(actionType){
            case "increase":
            case "decrease":
                argumentsGridPane.addRow(1, new TextField("property"), new TextField(prdAction.getProperty()));
                argumentsGridPane.addRow(2, new TextField("by"), new TextField(prdAction.getBy()));
                break;
            case "set":
                argumentsGridPane.addRow(1, new TextField("property"), new TextField(prdAction.getProperty()));
                argumentsGridPane.addRow(2, new TextField("value"), new TextField(prdAction.getValue()));
                break;
            case "calculation":
                argumentsGridPane.addRow(1, new TextField("result-prop"), new TextField(prdAction.getResultProp()));
                if (prdAction.getPRDMultiply() != null){
                    argumentsGridPane.addRow(2, new TextField("multiply-arg1"), new TextField(prdAction.getPRDMultiply().getArg1()));
                    argumentsGridPane.addRow(3, new TextField("multiply-arg2"), new TextField(prdAction.getPRDMultiply().getArg2()));
                }
                else{
                    argumentsGridPane.addRow(2, new TextField("divide-arg1"), new TextField(prdAction.getPRDDivide().getArg1()));
                    argumentsGridPane.addRow(3, new TextField("divide-arg2"), new TextField(prdAction.getPRDDivide().getArg2()));
                }
                break;
            case "condition":
                int rowIndex = 1;
                argumentsGridPane.addRow(rowIndex++, new TextField("singularity"), new TextField(prdAction.getPRDCondition().getSingularity()));
                argumentsGridPane.addRow(rowIndex++, new TextField("property"), new TextField(prdAction.getPRDCondition().getProperty()));
                switch(prdAction.getPRDCondition().getSingularity()){
                    case "single":
                        argumentsGridPane.addRow(rowIndex++, new TextField("operator"), new TextField(prdAction.getPRDCondition().getOperator()));
                        argumentsGridPane.addRow(rowIndex++, new TextField("value"), new TextField(prdAction.getPRDCondition().getValue()));
                        break;
                    case "multiple":
                        argumentsGridPane.addRow(rowIndex++, new TextField("logical"), new TextField(prdAction.getPRDCondition().getLogical()));
                        argumentsGridPane.addRow(rowIndex++, new TextField("multiple action count"), new TextField(prdAction.getPRDCondition().getPRDCondition().size() + ""));
                }
                argumentsGridPane.addRow(rowIndex++, new TextField("then action count"), new TextField(prdAction.getPRDThen().getPRDAction().size() + ""));
                argumentsGridPane.addRow(rowIndex, new TextField("else action count"), new TextField(prdAction.getPRDElse().getPRDAction().size() + ""));
                break;
            case "replace":
                argumentsGridPane.addRow(1, new TextField("kill"), new TextField(prdAction.getKill()));
                argumentsGridPane.addRow(2, new TextField("create"), new TextField(prdAction.getCreate()));
                argumentsGridPane.addRow(3, new TextField("mode"), new TextField(prdAction.getMode()));
                break;
            case "proximity":
                argumentsGridPane.addRow(1, new TextField("source-entity"), new TextField(prdAction.getPRDBetween().getSourceEntity()));
                argumentsGridPane.addRow(2, new TextField("target-entity"), new TextField(prdAction.getPRDBetween().getTargetEntity()));
                argumentsGridPane.addRow(3, new TextField("env-depth-of"), new TextField(prdAction.getPRDEnvDepth().getOf()));
                argumentsGridPane.addRow(4, new TextField("operator"), new TextField(prdAction.getPRDActions().getPRDAction().size() + ""));
                argumentsGridPane.addRow(5, new TextField("value"), new TextField(prdAction.getValue()));
                break;
            default:
                break;
        }
    }
}
