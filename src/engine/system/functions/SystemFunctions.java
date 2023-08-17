package engine.system.functions;

import engine.definition.environment.api.EnvVariablesManager;
import engine.definition.property.api.PropertyType;
import engine.execution.instance.environment.api.ActiveEnvironment;
import engine.execution.instance.property.PropertyInstance;

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

    public Object environment(PropertyInstance propertyInstance){
        return propertyInstance.getValue();
    }
    public PropertyType random(Integer to){
        return PropertyType.DECIMAL.convert(random.nextInt(to));
    }
}
