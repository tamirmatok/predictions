package dto.api;

import java.util.HashMap;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Map;

public abstract class AbstractDTO<T> {
    private final String timestamp;
    private Boolean success;

    public T data;

    public AbstractDTO(boolean success, T data) {
        this.success = success;
        this.timestamp = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // FOR this to happen make sure that each data given has to string method
    public String printDTO() {
        return "AbstractDTO{" +
                "timestamp='" + timestamp + '\'' +
                ", success=" + success +
                ", data=" + data +
                '}';
    }

}
