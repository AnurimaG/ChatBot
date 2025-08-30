package stanchat.model;

import java.time.LocalDateTime;

public class Memory {
    private String keyName;
    private String value;
    private LocalDateTime timestamp;

    public Memory(String keyName, String value, LocalDateTime timestamp) {
        this.keyName = keyName;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getKeyName() { return keyName; }
    public String getValue() { return value; }
    public LocalDateTime getTimestamp() { return timestamp; }
}