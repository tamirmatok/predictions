package dto.impl;

import dto.api.AbstractDTO;
import engine.schema.generated.PRDEvironment;

public class PRDEnvDTO extends AbstractDTO<PRDEvironment> {
    public PRDEnvDTO(boolean success, PRDEvironment data) {
        super(success, data);
    }

    public PRDEvironment getPRDEnv() {
        return data;
    }
}
