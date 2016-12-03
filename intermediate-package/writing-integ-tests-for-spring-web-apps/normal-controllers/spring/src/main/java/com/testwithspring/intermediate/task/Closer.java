package com.testwithspring.intermediate.task;

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
}
