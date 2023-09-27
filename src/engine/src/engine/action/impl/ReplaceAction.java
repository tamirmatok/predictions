package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.definition.entity.EntityDefinition;
import engine.definition.property.api.PropertyDefinition;
import engine.definition.property.api.PropertyType;
import engine.execution.context.Context;
import engine.execution.context.ContextImpl;
import engine.execution.instance.enitty.EntityInstance;


public class ReplaceAction extends AbstractAction {

    private final String kill;
    private final String create;
    private final String mode;

    public ReplaceAction(EntityDefinition mainEntityDefinition, SecondaryEntityDefinition secondaryEntityDefinition, String kill, String create, String mode) {
        super(ActionType.REPLACE, mainEntityDefinition, secondaryEntityDefinition);
        this.kill = kill;
        this.create = create;
        this.mode = mode;
    }

    @Override
    public void invoke(Context context) {
        // kill the entity instance
        EntityInstance entityInstanceToKill = context.getEntityInstanceManager().getInstanceByName(kill);
        if (entityInstanceToKill != null) {
            context.getEntityInstanceManager().killEntity(entityInstanceToKill.getId());

            // get the entity definition to create and create the instance
            EntityDefinition entityDefinition = context.getEntityInstanceManager().getEntityDefinitionByName(create);
            EntityInstance newEntityInstance = context.getEntityInstanceManager().create(entityDefinition);


            if (mode.equals("derived")) {
                for (PropertyDefinition propertyDefinition : entityDefinition.getProps()) {
                    if (entityInstanceToKill.getProperties().containsKey(propertyDefinition.getName())) { // has property with the same name
                        PropertyType killedPropertyType = entityInstanceToKill.getProperties().get(propertyDefinition.getName()).getPropertyDefinition().getType();
                        if (propertyDefinition.getType() == killedPropertyType) { // has property with the same type
                            Object newValue = entityInstanceToKill.getProperties().get(propertyDefinition.getName()).getValue();
                            newEntityInstance.getProperties().get(propertyDefinition.getName()).updateValue(newValue, context.getCurrentTick());
                        }
                    }

                }
            }
        }
        invokeOnSecondary(context);
    }
}
