package com.testwithspring.intermediate.task;

import com.testwithspring.intermediate.common.NotFoundException;
import com.testwithspring.intermediate.user.LoggedInUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
class RepositoryTaskCrudService implements TaskCrudService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTaskCrudService.class);

    private final TaskRepository repository;

    @Autowired
    RepositoryTaskCrudService(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public TaskDTO create(TaskFormDTO task, LoggedInUser loggedInUser) {
        LOGGER.info("Creating a new task with information: {}", task);

        Task newTask = Task.getBuilder()
                .withCreator(loggedInUser.getId())
                .withDescription(task.getDescription())
                .withTitle(task.getTitle())
                .build();
        newTask = repository.save(newTask);
        LOGGER.info("Created a new task with information: {}", task);

        return mapToDTO(newTask);
    }

    @Transactional
    @Override
    public TaskDTO delete(Long id) {
        LOGGER.info("Deleting a task with id: {}", id);

        Task deleted = repository.findOne(id).orElseThrow(
                () -> new NotFoundException(String.format("No task found with id: %d", id))
        );
        repository.delete(deleted);
        LOGGER.info("Deleted the task: {}", deleted);

        return mapToDTO(deleted);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskListDTO> findAll() {
        LOGGER.info("Finding all tasks");

        List<TaskListDTO> tasks = repository.findAll();
        LOGGER.info("Found {} tasks", tasks.size());

        return tasks;
    }

    @Transactional(readOnly = true)
    @Override
    public TaskDTO findById(Long id) {
        LOGGER.info("Finding task with id: {}", id);

        Task found = repository.findOne(id).orElseThrow(
                () -> new NotFoundException(String.format("No task found with id: %d", id))
        );
        LOGGER.info("Found task: {}", found);
        return mapToDTO(found);
    }

    private TaskDTO mapToDTO(Task model) {
        TaskDTO dto = new TaskDTO();

        dto.setId(model.getId());

        Assignee assignee = model.getAssignee();
        if (assignee != null) {
            dto.setAssigneeId(assignee.getUserId());
        }

        Closer closer = model.getCloser();
        if (closer != null) {
            dto.setCloserId(closer.getUserId());
        }

        dto.setCreationTime(model.getCreationTime());
        dto.setCreatorId(model.getCreator().getUserId());
        dto.setDescription(model.getDescription());
        dto.setModificationTime(model.getModificationTime());
        dto.setStatus(model.getStatus());
        dto.setResolution(model.getResolution());
        dto.setTitle(model.getTitle());

        List<TagDTO> tagDTOs = new ArrayList<>();

        Set<Tag> tagModels = model.getTags();
        for (Tag tagModel: tagModels) {
            TagDTO tagDTO = new TagDTO();

            tagDTO.setId(tagModel.getId());
            tagDTO.setName(tagModel.getName());

            tagDTOs.add(tagDTO);
        }
        dto.setTags(tagDTOs);

        return dto;
    }

    @Transactional
    @Override
    public TaskDTO update(TaskFormDTO task, LoggedInUser loggedInUser) {
        LOGGER.info("Updating existing task by using information: {}", task);

        Task updated = repository.findOne(task.getId()).orElseThrow(
                () -> new NotFoundException(String.format("No task found with id: %d", task.getId()))
        );
        LOGGER.debug("Found task: {}", updated);

        updated.setModifier(loggedInUser.getId());
        updated.setDescription(task.getDescription());
        updated.setTitle(task.getTitle());

        LOGGER.info("Updated existing task: {}", updated);

        return mapToDTO(updated);
    }
}
