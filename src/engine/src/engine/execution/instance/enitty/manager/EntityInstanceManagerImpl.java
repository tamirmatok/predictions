package engine.execution.instance.enitty.manager;

import engine.action.api.Action;
import engine.action.impl.SecondaryEntityDefinition;
import engine.action.impl.condition.impl.Condition;
import engine.action.impl.condition.impl.SingleCondition;
import engine.definition.entity.EntityDefinition;
import engine.definition.property.api.PropertyDefinition;
import engine.execution.context.Context;
import engine.execution.context.ContextImpl;
import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.enitty.EntityInstanceImpl;
import engine.execution.instance.environment.api.ActiveEnvironment;
import engine.execution.instance.property.PropertyInstance;
import engine.execution.instance.property.PropertyInstanceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class EntityInstanceManagerImpl implements EntityInstanceManager {

    private int count;
    private HashMap<String, EntityDefinition> entityNameToEntityDefinitions;
    private final ArrayList<EntityInstance> entityInstances;

    public EntityInstanceManagerImpl() {
        count = 0;
        entityInstances = new ArrayList<>();
        entityNameToEntityDefinitions = new HashMap<>();
    }
    @Override
    public EntityInstance create(EntityDefinition entityDefinition) {
        entityNameToEntityDefinitions.put(entityDefinition.getName(), entityDefinition);
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
    public ArrayList<EntityInstance> getEntityInstances() {
        return entityInstances;
    }

    @Override
    public ArrayList<EntityInstance> getInstancesByDefinition(EntityDefinition entityDefinition) {
        ArrayList<EntityInstance> definitionEntityInstances = new ArrayList<>();
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
    public HashMap<String, Integer> getEntityNameToEntityCount() {
        HashMap<String, Integer> entityNameToEntityCount = new HashMap<>();
        for (EntityInstance entityInstance : entityInstances) {
            String entityName = entityInstance.getEntityDefinition().getName();
            if (entityNameToEntityCount.containsKey(entityName)) {
                int currentCount = entityNameToEntityCount.get(entityName);
                entityNameToEntityCount.put(entityName, currentCount + 1);
            } else {
                entityNameToEntityCount.put(entityName, 1);
            }
        }
        return entityNameToEntityCount;
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
                    count--;
                    return;
                }
            }
        }
        catch (Exception e){
            System.out.println("error while trying to kill entity - " + e.getMessage());
        }
    }

    @Override
    public ArrayList<EntityInstance> getSecondaryEntityInstances(SecondaryEntityDefinition secondaryEntityDefinition , ActiveEnvironment activeEnvironment) {
        ArrayList<EntityInstance> secondaryEntityInstances = new ArrayList<>();
        ArrayList<EntityInstance>  allSecondaryEntityInstances = this.getInstancesByDefinition(secondaryEntityDefinition.getEntityDefinition());
        if (secondaryEntityDefinition.isAll()) {
            secondaryEntityInstances.addAll(allSecondaryEntityInstances);
        } else {
            int count = Integer.parseInt(secondaryEntityDefinition.getCount());

            // choose randomly count entities from allSecondaryEntityInstances
            Random random = new Random();
            for (int i = 0; i < count; i++) {
                int randomIndex = random.nextInt(allSecondaryEntityInstances.size());
                secondaryEntityInstances.add(allSecondaryEntityInstances.get(randomIndex));
            }

            // if condition is not null, filter secondaryEntityInstances by condition
            if (secondaryEntityDefinition.hasCondition()) {
                ArrayList<EntityInstance> filteredSecondaryEntityInstances = new ArrayList<>();
                for (EntityInstance entityInstance : secondaryEntityInstances) {
                    Condition condition = secondaryEntityDefinition.getCondition();
                    Context newContext = new ContextImpl(entityInstance, null, this, activeEnvironment, null);
                    if (condition.calcCondition(newContext)) {
                        filteredSecondaryEntityInstances.add(entityInstance);
                    }
                }
                return filteredSecondaryEntityInstances;
            }
        }
        return secondaryEntityInstances;
    }

    public EntityDefinition getEntityDefinitionByName(String entityName){
        return entityNameToEntityDefinitions.get(entityName);
    }
    public HashMap<String, EntityDefinition> getEntityNameToEntityDefinition(){
        return entityNameToEntityDefinitions;
    }

}
