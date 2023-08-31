package dto.impl;

import dto.api.AbstractDTO;
import engine.schema.generated.PRDEnvironment;

public class PRDEnvDTO extends AbstractDTO<PRDEnvironment> {
    public PRDEnvDTO(boolean success, PRDEnvironment data) {
        super(success, data);
    }

    public PRDEnvironment getPRDEnv() {
        return data;
    }
}
