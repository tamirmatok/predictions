package ui.app.subComponents.details.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import ui.app.controller.AppController;

public class DetailsComponentController {

    @FXML private Button envBtn;
    @FXML private Button gridBtn;
    @FXML private Button entitiesBtn;
    @FXML private Button rulesBtn;
    @FXML private Button terminationBtn;
    @FXML private GridPane details;
    AppController mainController;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}
