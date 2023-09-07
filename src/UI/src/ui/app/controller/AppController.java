package ui.app.controller;

import dto.impl.PrdWorldDTO;
import engine.impl.Engine;
import engine.schema.generated.PRDWorld;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import ui.app.subComponents.header.controller.HeaderComponentController;
import ui.app.subComponents.details.controller.DetailsComponentController;

public class AppController {

    //View components
    @FXML private Tab detailsTab;
    @FXML private Tab newExecutionTab;
    @FXML private Tab resultsTab;
    @FXML private ScrollPane headerComponent;
    @FXML private ScrollPane detailsComponent;



    //Controllers
    @FXML private HeaderComponentController headerComponentController;
    @FXML private DetailsComponentController detailsComponentController;


    //Model
    private final Engine engine;


    // Properties
    private SimpleBooleanProperty isSystemLoaded;


    public AppController(){
        this.engine = new Engine();
        this.isSystemLoaded = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize() {

        //Set the main controller to the sub controllers
        // TODO: add the other controllers
        if (headerComponentController != null ) {
            headerComponentController.setMainController(this);
        }
        if (detailsComponentController != null ) {
            detailsComponentController.setMainController(this);
        }

        // Binding different properties
        detailsTab.disableProperty().bind(this.isSystemLoaded.not());
        newExecutionTab.disableProperty().bind(this.isSystemLoaded.not());
        resultsTab.disableProperty().bind(this.isSystemLoaded.not());
    }
    public void setSystemLoaded(boolean isSystemLoaded){
        this.isSystemLoaded.set(isSystemLoaded);
    }
    public Engine getEngine() {
        return engine;
    }
    public void setDetails(PRDWorld prdWorld){
        this.detailsComponentController.setDetails(prdWorld);
    }
}
