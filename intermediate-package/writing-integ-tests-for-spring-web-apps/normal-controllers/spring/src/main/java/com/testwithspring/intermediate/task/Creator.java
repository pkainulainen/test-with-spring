package com.testwithspring.intermediate.task;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
class Creator {

    @Column(name = "creator_id", nullable = false)
    private Long userId;

    /**
     * Required by Hibernate.
     */
    private Creator() {}

    Creator(Long userId) {
        this.userId = userId;
    }

    Long getUserId() {
        return userId;
    }
}
