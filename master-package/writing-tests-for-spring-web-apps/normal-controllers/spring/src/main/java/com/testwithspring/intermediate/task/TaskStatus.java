package com.testwithspring.intermediate.task;

public enum TaskStatus {
    CLOSED,
    IN_PROGRESS,
    OPEN;

    /**
     * Returns the localization key that is used to fetch the localized
     * task status from the localization file.
     * @return
     */
    public String getLocalizationKey() {
        return this.getClass().getSimpleName() + "." + this.name();
    }
}
