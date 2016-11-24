package com.testwithspring.intermediate.message;

public class Message {

    private Long id;
    private String messageText;

    public Message() {}

    public Long getId() {
        return id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", messageText='" + messageText + '\'' +
                '}';
    }
}

