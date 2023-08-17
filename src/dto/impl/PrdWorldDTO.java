package dto.impl;

import dto.api.AbstractDTO;
import engine.schema.generated.PRDWorld;

public class PrdWorldDTO extends AbstractDTO<PRDWorld> {

    public PrdWorldDTO(boolean success, PRDWorld data) {
        super(success, data);
    }

    public PRDWorld getPrdWorld() {
        return data;
    }

}
