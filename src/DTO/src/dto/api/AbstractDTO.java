package dto.api;

import java.util.HashMap;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Map;

public abstract class AbstractDTO<T> {
    private final String timestamp;
    private final Boolean success;

    private final String errorMessage;

    public T data;

    public AbstractDTO(boolean success, T data) {
        this.success = success;
        this.timestamp = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
        this.data = data;
        this.errorMessage = null;
    }

    public AbstractDTO(String errorMessage){
        success = false;
        this.timestamp = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
        this.errorMessage = errorMessage;

    }

    public boolean isSuccess() {
        return success;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public T getData(){
        return data;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

}
