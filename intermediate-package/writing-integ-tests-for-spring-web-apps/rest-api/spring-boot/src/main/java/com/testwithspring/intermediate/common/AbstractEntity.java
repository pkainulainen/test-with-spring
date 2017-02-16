package com.testwithspring.intermediate.common;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * This is a base class that should be extended by all entity
 * classes. This class declares common fields that are useful
 * for most entities. These fields are: id, creation time,
 * modification time, and version.
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    @CreatedDate
    private ZonedDateTime creationTime;

    @Column(name = "modification_time")
    @LastModifiedDate
    private ZonedDateTime modificationTime;

    @Version
    private Long version;

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public Long getId() {
        return id;
    }

    public ZonedDateTime getModificationTime() {
        return modificationTime;
    }

    public Long getVersion() {
        return version;
    }
}
