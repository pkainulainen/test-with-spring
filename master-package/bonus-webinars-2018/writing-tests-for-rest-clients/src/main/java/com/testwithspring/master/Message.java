package com.testwithspring.master;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Contains the information of a single message.
 */
public class Message {

    private Long id;
    private String text;

    public Message() {}

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("text", this.text)
                .toString();
    }
}
