package server;

import java.io.Serializable;

public class ServerResponse implements Serializable {
    private String messageType;
    private Serializable data;

    public ServerResponse(String messageType, Serializable data) {
        this.messageType = messageType;
        this.data = data;
    }

    public String getMessageType() {
        return messageType;
    }

    public Serializable getData() {
        return data;
    }
}
