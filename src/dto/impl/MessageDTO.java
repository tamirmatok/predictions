package dto.impl;

import dto.api.AbstractDTO;

public class MessageDTO extends AbstractDTO<String> {



    public MessageDTO(boolean success, String data) {
        super(success, data);
    }

    public String getMessage() {
        return data;
    }

}