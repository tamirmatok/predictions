package engine.execution.instance.world.impl;

import engine.definition.entity.EntityDefinition;
import engine.execution.instance.enitty.manager.EntityInstanceManager;
import engine.execution.instance.world.api.WorldInstanceInterface;
import engine.factory.impl.JaxbConverter;
import engine.schema.generated.*;
import engine.definition.world.api.WorldDefinition;


public class WorldInstance implements WorldInstanceInterface {

    private final WorldDefinition worldDefinition;

    public WorldInstance(WorldDefinition worldDefinition) {
        this.worldDefinition = worldDefinition;
    }

    @Override
    public void run() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setWorld(PRDWorld prdWorld) {
        setEntities(prdWorld.getPRDEntities());
        System.out.println("Stop");
//        setEnvironment(prdWorld.getPRDEvironment());
//        setRules(prdWorld.getPRDRules());
//        setTermination(prdWorld.getPRDTermination());
    }

    public void setEntities(PRDEntities prdEntities) {
        EntityInstanceManager entityInstanceManager = this.worldDefinition.getEntityInstanceManager();
        for (PRDEntity prdEntity : prdEntities.getPRDEntity()) {
            EntityDefinition entityDefinition = JaxbConverter.convertEntity(prdEntity);
            entityInstanceManager.create(entityDefinition);
        }
    }

    public void setEnvironment(PRDEvironment prdEnvironment){

    }

    public void setRules(PRDRules prdRules) {

    }

    public void setTermination(PRDTermination prdTermination) {

    }
}
