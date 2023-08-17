package engine.execution.instance.world.impl;

import dto.impl.MessageDTO;
import engine.action.api.Action;
import engine.definition.entity.EntityDefinition;
import engine.definition.environment.api.EnvVariablesManager;
import engine.definition.property.api.PropertyDefinition;
import engine.definition.property.api.PropertyType;
import engine.execution.context.Context;
import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.enitty.manager.EntityInstanceManager;
import engine.execution.instance.enitty.manager.EntityInstanceManagerImpl;
import engine.execution.instance.environment.api.ActiveEnvironment;
import engine.execution.instance.environment.impl.ActiveEnvironmentImpl;
import engine.execution.instance.property.PropertyInstance;
import engine.execution.instance.property.PropertyInstanceImpl;
import engine.execution.instance.termination.impl.Termination;
import engine.execution.instance.world.api.WorldInstanceInterface;
import engine.rule.Rule;
import engine.schema.generated.*;
import engine.definition.world.api.WorldDefinition;
import engine.system.functions.SystemFunctions;

import java.util.ArrayList;


public class WorldInstance implements WorldInstanceInterface {

    private final WorldDefinition worldDefinition;
    private final EntityInstanceManager entityInstanceManager;
    private final ActiveEnvironment activeEnvironment;
    private ArrayList<Rule> rules;
    private Termination termination;
    private int countTick;
    private ArrayList<Rule> rulesOnRun;
    private ArrayList<Action> actionsOnRun;

    public WorldInstance(WorldDefinition worldDefinition) {
        this.worldDefinition = worldDefinition;
        this.entityInstanceManager = new EntityInstanceManagerImpl();
        this.activeEnvironment = new ActiveEnvironmentImpl();
        this.rules = new ArrayList<Rule>();
        this.termination = new Termination();
        this.countTick = 0;
        this.rulesOnRun = new ArrayList<Rule>();
        this.actionsOnRun = new ArrayList<Action>();
    }

    public void setWorldInstance() {
        this.setEntitiesInstances();
        this.rules = this.worldDefinition.getRules();
        this.termination = this.worldDefinition.getTermination();

    }

    public void setEntitiesInstances() {
        for (EntityDefinition entityDefinition : this.worldDefinition.getEntityDefinitions().values()) {
            for(int i=0; i < entityDefinition.getPopulation(); i++)
                entityInstanceManager.create(entityDefinition);
        }
    }


    public MessageDTO setEnvVariable(String envVariableName, Object value){
        try {
            PropertyDefinition propertyDefinition = this.worldDefinition.getEnvVariablesManager().getEnvVariable(envVariableName);
            PropertyInstance propertyInstance = new PropertyInstanceImpl(propertyDefinition, value);
            activeEnvironment.addPropertyInstance(propertyInstance);
        }
        catch (Exception e){
            return new MessageDTO(false, e.getMessage());
        }

        return new MessageDTO(true, "Env variable " + envVariableName + " set to " + value + " successfully !\n");
    }


    @Override
    public MessageDTO run() {
        setWorldInstance();
        runSimulation();
            //do{
        // method 1:
            //  for rule in rules:
            //    if(onRun(rule)): -> נבדוק אם הגיע הטיק או ההסתברות שלו
            //      rulesOnRun.add(rule)
        // method 2:
            //  for rule in rulesOnRun:
            //    for action in rule.actions:
            //      actionsOnRun.add(action)
        // method 3:
            //  for action in actionsOnRun:
            //    for entity in world.Entity:
            //      if (isGoodEntity(entity)) :action.invoke(person)
            //  this.increaseTick() ->countTick++//לכל רול בר הפעלה נאסוף את סך האקשנים ולכל אקשן נעבור על כל המופעים ונבצע אינבוק לכל אקשן על האינסטנס הספציפי עליו נמצאים
            //}while(!this.isFinish)-> כל עוד אף אחד מתנאי הסיום לא הגיע-כלומר לא נגמר סך הטיקים שהוגדרו
        return null;
    }

    @Override
    public void stop() {

    }
    public void runSimulation() {
        resetTick();
        do {
            // Method 1: Process rules
            for (Rule rule : rules) {
                if (isOnRun(rule)) {
                    rulesOnRun.add(rule);
                }
            }

            // Method 2: Process actions within rules
            for (Rule rule : rulesOnRun) {
                actionsOnRun.addAll(rule.getActionsToPerform());
            }

            // Method 3: Invoke actions on entities
            for (Action action : actionsOnRun) {
                for (EntityInstance entity : entityInstanceManager.getInstances()) {
                    if (isGoodEntity(entity)) {
                        action.invoke((Context) entity);
                    }
                }
            }

            increaseTick();
        } while (!isFinish());
    }


    private boolean isOnRun(Rule rule) {
        // Implementation onRun method logic
        //TODO
        return true;
    }

    private boolean isGoodEntity(EntityInstance entity) {
        // Implementation isGoodEntity method logic
        //TODO
        return false;
    }

    private void resetTick() {
        this.countTick = 0;
    }

    private void increaseTick() {
        this.countTick++;
    }

    private boolean isFinish() {
        //TODO
        return this.countTick < 1000;
    }

}
