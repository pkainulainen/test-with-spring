package com.testwithspring.intermediate.task;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
class Assignee {

    @Column(name = "assignee_id")
    private Long userId;

    /**
     * Required by Hibernate
     */
    private Assignee() {}

    Assignee(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", this.userId)
                .build();
    }
}
