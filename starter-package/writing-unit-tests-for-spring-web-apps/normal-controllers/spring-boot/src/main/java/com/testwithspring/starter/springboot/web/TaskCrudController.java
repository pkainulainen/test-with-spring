package com.testwithspring.starter.springboot.web;

import com.testwithspring.starter.springboot.task.TaskCrudService;
import com.testwithspring.starter.springboot.task.TaskDTO;
import com.testwithspring.starter.springboot.task.TaskFormDTO;
import com.testwithspring.starter.springboot.task.TaskListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

/**
 * This controller processes HTTP requests that are related to tasks.
 */
@Controller
public class TaskCrudController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskCrudController.class);

    private static final String FEEDBACK_MESSAGE_KEY_TASK_CREATED = "feedback.message.task.created";
    private static final String FEEDBACK_MESSAGE_KEY_TASK_UPDATED = "feedback.message.task.updated";
    private static final String FEEDBACK_MESSAGE_KEY_TASK_DELETED = "feedback.message.task.deleted";
    private static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    private static final String MODEL_ATTRIBUTE_NAME_TASK = "task";
    private static final String MODEL_ATTRIBUTE_NAME_TASKS = "tasks";

    private static final String REDIRECT_ATTRIBUTE_TASK_ID = "taskId";

    private static final String VIEW_NAME_CREATE_TASK = "task/create";
    private static final String VIEW_NAME_TASK_LIST = "task/list";
    private static final String VIEW_NAME_UPDATE_TASK = "task/update";
    private static final String VIEW_NAME_VIEW_TASK = "task/view";
    private static final String VIEW_REDIRECT_VIEW_TASK = "redirect:/task/{taskId}";
    private static final String VIEW_REDIRECT_TASK_LIST = "redirect:/";

    private final TaskCrudService crudService;
    private final MessageSource messageSource;

    @Autowired
    TaskCrudController(TaskCrudService crudService, MessageSource messageSource) {
        this.crudService = crudService;
        this.messageSource = messageSource;
    }

    /**
     * Deletes an existing task.
     * @param taskId    The id of the deleted task.
     * @param currentLocale
     * @param redirectAttributes
     * @return          The view name of the task list.
     */
    @RequestMapping(value = "/task/{taskId}/delete", method = RequestMethod.GET)
    public String deleteTask(@PathVariable("taskId") Long taskId,
                             Locale currentLocale,
                             RedirectAttributes redirectAttributes) {
        LOGGER.info("Deleting task with id: {}", taskId);

        TaskDTO deleted = crudService.delete(taskId);
        LOGGER.info("Deleted task: {}", deleted);

        addFeedbackMessage(redirectAttributes, FEEDBACK_MESSAGE_KEY_TASK_DELETED, currentLocale, deleted.getTitle());

        return VIEW_REDIRECT_TASK_LIST;
    }

    /**
     * Renders the create a new task form.
     * @param model
     * @return      The view name of the new task form.
     */
    @RequestMapping(value = "/task/create", method = RequestMethod.GET)
    public String showCreateTaskForm(Model model) {
        LOGGER.info("Rendering create task form");

        TaskFormDTO formObject = new TaskFormDTO();
        model.addAttribute(MODEL_ATTRIBUTE_NAME_TASK, formObject);

        return VIEW_NAME_CREATE_TASK;
    }

    /**
     * Processes form submissions of the create new task form.
     * @param formObject
     * @param bindingResult
     * @param redirectAttributes
     * @param currentLocale
     * @return  The view name of the rendered view. If the form has validation error, this method returns
     *          the name of the form view. If the task was created successfully, this method returns the
     *          name of the redirect view that redirects the user to the view task view.
     */
    @RequestMapping(value = "/task/create", method = RequestMethod.POST)
    public String processCreateTaskForm(@Valid @ModelAttribute(MODEL_ATTRIBUTE_NAME_TASK) TaskFormDTO formObject,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes,
                                        Locale currentLocale) {
        LOGGER.info("Creating a new task with information: {}", formObject);

        if (bindingResult.hasErrors()) {
            LOGGER.error("Create new task form was submitted with binding errors. Rendering create task form");
            return VIEW_NAME_CREATE_TASK;
        }

        TaskDTO created = crudService.create(formObject);
        LOGGER.info("Created new task with information: {}", created);

        addFeedbackMessage(redirectAttributes, FEEDBACK_MESSAGE_KEY_TASK_CREATED, currentLocale, created.getTitle());
        redirectAttributes.addAttribute(REDIRECT_ATTRIBUTE_TASK_ID, created.getId());

        return VIEW_REDIRECT_VIEW_TASK;
    }

    /**
     * Renders the view that shows the information of an existing task.
     * @param taskId    The id of the shown task.
     * @param model
     * @return  The view name of the view task view.
     */
    @RequestMapping(value = "/task/{taskId}", method = RequestMethod.GET)
    public String showTask(@PathVariable("taskId") Long taskId, Model model) {
        LOGGER.info("Rendering task view for task with id: {}", taskId);

        TaskDTO task = crudService.findById(taskId);
        LOGGER.info("Found task: {}", task);

        model.addAttribute(MODEL_ATTRIBUTE_NAME_TASK, task);

        return VIEW_NAME_VIEW_TASK;
    }

    /**
     * Renders the task list view.
     * @param model
     * @return  The view name of the task list view.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showTaskList(Model model) {
        LOGGER.info("Rendering task list view");

        List<TaskListDTO> tasks = crudService.findAll();
        LOGGER.info("Found {} tasks", tasks.size());

        model.addAttribute(MODEL_ATTRIBUTE_NAME_TASKS, tasks);

        return VIEW_NAME_TASK_LIST;
    }

    /**
     * Renders the update task form.
     * @param taskId
     * @param model
     * @return  The view name of the update task form.
     */
    @RequestMapping(value = "/task/{taskId}/update", method = RequestMethod.GET)
    public String showUpdateTaskForm(@PathVariable("taskId") Long taskId, Model model) {
        LOGGER.info("Rendering update task form for task with id: {}", taskId);

        TaskDTO task = crudService.findById(taskId);
        LOGGER.debug("Found task: {}", task);

        TaskFormDTO formObject = createUpdateFormObject(task);
        LOGGER.info("Rendering update task form for form object: {}", formObject);

        model.addAttribute(MODEL_ATTRIBUTE_NAME_TASK, formObject);

        return VIEW_NAME_UPDATE_TASK;
    }

    private TaskFormDTO createUpdateFormObject(TaskDTO task) {
        TaskFormDTO formObject = new TaskFormDTO();

        formObject.setId(task.getId());
        formObject.setDescription(task.getDescription());
        formObject.setTitle(task.getTitle());

        return formObject;
    }

    /**
     * Processes the form submissions of the update task form.
     * @param formObject
     * @param bindingResult
     * @param redirectAttributes
     * @param currentLocale
     * @return  The view name of the rendered view. If the form has validation errors, this method returns
     * the name of the form view. If the task was updated successfully, this method returns the name of the
     * redirect view that redirects the user to the view task view.
     */
    @RequestMapping(value = "/task/{taskId}/update", method = RequestMethod.POST)
    public String processUpdateTaskForm(@Valid @ModelAttribute(MODEL_ATTRIBUTE_NAME_TASK) TaskFormDTO formObject,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes,
                                        Locale currentLocale) {
        LOGGER.info("Updating the information of an existing task by using information: {}", formObject);

        if (bindingResult.hasErrors()) {
            LOGGER.error("Update task form was submitted with binding errors. Rendering update task form");
            return VIEW_NAME_UPDATE_TASK;
        }

        TaskDTO updated = crudService.update(formObject);
        LOGGER.info("Updated the information of an existing task: {}", updated);

        addFeedbackMessage(redirectAttributes, FEEDBACK_MESSAGE_KEY_TASK_UPDATED, currentLocale, updated.getTitle());
        redirectAttributes.addAttribute(REDIRECT_ATTRIBUTE_TASK_ID, updated.getId());

        return VIEW_REDIRECT_VIEW_TASK;
    }

    /**
     * Adds a feedback message as a flash attribute. The added message is fetched by using a {@code MessageSource}
     * object.
     *
     * @param attributes        The {@code RedirectAttributes} object in which the message is added.
     * @param messageCode       The message code of the feedback message.
     * @param currentLocale     The current locale.
     * @param messageParameters The message parameters that are passed to the {@code MessageSource} when the feedback
     *                          message is resolved.
     */
    private void addFeedbackMessage(RedirectAttributes attributes,
                                    String messageCode,
                                    Locale currentLocale,
                                    Object... messageParameters) {
        LOGGER.debug("Adding feedback message with code: {}, locale: {}, and params: {}",
                messageCode,
                currentLocale,
                messageParameters
        );

        String localizedFeedbackMessage = messageSource.getMessage(messageCode, messageParameters, currentLocale);
        LOGGER.debug("Localized message is: {}", localizedFeedbackMessage);
        attributes.addFlashAttribute(FLASH_MESSAGE_KEY_FEEDBACK, localizedFeedbackMessage);
    }
}
