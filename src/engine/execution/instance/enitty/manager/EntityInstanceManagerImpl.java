package engine.execution.instance.enitty.manager;

import engine.definition.entity.EntityDefinition;
import engine.definition.property.api.PropertyDefinition;
import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.enitty.EntityInstanceImpl;
import engine.execution.instance.property.PropertyInstance;
import engine.execution.instance.property.PropertyInstanceImpl;

import java.util.ArrayList;
import java.util.List;

public class EntityInstanceManagerImpl implements EntityInstanceManager {

    private int count;
    private List<EntityInstance> entityInstances;

    public EntityInstanceManagerImpl() {
        count = 0;
        entityInstances = new ArrayList<>();
    }

    @Override
    public EntityInstance create(EntityDefinition entityDefinition) {

        count++;
        EntityInstance newEntityInstance = new EntityInstanceImpl(entityDefinition, count);
        entityInstances.add(newEntityInstance);

        for (PropertyDefinition propertyDefinition : entityDefinition.getProps()) {
            Object value = propertyDefinition.generateValue();
            PropertyInstance newPropertyInstance = new PropertyInstanceImpl(propertyDefinition, value);
            newEntityInstance.addPropertyInstance(newPropertyInstance);
        }

        return newEntityInstance;
    }

    @Override
    public List<EntityInstance> getEntityInstances() {
        return entityInstances;
    }

    @Override
    public List<EntityInstance> getInstancesByDefinition(EntityDefinition entityDefinition) {
        List<EntityInstance> definitionEntityInstances = new ArrayList<>();
        for (EntityInstance entityInstance: entityInstances) {
            if (entityInstance.getEntityDefinition().equals(entityDefinition)) {
                definitionEntityInstances.add(entityInstance);
            }
        }
        return definitionEntityInstances;
    }

    @Override
    public EntityInstance getInstanceByName(String name) {

        for (EntityInstance entityInstance : entityInstances) {
            EntityDefinition entityDefinition = entityInstance.getEntityDefinition();
            if (entityDefinition.getName().equals(name)) {
                return entityInstance;
            }
        }
        return null;
    }

    public EntityInstance getEntityInstanceById(int id){
        for (EntityInstance entityInstance: entityInstances){
            if (entityInstance.getId() == id){
                return entityInstance;
            }
        }
        return null;
    }

    @Override
    public void killEntity(int id){
        try {
            EntityInstance entityInstanceToKill = this.getEntityInstanceById(id);
            int currentPopulation = entityInstanceToKill.getEntityDefinition().getPopulation();
            entityInstanceToKill.getEntityDefinition().setPopulation(currentPopulation - 1);
            for (int i=0;i< entityInstances.size(); i++){
                EntityInstance entityInstance = entityInstances.get(i);
                if (entityInstance.equals(entityInstanceToKill)){
                    entityInstances.remove(i);
                    return;
                }
            }
        }
        catch (Exception e){
            System.out.println("error");
        }
    }
}
