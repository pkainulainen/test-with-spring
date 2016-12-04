package com.testwithspring.intermediate.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * A dummy implementation which ensures that we can run the application
 */
@Service
class RepositoryTaskCrudService implements TaskCrudService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTaskCrudService.class);

    private final TaskRepository repository;

    @Autowired
    RepositoryTaskCrudService(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public TaskDTO create(TaskFormDTO task) {
        return null;
    }

    @Override
    public TaskDTO delete(Long id) {
        return null;
    }

    @Override
    public List<TaskListDTO> findAll() {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public TaskDTO findById(Long id) {
        LOGGER.info("Finding task with id: {}", id);

        Task found = repository.findOne(id).orElseThrow(
                () -> new TaskNotFoundException(String.format("No task found with id: %d", id))
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

        return dto;
    }

    @Override
    public TaskDTO update(TaskFormDTO task) {
        return null;
    }
}
