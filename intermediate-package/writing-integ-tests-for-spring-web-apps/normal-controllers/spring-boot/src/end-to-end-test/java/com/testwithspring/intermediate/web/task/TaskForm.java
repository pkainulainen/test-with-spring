package com.testwithspring.intermediate.web.task;

import com.testwithspring.intermediate.EndToEndTestTasks;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * This page object represents the form that is used to
 * create new tasks and update the information of existing
 * tasks.
 */
final class TaskForm {

    private WebDriver browser;
    private WebElement form;

    TaskForm(WebDriver browser, String formId) {
        this.browser = browser;
        this.form = this.browser.findElement(By.id(formId));
    }

    /**
     * Clears the current description and enters the new description of the task
     * to the description text area.
     * @param description
     */
    void typeDescription(String description) {
        WebElement descriptionTextArea = form.findElement(By.id("task-description"));
        descriptionTextArea.clear();
        descriptionTextArea.sendKeys(description);
    }

    /**
     * Clears the current title and enters the title of the task to the title text field.
     * @param title
     */
    void typeTitle(String title) {
        WebElement titleInputField = form.findElement(By.id("task-title"));
        titleInputField.clear();
        titleInputField.sendKeys(title);
    }

    /**
     * Submits the task form. The method assumes that validation
     * is successful and the user is forwarded to the view task page.
     * @return  The page object that represents the view task page.
     */
    TaskPage submitTaskForm() {
        Long taskId = getTaskId();
        form.submit();
        return new TaskPage(browser, taskId);
    }

    /**
     * Returns the text that is entered to the description text area.
     * @return
     */
    String getTaskDescription() {
        return form.findElement(By.id("task-description")).getAttribute("value");
    }

    /**
     * Finds the id of the task. If this task form creates a new task, this method returns
     * {@link com.testwithspring.intermediate.EndToEndTestTasks#NEW_TASK_ID}. On the other hand,
     * if this task form updates an existing task, this method returns the id of the updated task.
     * @return
     */
    private Long getTaskId() {
        List<WebElement> idElements = form.findElements(By.id("task-id"));
        if (idElements.size() == 1) {
            String taskId = idElements.get(0).getAttribute("value");
            return Long.parseLong(taskId);
        }

        //Because hidden id was not found, this form is create task form and
        //we need to return the id of the new task.
        return EndToEndTestTasks.NEW_TASK_ID;
    }

    /**
     * Returns the text that is entered to the title text field.
     * @return
     */
    String getTaskTitle() {
        return form.findElement(By.id("task-title")).getAttribute("value");
    }
}