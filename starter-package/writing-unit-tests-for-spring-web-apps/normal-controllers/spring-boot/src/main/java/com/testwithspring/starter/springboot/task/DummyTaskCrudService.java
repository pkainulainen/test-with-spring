package com.testwithspring.starter.springboot.task;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class exists only because it allows us to run the example
 * application.
 */
@Service
public class DummyTaskCrudService implements TaskCrudService {
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
