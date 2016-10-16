package com.crookk.pikaplus.local.event;

public class ConsoleLogEvent {

    private String message;

    public ConsoleLogEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
