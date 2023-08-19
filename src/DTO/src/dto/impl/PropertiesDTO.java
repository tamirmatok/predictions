package dto.impl;

import dto.api.AbstractDTO;
import engine.schema.generated.PRDProperty;

import java.util.HashMap;

public class PropertiesDTO extends AbstractDTO<HashMap<String, String>>{

    public PropertiesDTO(boolean success, HashMap<String, String> data) {
        super(success, data);
    }

}
