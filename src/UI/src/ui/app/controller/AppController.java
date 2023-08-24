package ui.app.controller;

import engine.impl.Engine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import ui.app.subComponents.header.controller.HeaderController;
import ui.app.subComponents.details.controller.DetailsComponentController;

public class AppController {

    @FXML private GridPane headerComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private DetailsComponentController detailsComponentController;
    private Engine engine;
    private SimpleBooleanProperty isSystemLoaded;


    public AppController(){
        this.engine = new Engine();
        this.isSystemLoaded = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize() {

        if (headerComponentController != null ) {
            headerComponentController.setMainController(this);
        }
        if (detailsComponentController != null ) {
            detailsComponentController.setMainController(this);
        }
    }


    public void setSystemLoaded(boolean isSystemLoaded){
        this.isSystemLoaded.set(isSystemLoaded);
    }

    public Engine getEngine() {
        return engine;
    }
}
