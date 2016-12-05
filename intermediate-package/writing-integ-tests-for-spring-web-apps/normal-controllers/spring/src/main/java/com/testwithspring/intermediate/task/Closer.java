package com.testwithspring.intermediate.task;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
class Closer {

    @Column(name = "closer_id")
    private Long userId;

    /**
     * Required by Hibernate.
     */
    private Closer() {}

    Closer(Long userId) {
        this.userId = userId;
    }

    Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", this.userId)
                .build();
    }
}
