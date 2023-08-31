package engine.factory.impl;

import engine.action.api.Action;
import engine.action.impl.*;
import engine.action.impl.calculation.CalculationAction;
import engine.action.impl.calculation.Operation;
import engine.action.impl.condition.api.ConditionAction;
import engine.action.impl.condition.impl.*;
import engine.definition.entity.EntityDefinition;
import engine.definition.entity.EntityDefinitionImpl;
import engine.definition.property.api.PropertyDefinition;
import engine.definition.property.impl.BooleanPropertyDefinition;
import engine.definition.property.impl.FloatPropertyDefinition;
import engine.definition.property.impl.IntegerPropertyDefinition;
import engine.definition.property.impl.StringPropertyDefinition;
import engine.definition.value.generator.api.ValueGeneratorFactory;
import engine.execution.instance.termination.impl.Termination;
import engine.rule.ActivationImpl;
import engine.rule.Rule;
import engine.rule.RuleImpl;
import engine.schema.generated.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class JaxbConverter {
    public JaxbConverter() {
        super();
    }

    public static PropertyDefinition convertProperty(PRDProperty prdProperty) {
        String propertyType = prdProperty.getType();
        switch (propertyType) {
            // Decimal and float has optinal range value.
            case "decimal":
                if (prdProperty.getPRDValue().isRandomInitialize()) {
                    int from = (int) prdProperty.getPRDRange().getFrom();
                    int to = (int) prdProperty.getPRDRange().getTo();
                    return new IntegerPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createRandomInteger(from, to));

                } else { // we should set range
                    int value = Integer.parseInt(prdProperty.getPRDValue().getInit());
                    if (prdProperty.getPRDRange() != null) {
                        int from = (int) prdProperty.getPRDRange().getFrom();
                        int to = (int) prdProperty.getPRDRange().getTo();
                        return new IntegerPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createFixed(value, from, to));
                    }
                    return new IntegerPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createFixed(value));
                }
            case "boolean":
                if (prdProperty.getPRDValue().isRandomInitialize()) {
                    return new BooleanPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createRandomBoolean());
                } else {
                    boolean val = Boolean.parseBoolean(prdProperty.getPRDValue().getInit());
                    return new BooleanPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createFixed(val));
                }
            case "float":
                if (prdProperty.getPRDValue().isRandomInitialize()) {
                    float from = (float) prdProperty.getPRDRange().getFrom();
                    float to = (float) prdProperty.getPRDRange().getTo();
                    return new FloatPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createRandomFloat(from, to));

                } else { // we should set range
                    float value = Float.parseFloat(prdProperty.getPRDValue().getInit());
                    return new FloatPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createFixed(value));
                }
            case "string":
                if (prdProperty.getPRDValue().isRandomInitialize()) {
                    return new StringPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createRandomString());
                } else {
                    String value = prdProperty.getPRDValue().getInit();
                    return new StringPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createFixed(value));
                }
            default:
                throw new java.lang.IllegalArgumentException("Unexpected argument value: " + propertyType);
        }
    }

    public static PropertyDefinition convertEnvProperty(PRDEnvProperty prdEnvProperty) {
        PRDProperty prdProperty = new PRDProperty();
        prdProperty.setPRDName(prdEnvProperty.getPRDName());
        prdProperty.setType(prdEnvProperty.getType());
        if (prdEnvProperty.getPRDRange() != null) {
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

    public static EntityDefinition convertEntity(PRDEntity prdEntity) {
        //TODO: take from the user the population and update the value.
        EntityDefinition entityDefinition = new EntityDefinitionImpl(prdEntity.getName(), 1);
        ArrayList<String> propertyNames = new ArrayList<>();
        for (PRDProperty prdProperty : prdEntity.getPRDProperties().getPRDProperty()) {
            if (propertyNames.contains(prdProperty.getPRDName())) {
                throw new IllegalArgumentException(" entity " + prdEntity.getName() + " has duplicate property named " + prdProperty.getPRDName());
            }
            propertyNames.add(prdProperty.getPRDName());
            entityDefinition.getProps().add(convertProperty(prdProperty));
        }
        return entityDefinition;
    }


    public static Rule convertRule(PRDRule prdRule, HashMap<String, EntityDefinition> entityDefinition) {
        Rule rule = new RuleImpl(prdRule.getName());
        ActivationImpl activation = convertActivation(prdRule.getPRDActivation());
        rule.setActivation(activation);
        for (PRDAction prdAction : prdRule.getPRDActions().getPRDAction()) {
            if (entityDefinition.get(prdAction.getEntity()) == null) {
                throw new IllegalArgumentException(" rule " + prdRule.getName() + " - no entity named " + prdAction.getEntity());
            }
            Action action = convertAction(prdAction, entityDefinition.get(prdAction.getEntity()));
            rule.addAction(action);
        }
        return rule;
    }


    private static ActivationImpl convertActivation(PRDActivation prdActivation) {
        ActivationImpl activation = new ActivationImpl();
        if (prdActivation != null) {
            if (prdActivation.getProbability() != null) {
                activation.setProbability(prdActivation.getProbability().floatValue());
            }
            if (prdActivation.getTicks() != null) {
                activation.setTicks(prdActivation.getTicks());
            }
        }
        return activation;
    }

    private static Action convertAction(PRDAction prdAction, EntityDefinition entityDefinition) {
        String actionType = prdAction.getType();
        switch (actionType) {
            case "increase":
                return new IncreaseAction(entityDefinition, prdAction.getProperty(), prdAction.getBy());
            case "decrease":
                return new DecreaseAction(entityDefinition, prdAction.getProperty(), prdAction.getBy());
            case "calculation":
                Operation operation = convertOperation(prdAction);
                return new CalculationAction(entityDefinition, prdAction.getResultProp(), operation);
            case "condition":
                return convertConditionAction(prdAction, entityDefinition);
            case "set":
                String entityName = prdAction.getEntity();
                String propName = prdAction.getProperty();
                String valueExpression = prdAction.getValue();
                return new SetAction(entityDefinition, propName, valueExpression);
            case "kill":
                return new KillAction(entityDefinition);
            //TODO: implement;
//            case "replace":
//                return new ReplaceAction(entityDefinition, prdAction.getProperty(), prdAction.getBy());
//            case "proximity":
//                return new ProximityAction(entityDefinition);
            default:
                throw new java.lang.IllegalArgumentException("Unexpected action type value: " + actionType);
        }

    }


    private static ConditionAction convertConditionAction(PRDAction prdAction, EntityDefinition entityDefinition) {
        ArrayList<Action> thanActions = getThenActions(entityDefinition, prdAction);
        ArrayList<Action> elseActions = getElseActions(entityDefinition, prdAction);
        if (Objects.equals(prdAction.getPRDCondition().getSingularity(), "single")) {
            return getSingleConditionAction(entityDefinition, prdAction.getPRDCondition(), thanActions, elseActions);
        } else if (Objects.equals(prdAction.getPRDCondition().getSingularity(), "multiple")) {
            return getMultipleConditionAction(entityDefinition, prdAction.getPRDCondition(), thanActions, elseActions);
        } else {
            throw new IllegalArgumentException(" rule " + prdAction.getEntity() + " - no valid singularity for condition action");
        }

    }

    private static Operation convertOperation(PRDAction prdAction) {
        Operation operation = new Operation();
        if (prdAction.getPRDMultiply() != null) {
            operation.setOperation("multiply");
            operation.setArg1Experssion(prdAction.getPRDMultiply().getArg1());
            operation.setArg2Experssion(prdAction.getPRDMultiply().getArg2());
        } else if (prdAction.getPRDDivide() != null) {
            operation.setOperation("divide");
            operation.setArg1Experssion(prdAction.getPRDDivide().getArg1());
            operation.setArg2Experssion(prdAction.getPRDDivide().getArg2());
        } else {
            throw new IllegalArgumentException(" rule " + prdAction.getEntity() + " - no operation for calculation action");
        }
        return operation;
    }


    private static SingleConditionAction getSingleConditionAction(EntityDefinition entityDefinition, PRDCondition prdCondition, ArrayList<Action> thenActions, ArrayList<Action> elseActions) {
        SingleCondition singleCondition = (SingleCondition) getCondition(prdCondition);
        return new SingleConditionAction(entityDefinition, singleCondition, thenActions, elseActions);
    }

    private static MultipleConditionAction getMultipleConditionAction(EntityDefinition entityDefinition, PRDCondition prdCondition, ArrayList<Action> thenActions, ArrayList<Action> elseActions) {
        // validate logical
        String logical = prdCondition.getLogical();
        if (logical == null || logical.isEmpty()) {
            throw new IllegalArgumentException("Multiple condition error - Logical operator cannot be null or empty");
        }
        MultipleCondition multipleCondition = (MultipleCondition) getCondition(prdCondition);
        return new MultipleConditionAction(entityDefinition, multipleCondition, thenActions, elseActions);
    }


    private static Condition getCondition(PRDCondition prdCondition) {
        if (prdCondition.getSingularity().equals("single")) {
            String entityName = prdCondition.getEntity();
            String propName = prdCondition.getProperty();
            String operator = prdCondition.getOperator();
            String valueExpression = prdCondition.getValue();
            return new SingleCondition(entityName, propName, operator, valueExpression);
        } else if (prdCondition.getSingularity().equals("multiple")) {
            ArrayList<Condition> innerConditions = new ArrayList<>();
            for (PRDCondition condition : prdCondition.getPRDCondition()) {
                if (condition.getSingularity().equals("single")) {
                    innerConditions.add(getCondition(condition));
                } else if (condition.getSingularity().equals("multiple")) {
                    innerConditions.add(getCondition(condition));
                } else {
                    throw new IllegalArgumentException(" Condition " + condition + " - no valid singularity for condition action");
                }
            }
            return new MultipleCondition(prdCondition.getLogical(), innerConditions);
        } else {
            throw new IllegalArgumentException(" Condition " + prdCondition + " - no valid singularity for condition action");
        }
    }

    private static ArrayList<Action> getElseActions(EntityDefinition entityDefinition, PRDAction prdAction) {
        ArrayList<Action> elseActions = new ArrayList<>();
        if (prdAction.getPRDElse() != null) {
            for (PRDAction action : prdAction.getPRDElse().getPRDAction()) {
                elseActions.add(convertAction(action, entityDefinition));
            }
            return elseActions;
        }
        return elseActions;
    }


    private static ArrayList<Action> getThenActions(EntityDefinition entityDefinition, PRDAction prdAction) {
        ArrayList<Action> thenActions = new ArrayList<>();
        for (PRDAction action : prdAction.getPRDThen().getPRDAction()) {
            thenActions.add(convertAction(action, entityDefinition));
        }
        return thenActions;
    }

//    public static Termination convertTermination(PRDTermination prdTermination) {
//        int ticks = 0;
//        int seconds = 0;
//        //TODO : VALIDATE IF BOTH OF THE CONDITIONS MUST APPEAR
//        if (prdTermination == null) {
//            throw new IllegalArgumentException("Termination is null");
//        }
//        List<Object> terminationConditions = prdTermination.getPRDBySecondOrPRDByTicks();
//        for (Object terminationCondition : terminationConditions) {
//            if (terminationCondition instanceof PRDByTicks) {
//                ticks = ((PRDByTicks) terminationCondition).getCount();
//            } else if (terminationCondition instanceof PRDBySecond) {
//                seconds = ((PRDBySecond) terminationCondition).getCount();
//            }
//        }
//        return new Termination(ticks, seconds);
//    }
}

