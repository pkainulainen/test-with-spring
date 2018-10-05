package com.testwithspring.master.webinar;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Contains the information of a single tasdk.
 */
public class Task {

    private Long id;
    private String title;

    public Task() {}

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("title", this.title)
                .toString();
    }
}
