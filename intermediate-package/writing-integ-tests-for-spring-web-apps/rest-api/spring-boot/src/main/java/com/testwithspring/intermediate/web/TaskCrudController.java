package com.testwithspring.intermediate.web;

import com.testwithspring.intermediate.task.TaskCrudService;
import com.testwithspring.intermediate.task.TaskDTO;
import com.testwithspring.intermediate.task.TaskFormDTO;
import com.testwithspring.intermediate.task.TaskListDTO;
import com.testwithspring.intermediate.user.LoggedInUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * This controller implements the REST API that provides CRUD
 * operations for tasks.
 */
@RestController
@RequestMapping("/api/task")
public class TaskCrudController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskCrudController.class);

    private final TaskCrudService crudService;

    @Autowired
    TaskCrudController(TaskCrudService crudService) {
        this.crudService = crudService;
    }

    /**
     * Creates a new task.
     * @param task  The information of the created task.
     * @return      The information of the created task.
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskFormDTO task, @AuthenticationPrincipal LoggedInUser loggedInUser) {
        LOGGER.info("Creating a new task with information: {}", task);

        TaskDTO created = crudService.create(task, loggedInUser);
        LOGGER.info("Created a new task with information: {}", created);

        return created;
    }

    /**
     * Deletes an existing task.
     * @param taskId    The id of the deleted task.
     * @return          The information of the deleted task.
     * @throws com.testwithspring.intermediate.common.NotFoundException if the taks is not found.
     */
    @RequestMapping(value = "{taskId}", method = RequestMethod.DELETE)
    public TaskDTO delete(@PathVariable("taskId") Long taskId) {
        LOGGER.info("Deleting task with id: {}", taskId);

        TaskDTO deleted = crudService.delete(taskId);
        LOGGER.info("Deleted task: {}", deleted);

        return deleted;
    }

    /**
     * Finds all tasks.
     * @return  A list of tasks found from the database. If no tasks is found,
     * this method returns an empty list.
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<TaskListDTO> findAll() {
        LOGGER.info("Finding all tasks");

        List<TaskListDTO> tasks = crudService.findAll();
        LOGGER.info("Found {} tasks", tasks.size());

        return tasks;
    }

    /**
     * Finds a single task.
     * @param taskId    The id of the requested task.
     * @return          The information of the found task.
     * @throws com.testwithspring.intermediate.common.NotFoundException if the taks is not found.
     */
    @RequestMapping(value = "{taskId}", method = RequestMethod.GET)
    public TaskDTO findById(@PathVariable("taskId") Long taskId) {
        LOGGER.info("Finding task with id: {}", taskId);

        TaskDTO found = crudService.findById(taskId);
        LOGGER.info("Found task: {}", found);

        return found;
    }

    /**
     * Updates the information of an existing task.
     * @param task  The new information of an existing task.
     * @return      The information of the updated task.
     * @throws com.testwithspring.intermediate.common.NotFoundException if the taks is not found.
     */
    @RequestMapping(value= "{taskId}", method = RequestMethod.PUT)
    public TaskDTO update(@PathVariable("taskId") Long taskId,
                          @Valid @RequestBody TaskFormDTO task,
                          @AuthenticationPrincipal LoggedInUser loggedInUser) {
        task.setId(taskId);
        LOGGER.info("Updates an existing task with information: {}", task);

        TaskDTO updated = crudService.update(task, loggedInUser);
        LOGGER.info("Updated the information of a task: {}", updated);

        return updated;
    }
}