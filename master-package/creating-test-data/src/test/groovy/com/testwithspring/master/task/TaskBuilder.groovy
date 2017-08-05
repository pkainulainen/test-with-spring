package com.testwithspring.master.task

import com.testwithspring.master.ReflectionFieldSetter

import java.time.ZonedDateTime

/**
 * This test data builder is used for creating new {@link Task} objects
 * that are used in our unit tests. The reason why we shouldn't use
 */
class TaskBuilder {

    private static final NOT_IN_USE = -1L
    private static final NOT_IMPORTANT = 'NOT_IMPORTANT'
    private static final NOT_SET = ZonedDateTime.now().minusYears(9999)

    private assigneeId = null
    private closerId = null
    private creationTime = NOT_SET
    private creatorId = NOT_IN_USE
    private description = NOT_IMPORTANT
    private id = NOT_IN_USE
    private modificationTime = NOT_SET
    private modifierId = NOT_IN_USE
    private resolution = null
    private status = null
    private tags = new HashSet<>()
    private title = NOT_IMPORTANT

    TaskBuilder() {
    }

    def withAssignee(assigneeId) {
        this.assigneeId = assigneeId
        return this
    }

    def withCreationTime(creationTime) {
        this.creationTime = creationTime
        return this
    }

    def withCreator(creatorId) {
        this.creatorId = creatorId
        return this
    }

    def withDescription(description) {
        this.description = description
        return this
    }

    def withId(id) {
        this.id = id
        return this
    }

    def withModificationTime(modificationTime) {
        this.modificationTime = modificationTime
        return this
    }

    def withModifier(modifierId) {
        this.modifierId = modifierId
        return this
    }

    def withResolutionDuplicate(closerId) {
        this.closerId = closerId
        this.resolution = TaskResolution.DUPLICATE
        this.status = TaskStatus.CLOSED
        return this
    }

    def withResolutionDone(closerId) {
        this.closerId = closerId
        this.resolution = TaskResolution.DONE
        this.status = TaskStatus.CLOSED
        return this
    }

    def withResolutionWontDo(closerId) {
        this.closerId = closerId
        this.resolution = TaskResolution.WONT_DO
        this.status = TaskStatus.CLOSED
        return this
    }

    def withStatusInProgress() {
        this.status = TaskStatus.IN_PROGRESS
        return this
    }

    def withStatusOpen() {
        this.status = TaskStatus.OPEN
        return this
    }

    def withTags(Tag... tags) {
        this.tags = new HashSet<>(Arrays.asList(tags));
        return this
    }

    def withTitle(title) {
        this.title = title
        return this
    }

    def build() {
        def task = Task.builder
                .withCreator(creatorId)
                .withDescription(description)
                .withTitle(title)
                .build()

        if (assigneeId != null) {
            task.@assignee = new Assignee(assigneeId)
        }

        if (closerId != null) {
            task.@closer = new Closer(closerId)
        }

        //We cannot access the private fields of the super class by using direct field access because
        //these fields are not inherited. Also, we cannot use properties because these fields don't
        //have setters. That's why we have resort to this ugly hack.
        ReflectionFieldSetter.setFieldValue(task, 'creationTime', creationTime)
        ReflectionFieldSetter.setFieldValue(task, 'id', id)
        ReflectionFieldSetter.setFieldValue(task, 'modificationTime', modificationTime)

        if (modifierId != null) {
            task.@modifier = new Modifier(modifierId)
        }

        task.@status = status
        task.@resolution = resolution
        task.@tags = tags

        return task
    }
}
