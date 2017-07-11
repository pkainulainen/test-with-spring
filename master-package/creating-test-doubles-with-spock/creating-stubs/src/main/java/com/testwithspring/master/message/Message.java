package com.testwithspring.master.message;

/**
 * Contains the information of a single message.
 */
public class Message {

    private Long id;
    private String message;

    public Message() {}

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
