package com.testwithspring.intermediate.task;

import com.testwithspring.intermediate.ReflectionFieldUtil;

public final class TagBuilder {

    private Long id = -1L;
    private String name = "NOT_IMPORTANT";

    public TagBuilder() {}

    public TagBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TagBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Tag build() {
        Tag tag = new Tag();

        ReflectionFieldUtil.setFieldValue(tag, "id", this.id);
        ReflectionFieldUtil.setFieldValue(tag, "name", this.name);

        return tag;
    }
}
