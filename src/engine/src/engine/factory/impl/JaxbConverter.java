package engine.factory.impl;

import engine.action.api.Action;
import engine.action.impl.*;
import engine.action.impl.calculation.CalculationAction;
import engine.action.impl.calculation.Operation;
import engine.action.impl.condition.api.ConditionAction;
import engine.action.impl.condition.impl.*;
import engine.definition.grid.GridDefinition;
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
                    if (prdProperty.getPRDRange() != null) {
                        int from = (int) prdProperty.getPRDRange().getFrom();
                        int to = (int) prdProperty.getPRDRange().getTo();
                        return new IntegerPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createRandomInteger(from, to));
                    }
                    else{
                        return new IntegerPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createRandomInteger(0, 1000));
                    }

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
                    if (prdProperty.getPRDRange() != null) {
                        float from = (float) prdProperty.getPRDRange().getFrom();
                        float to = (float) prdProperty.getPRDRange().getTo();
                        return new FloatPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createRandomFloat(from, to));
                    }
                    else{
                        return new FloatPropertyDefinition(prdProperty.getPRDName(), ValueGeneratorFactory.createRandomFloat(0f, 1000f));
                    }
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
        EntityDefinition entityDefinition = new EntityDefinitionImpl(prdEntity.getName(), 0);
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


    public static Rule convertRule(PRDRule prdRule, HashMap<String, EntityDefinition> entityNameToEntityDefinition) {
        Rule rule = new RuleImpl(prdRule.getName());
        ActivationImpl activation = convertActivation(prdRule.getPRDActivation());
        rule.setActivation(activation);
        for (PRDAction prdAction : prdRule.getPRDActions().getPRDAction()) {
            validateRuleActions(entityNameToEntityDefinition, prdRule);
            Action action = convertAction(prdAction, entityNameToEntityDefinition);
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

    private static Action convertAction(PRDAction prdAction, HashMap<String, EntityDefinition> entityNameToEntityDefinition) {
        String actionType = prdAction.getType();
        String entityName;
        boolean isCondition = actionType.equals("condition");
        if (actionType.equals("proximity")){
            entityName = prdAction.getPRDBetween().getSourceEntity();
        }
        else if (actionType.equals("replace")) {
            entityName = prdAction.getKill();
        }
        else{
            entityName = prdAction.getEntity();
        }
        EntityDefinition primaryEntityDefinition = entityNameToEntityDefinition.get(entityName);
        if (primaryEntityDefinition == null && !isCondition){
            throw new IllegalArgumentException(" rule " + prdAction.getType() + " - no entity named " + entityName + " in the system.");
        }
        EntityDefinition secondaryEntity;
        SecondaryEntityDefinition secondaryEntityDefinition = null;

        if (prdAction.getPRDSecondaryEntity() != null) {
            secondaryEntity = entityNameToEntityDefinition.get(prdAction.getPRDSecondaryEntity().getEntity());
            if (secondaryEntity == null) {
                throw new IllegalArgumentException(" rule " + prdAction.getType() + " - no entity named " + prdAction.getPRDSecondaryEntity().getEntity() + " in the system.");
            }
            String count = prdAction.getPRDSecondaryEntity().getPRDSelection().getCount();
            if ( prdAction.getPRDSecondaryEntity().getPRDSelection().getPRDCondition() != null) {
                Condition condition = getCondition(prdAction.getPRDSecondaryEntity().getPRDSelection().getPRDCondition());
                secondaryEntityDefinition = new SecondaryEntityDefinition(secondaryEntity, count, condition);
            }
            else{
                secondaryEntityDefinition  = new SecondaryEntityDefinition(secondaryEntity, count, null);
            }
        }
        switch (actionType) {
            case "increase":
                return new IncreaseAction(primaryEntityDefinition, secondaryEntityDefinition, prdAction.getProperty(), prdAction.getBy());
            case "decrease":
                return new DecreaseAction(primaryEntityDefinition, secondaryEntityDefinition, prdAction.getProperty(), prdAction.getBy());
            case "calculation":
                Operation operation = convertOperation(prdAction);
                return new CalculationAction(primaryEntityDefinition, secondaryEntityDefinition, prdAction.getResultProp(), operation);
            case "condition":
                return convertConditionAction(prdAction, primaryEntityDefinition, secondaryEntityDefinition, entityNameToEntityDefinition);
            case "set":
                String propName = prdAction.getProperty();
                String valueExpression = prdAction.getValue();
                return new SetAction(primaryEntityDefinition, secondaryEntityDefinition, propName, valueExpression);
            case "kill":
                if (entityNameToEntityDefinition.get(prdAction.getEntity()) == null) {
                    throw new IllegalArgumentException(" rule kill - no entity name " +prdAction.getEntity());
                }
                return new KillAction(primaryEntityDefinition, secondaryEntityDefinition);
            case "replace":
                return new ReplaceAction(entityNameToEntityDefinition.get(prdAction.getKill()), secondaryEntityDefinition, prdAction.getKill(), prdAction.getCreate(), prdAction.getMode());
            case "proximity":
                EntityDefinition targetEntityDefinition = entityNameToEntityDefinition.get(prdAction.getPRDBetween().getTargetEntity());
                secondaryEntityDefinition = new SecondaryEntityDefinition(targetEntityDefinition, "all", null);
                ArrayList <Action> thanActions = getThenActions(prdAction, entityNameToEntityDefinition);
                return new ProximityAction(entityNameToEntityDefinition.get(prdAction.getPRDBetween().getSourceEntity()), secondaryEntityDefinition, prdAction.getPRDBetween().getTargetEntity(), prdAction.getPRDEnvDepth().getOf(), thanActions);
            default:
                throw new java.lang.IllegalArgumentException("Unexpected action type value: " + actionType);
        }

    }


    private static ConditionAction convertConditionAction(PRDAction prdAction, EntityDefinition entityDefinition, SecondaryEntityDefinition secondaryEntityDefinition, HashMap<String, EntityDefinition> entityNameToEntityDefinition) {
        ArrayList<Action> thanActions = getThenActions(prdAction, entityNameToEntityDefinition);
        ArrayList<Action> elseActions = getElseActions(prdAction, entityNameToEntityDefinition);
        if (Objects.equals(prdAction.getPRDCondition().getSingularity(), "single")) {
            return getSingleConditionAction(entityDefinition, secondaryEntityDefinition, prdAction.getPRDCondition(), thanActions, elseActions);
        } else if (Objects.equals(prdAction.getPRDCondition().getSingularity(), "multiple")) {
            return getMultipleConditionAction(entityDefinition, secondaryEntityDefinition, prdAction.getPRDCondition(), thanActions, elseActions);
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


    private static SingleConditionAction getSingleConditionAction(EntityDefinition entityDefinition,  SecondaryEntityDefinition secondaryEntityDefinition, PRDCondition prdCondition, ArrayList<Action> thenActions, ArrayList<Action> elseActions) {
        SingleCondition singleCondition = (SingleCondition) getCondition(prdCondition);
        return new SingleConditionAction(entityDefinition,secondaryEntityDefinition, singleCondition, thenActions, elseActions);
    }

    private static MultipleConditionAction getMultipleConditionAction(EntityDefinition entityDefinition, SecondaryEntityDefinition secondaryEntityDefinition, PRDCondition prdCondition, ArrayList<Action> thenActions, ArrayList<Action> elseActions) {
        // validate logical
        String logical = prdCondition.getLogical();
        if (logical == null || logical.isEmpty()) {
            throw new IllegalArgumentException("Multiple condition error - Logical operator cannot be null or empty");
        }
        MultipleCondition multipleCondition = (MultipleCondition) getCondition(prdCondition);
        return new MultipleConditionAction(entityDefinition, secondaryEntityDefinition, multipleCondition, thenActions, elseActions);
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

    private static ArrayList<Action> getElseActions(PRDAction prdAction, HashMap<String, EntityDefinition> entityNameToEntityDefinition) {
        ArrayList<Action> elseActions = new ArrayList<>();
        if (prdAction.getPRDElse() != null) {
            for (PRDAction action : prdAction.getPRDElse().getPRDAction()) {
                elseActions.add(convertAction(action, entityNameToEntityDefinition));
            }
            return elseActions;
        }
        return elseActions;
    }


    private static ArrayList<Action> getThenActions(PRDAction prdAction, HashMap<String, EntityDefinition> entityNameToEntityDefinition) {
        ArrayList<Action> thenActions = new ArrayList<>();
        if (prdAction.getType().equals("proximity")) {
            for (PRDAction action : prdAction.getPRDActions().getPRDAction()) {
                thenActions.add(convertAction(action, entityNameToEntityDefinition));
            }
            return thenActions;
        }
        for (PRDAction action : prdAction.getPRDThen().getPRDAction()) {
            thenActions.add(convertAction(action, entityNameToEntityDefinition));
        }
        return thenActions;
    }

    public static GridDefinition convertGrid(PRDWorld.PRDGrid prdGrid) {
        if (prdGrid == null) {
            return null;
        }
        int rows = prdGrid.getRows();
        int cols = prdGrid.getColumns();
        if (rows < 10 || cols < 10 || rows > 100 || cols > 100) {
            throw new IllegalArgumentException("Grid rows and columns must be between 10 to 100.");
        }
        return new GridDefinition(rows, cols);
    }

    public static Termination convertTermination(PRDTermination prdTermination) {
        int ticks = -1;
        int seconds = -1;
        //TODO : VALIDATE IF BOTH OF THE CONDITIONS MUST APPEAR
        if (prdTermination == null) {
            throw new IllegalArgumentException("Termination is null");
        }
        else if (prdTermination.getPRDByUser() != null){
            return new Termination(true);
        }
        List<Object> terminationConditions = prdTermination.getPRDBySecondOrPRDByTicks();
        for (Object terminationCondition : terminationConditions) {
            if (terminationCondition instanceof PRDByTicks) {
                ticks = ((PRDByTicks) terminationCondition).getCount();
            } else if (terminationCondition instanceof PRDBySecond) {
                seconds = ((PRDBySecond) terminationCondition).getCount();
            }
        }
        return new Termination(ticks, seconds);
    }

    public static void validateRuleActions(HashMap<String, EntityDefinition> entityNameToEntityDefinition, PRDRule prdRule) {
        for (PRDAction prdAction : prdRule.getPRDActions().getPRDAction()) {
            boolean primaryEntityFound = false;
            boolean targetEntityFound = false;
            boolean isProximity = prdAction.getType().equals("proximity");
            boolean isReplace = prdAction.getType().equals("replace");
            boolean isCondition = prdAction.getType().equals("condition");
            String primaryEntity;
            String targetEntity = "";

            if (isProximity) {
                primaryEntity = prdAction.getPRDBetween().getSourceEntity();
                targetEntity = prdAction.getPRDBetween().getTargetEntity();
            }
            else if (isReplace) {
                primaryEntity = prdAction.getKill();
            }
            else if (isCondition){
                return;
            } else {
                primaryEntity = prdAction.getEntity();
            }
            for (String entityName : entityNameToEntityDefinition.keySet()) {
                if (entityName.equals(primaryEntity)) {
                    primaryEntityFound = true;
                }
                if (isProximity){
                    String targetEntityName = prdAction.getPRDBetween().getTargetEntity();
                    if (entityName.equals(targetEntityName)){
                        targetEntityFound = true;
                    }
                }
                if (primaryEntityFound){
                    String propertyName = prdAction.getProperty();
                    if (propertyName != null){
                        boolean propertyFound = false;
                        for (PropertyDefinition propertyDefinition: entityNameToEntityDefinition.get(primaryEntity).getProps()){
                            if (propertyName.equals(propertyDefinition.getName())){
                                propertyFound = true;
                            }
                        }
                        if (!propertyFound){
                            throw new IllegalArgumentException("Rule " + prdRule.getName() + " -   un exist property '"+ propertyName + "'" + "for entity '" + primaryEntity);
                        }
                    }
                    break;
                }
            }
            if (!primaryEntityFound) {
                throw new IllegalArgumentException("Rule '" + prdRule.getName() + "' contain non exist entity '" + primaryEntity + "'");
            }
            if ((isProximity && !targetEntityFound)) {
                throw new IllegalArgumentException("Rule '" + prdRule.getName() + "' contain non exist entity '" + targetEntity + "'");
            }
        }
    }

}




