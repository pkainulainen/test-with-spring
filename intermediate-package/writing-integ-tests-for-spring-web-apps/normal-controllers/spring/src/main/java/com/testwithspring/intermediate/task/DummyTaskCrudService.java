package com.testwithspring.intermediate.task;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A dummy implementation which ensures that we can run the application
 */
@Service
class DummyTaskCrudService implements TaskCrudService {

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

    @Override
    public TaskDTO findById(Long id) {
        return null;
    }

    @Override
    public TaskDTO update(TaskFormDTO task) {
        return null;
    }
}
