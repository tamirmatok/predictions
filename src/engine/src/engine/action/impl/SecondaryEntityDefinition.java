package engine.action.impl;

import engine.action.impl.condition.impl.Condition;
import engine.definition.entity.EntityDefinition;

public class SecondaryEntityDefinition {
    private final EntityDefinition entityDefinition;
    private final String count;
    private final Condition condition;

    public SecondaryEntityDefinition(EntityDefinition entityDefinition, String count, Condition condition) {
        this.entityDefinition = entityDefinition;
        this.count = count;
        this.condition = condition;
        this.validateCount();
    }

    private void validateCount() {
        if (!count.equals("ALL") && !count.equals("all")){
            try{
                Integer.parseInt(count);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Count must be either 'ALL' or an integer");
            }
        }
    }

    public boolean isAll() {
        return count.equals("ALL") || count.equals("all");
    }

    public EntityDefinition getEntityDefinition() {
        return entityDefinition;
    }

    public String getCount() {
        return count;
    }

    public Condition getCondition() {
        return condition;
    }

    public boolean hasCondition() {
        return condition != null;
    }
}
