package engine.factory.impl;

import engine.definition.entity.EntityDefinition;
import engine.definition.entity.EntityDefinitionImpl;
import engine.definition.property.api.PropertyDefinition;
import engine.definition.property.impl.BooleanPropertyDefinition;
import engine.definition.property.impl.FloatPropertyDefinition;
import engine.definition.property.impl.IntegerPropertyDefinition;
import engine.definition.property.impl.StringPropertyDefinition;
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
            // Decimal and float has optinal range value.
            case "decimal":
                if (prdProperty.getPRDValue().isRandomInitialize()){
                    int from = (int) prdProperty.getPRDRange().getFrom();
                    int to = (int) prdProperty.getPRDRange().getTo();
                    return new IntegerPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createRandomInteger(from, to));

                }
                else { // we should set range
                    int value = Integer.parseInt(prdProperty.getPRDValue().getInit());
                    return new IntegerPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createFixed(value));
                }
            case "boolean":
                if (prdProperty.getPRDValue().isRandomInitialize()){
                    return new BooleanPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createRandomBoolean());
                }
                else{
                    boolean val = Boolean.parseBoolean(prdProperty.getPRDValue().getInit());
                    return new BooleanPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createFixed(val));
                }
            case "float":
                if (prdProperty.getPRDValue().isRandomInitialize()){
                    float from = (float) prdProperty.getPRDRange().getFrom();
                    float to = (float) prdProperty.getPRDRange().getTo();
                    return new FloatPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createRandomFloat(from, to));

                }
                else { // we should set range
                    float value = Float.parseFloat(prdProperty.getPRDValue().getInit());
                    return new FloatPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createFixed(value));
                }
            case "string":
                if (prdProperty.getPRDValue().isRandomInitialize()){
                    return new StringPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createRandomString());
                }
                else{
                    String value = prdProperty.getPRDValue().getInit();
                    return new StringPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createFixed(value));                }
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
