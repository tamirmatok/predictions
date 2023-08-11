package engine.action.impl.expression;

import engine.definition.entity.EntityDefinition;
import engine.definition.property.api.PropertyType;

public interface ExpressionInterface {
      PropertyType calculate(String expression, EntityDefinition entityDefinition);
}
