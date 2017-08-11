package com.testwithspring.master.task

import com.testwithspring.master.user.PersonDTO


/**
 * This test data builder is used for creating new {@link TaskDTO} objects
 * that are used in our unit tests. The reason why we should use a builder is
 * that the {@link TaskDTO} class is not just a data container because we
 * have to understand the different states of a task before we can create
 * new {@link TaskDTO} objects.
 */
class TaskDTOBuilder {

    private assignee
    private closer
    private creationTime
    private creator
    private description
    private id
    private modificationTime
    private modifier
    private resolution
    private status
    private tags = []
    private title

    def withId(id) {
        this.id = id
        return this
    }

    def withAssignee(assignee) {
        this.assignee = assignee
        return this
    }

    def withCreationTime(creationTime) {
        this.creationTime = creationTime
        return this
    }

    def withCreator(creator) {
        this.creator = creator
        return this
    }

    def withDescription(description) {
        this.description = description
        return this
    }

    def withModificationTime(modificationTime) {
        this.modificationTime = modificationTime
        return this
    }

    def withModifier(PersonDTO modifier) {
        this.modifier = modifier
        return this
    }

    def withResolutionDuplicate(closer) {
        this.closer = closer
        this.resolution = TaskResolution.DUPLICATE
        this.status = TaskStatus.CLOSED
        return this
    }

    def withResolutionDone(closer) {
        this.closer = closer
        this.resolution = TaskResolution.DONE
        this.status = TaskStatus.CLOSED
        return this
    }

    def withResolutionWontDo(closer) {
        this.closer = closer
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

    def withTags(tags) {
        this.tags = Arrays.asList(tags)
        return this
    }

    def withTitle(title) {
        this.title = title
        return this
    }

    def build() {
        return new TaskDTO(assignee: this.assignee,
                closer: this.closer,
                creationTime: this.creationTime,
                creator: this.creator,
                description: this.description,
                id: this.id,
                modificationTime: this.modificationTime,
                modifier: this.modifier,
                resolution: this.resolution,
                status: this.status,
                tags: this.tags,
                title: this.title
        )
    }
}
