package ui.app.subComponents.results.executionDetails;

import javafx.beans.property.SimpleStringProperty;

public class EntityData {
    public final SimpleStringProperty entityName;
    public final SimpleStringProperty entityPopulation;

    public EntityData(String entityName, String entityPopulation) {
        this.entityName = new SimpleStringProperty(entityName);
        this.entityPopulation = new SimpleStringProperty(entityPopulation);
    }

    public SimpleStringProperty entityNameProperty() {
        return entityName;
    }

    public SimpleStringProperty entityPopulationProperty() {
        return entityPopulation;
    }
}



