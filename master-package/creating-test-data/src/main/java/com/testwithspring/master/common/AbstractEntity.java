package com.testwithspring.master.common;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * This is a base class that should be extended by all entity
 * classes. This class declares common fields that are useful
 * for most entities. These fields are: id, creation time,
 * modification time, and version.
 */
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    private ZonedDateTime creationTime;

    @Column(name = "modification_time")
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
