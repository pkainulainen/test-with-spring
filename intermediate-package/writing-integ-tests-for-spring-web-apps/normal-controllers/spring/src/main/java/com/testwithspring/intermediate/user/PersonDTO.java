package com.testwithspring.intermediate.user;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PersonDTO {

    private String name;
    private Long userId;

    public PersonDTO() {}

    public String getName() {
        return name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", this.userId)
                .append("name", this.name)
                .toString();
    }
}
