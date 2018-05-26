package com.testwithspring.master;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;

public class MessageDTO {

    @NotBlank
    private String text;

    MessageDTO() {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("text", this.text)
                .build();
    }
}
