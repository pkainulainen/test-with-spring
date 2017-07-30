package com.testwithspring.master.task

import com.testwithspring.master.ReflectionFieldSetter

final class TagBuilder {

    private id = -1L
    private name = 'NOT_IMPORTANT'

    TagBuilder() {}

    def withId(id) {
        this.id = id
        return this
    }

    def TagBuilder withName(name) {
        this.name = name
        return this
    }

    def build() {
        def tag = new Tag()

        ReflectionFieldSetter.setFieldValue(tag, 'id', id)
        tag.@name = name

        return tag
    }
}
