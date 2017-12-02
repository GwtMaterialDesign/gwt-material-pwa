package com.github.gwtmaterialdesign.shared;

import java.io.Serializable;

public class Message implements Serializable {

    String message;

    public Message(String message) {
        this.message = message;
    }

    public Message() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
