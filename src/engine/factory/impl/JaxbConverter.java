package engine.factory.impl;

import engine.action.api.Action;
import engine.action.impl.DecreaseAction;
import engine.action.impl.IncreaseAction;
import engine.action.impl.KillAction;
import engine.definition.entity.EntityDefinition;
import engine.definition.entity.EntityDefinitionImpl;
import engine.definition.property.api.PropertyDefinition;
import engine.definition.property.api.PropertyType;
import engine.definition.property.impl.BooleanPropertyDefinition;
import engine.definition.property.impl.FloatPropertyDefinition;
import engine.definition.property.impl.IntegerPropertyDefinition;
import engine.definition.property.impl.StringPropertyDefinition;
import engine.definition.value.generator.api.ValueGeneratorFactory;
import engine.rule.Activation;
import engine.rule.ActivationImpl;
import engine.rule.Rule;
import engine.rule.RuleImpl;
import engine.schema.generated.*;

import java.util.HashMap;

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

    public static PropertyDefinition convertEnvProperty(PRDEnvProperty prdEnvProperty){
        PRDProperty prdProperty = new PRDProperty();
        prdProperty.setPRDName(prdEnvProperty.getPRDName());
        prdProperty.setType(prdEnvProperty.getType());
        if (prdEnvProperty.getPRDRange() != null){
            PRDRange prdRange = new PRDRange();
            prdRange.setFrom(prdEnvProperty.getPRDRange().getFrom());
            prdRange.setTo(prdEnvProperty.getPRDRange().getTo());
            prdProperty.setPRDRange(prdRange);
        }
        //At the definition phase we will set the values to random
        PRDValue prdValue = new PRDValue();
        prdValue.setRandomInitialize(true);
        prdProperty.setPRDValue(prdValue);
        return convertProperty(prdProperty);
    }
    public static EntityDefinition convertEntity(PRDEntity prdEntity){
        EntityDefinition entityDefinition = new EntityDefinitionImpl(prdEntity.getName(), prdEntity.getPRDPopulation());
        for (PRDProperty prdProperty : prdEntity.getPRDProperties().getPRDProperty()) {
            entityDefinition.getProps().add(convertProperty(prdProperty));
        }
        return entityDefinition;
    }


    public static Rule convertRule(PRDRule prdRule, HashMap<String,EntityDefinition> entityDefinition) {
        Rule rule = new RuleImpl(prdRule.getName());
        rule.setActivation(new ActivationImpl());
        for (PRDAction prdAction : prdRule.getPRDActions().getPRDAction()) {
            if (entityDefinition.get(prdAction.getEntity()) == null){
                throw new IllegalArgumentException(" rule " + prdRule.getName() + " - no entity named " + prdAction.getEntity());
            }
            Action action = convertAction(prdAction, entityDefinition.get(prdAction.getEntity()));
            rule.addAction(action);
        }
        return rule;
    }

    private static Action convertAction(PRDAction prdAction, EntityDefinition entityDefinition) {
        String actionType = prdAction.getType();
        switch (actionType) {
            case "increase":
                return new IncreaseAction(entityDefinition, prdAction.getProperty(), prdAction.getBy());
            case "decrease":
                return new DecreaseAction(entityDefinition, prdAction.getProperty(), prdAction.getBy());
//            case "calculation":
//                return new CalculationAction(entityDefinition);
//            case "condition":
//                return new ConditionAction(entityDefinition);
//            case "set":
//                return new SetAction(entityDefinition);
            case "kill":
                return new KillAction(entityDefinition);
//            case "replace":
//                return new ReplaceAction(entityDefinition, prdAction.getProperty(), prdAction.getBy());
//            case "proximity":
//                return new ProximityAction(entityDefinition);
//            default:
//                throw new java.lang.IllegalArgumentException("Unexpected action type value: " + actionType);
        }
        return null;
    }
}
