package ui.app.subComponents.details.cards.grid;

import javafx.scene.control.TextField;

public class GridCardController {
    public TextField rowNumberTextArea;
    public TextField colNumberTextArea;


    public void setRowNumber(String rowNumber){
        rowNumberTextArea.setText(rowNumber);
    }

    public void setColumnNumber(String columnNumber){
        colNumberTextArea.setText(columnNumber);
    }


}
