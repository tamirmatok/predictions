package engine.system.functions;

import engine.definition.environment.api.EnvVariablesManager;
import engine.definition.property.api.PropertyType;
import engine.execution.instance.environment.api.ActiveEnvironment;

import java.util.Random;

public class SystemFunctions {

    Random random;
    String[] functionNames = {"random", "environment"};
    public SystemFunctions(){
        random = new Random();
    }

    public String[] getSystemFunctionNames(){
        return functionNames;
    }

    private Object environment(ActiveEnvironment activeEnvironment, String envVarName){
        return activeEnvironment.getProperty(envVarName).getValue();
    }

//    private PropertyType random(int to){
//        return PropertyType.DECIMAL.convert(random.nextInt(to));
//    }
}
