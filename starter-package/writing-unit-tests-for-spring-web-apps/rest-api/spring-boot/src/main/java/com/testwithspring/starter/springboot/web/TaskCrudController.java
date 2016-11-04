package com.testwithspring.starter.springboot.web;

import com.testwithspring.starter.springboot.task.TaskCrudService;
import com.testwithspring.starter.springboot.task.TaskDTO;
import com.testwithspring.starter.springboot.task.TaskFormDTO;
import com.testwithspring.starter.springboot.task.TaskListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public TaskDTO create(@Valid @RequestBody TaskFormDTO task) {
        LOGGER.info("Creating a new task with information: {}", task);

        TaskDTO created = crudService.create(task);
        LOGGER.info("Created a new task with information: {}", created);

        return created;
    }

    /**
     * Deletes an existing task.
     * @param taskId    The id of the deleted task.
     * @return          The information of the deleted task.
     * @throws com.testwithspring.starter.springboot.task.TaskNotFoundException if the deleted task
     * is not found.
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
     * @throws com.testwithspring.starter.springboot.task.TaskNotFoundException if the deleted task
     * is not found.
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
     * @throws com.testwithspring.starter.springboot.task.TaskNotFoundException if the deleted task
     * is not found.
     */
    @RequestMapping(value= "{taskId}", method = RequestMethod.PUT)
    public TaskDTO update(@PathVariable("taskId") Long taskId, @Valid @RequestBody TaskFormDTO task) {
        task.setId(taskId);
        LOGGER.info("Updates an existing task with information: {}", task);

        TaskDTO updated = crudService.update(task);
        LOGGER.info("Updated the information of a task: {}", updated);

        return updated;
    }
}
