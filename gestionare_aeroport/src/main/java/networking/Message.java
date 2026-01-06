package networking;

import java.io.Serializable;

public class Message implements Serializable {
    // This ID is important for serialization to work across different JVMs
    private static final long serialVersionUID = 1L;

    private String type;   // The command (e.g., "LOGIN", "GET_ALL_FLIGHTS", "ADD_USER")
    private Object object; // The data payload (e.g., a User object, a String, or null)

    public Message(String type, Object object) {
        this.type = type;
        this.object = object;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "Message{" + "type='" + type + '\'' + ", object=" + object + '}';
    }
}