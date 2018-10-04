package com.testwithspring.master.webinar;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Contains the information of a single tasdk.
 */
public class Task {

    private final Long id;
    private final String title;

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("title", this.title)
                .toString();
    }
}
