package com.testwithspring.intermediate.task;

import com.testwithspring.intermediate.common.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tags")
public class Tag extends AbstractEntity {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    public String getName() {
        return name;
    }
}
