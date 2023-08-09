package engine.factory.impl;

import engine.definition.entity.EntityDefinition;
import engine.definition.entity.EntityDefinitionImpl;
import engine.definition.property.api.PropertyDefinition;
import engine.definition.property.impl.IntegerPropertyDefinition;
import engine.definition.value.generator.api.ValueGeneratorFactory;
import engine.schema.generated.PRDEntity;
import engine.schema.generated.PRDProperty;

public class JaxbConverter {
    public JaxbConverter() {
        super();
    }
    public static PropertyDefinition convertProperty(PRDProperty prdProperty) {
        String propertyType = prdProperty.getType();
        switch (propertyType) {
            case "decimal":
                int from = (int) prdProperty.getPRDRange().getFrom();
                int to = (int) prdProperty.getPRDRange().getTo();
                return new IntegerPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createRandomInteger(from, to));
//            case "float":
//                break;
//            case "boolean":
//                break;
//            case "string":
//                break;
            default:
                throw new java.lang.IllegalArgumentException("Unexpected argument value: " + propertyType);
        }
    }
    public static EntityDefinition convertEntity(PRDEntity prdEntity){
        EntityDefinition entityDefinition = new EntityDefinitionImpl(prdEntity.getName(), prdEntity.getPRDPopulation());
        for (PRDProperty prdProperty : prdEntity.getPRDProperties().getPRDProperty()) {
            entityDefinition.getProps().add(convertProperty(prdProperty));
        }
        return entityDefinition;
    }
}
