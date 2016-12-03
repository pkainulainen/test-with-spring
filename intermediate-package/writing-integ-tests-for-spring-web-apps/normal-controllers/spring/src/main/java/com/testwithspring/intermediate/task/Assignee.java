package com.testwithspring.intermediate.task;

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
}
