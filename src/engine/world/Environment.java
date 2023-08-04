package engine.world;

import schema.generated.PRDEnvProperty;
import schema.generated.PRDEvironment;

import java.util.ArrayList;
import java.util.List;

public class Environment {

    ArrayList<EnvProperty> envProperties;
    public Environment(PRDEvironment prdEvironment) {
        List<PRDEnvProperty> prdEnvProperties = prdEvironment.getPRDEnvProperty();
        envProperties = new ArrayList<EnvProperty>(prdEnvProperties.size());
        for (PRDEnvProperty prdEnvProperty : prdEnvProperties) {
            EnvProperty envProperty = new EnvProperty();
            envProperty.setName(prdEnvProperty.getPRDName());
            envProperty.setRange(prdEnvProperty.getPRDRange());
            envProperty.setType(prdEnvProperty.getType());
            envProperties.add(envProperty);
        }

    }
}
