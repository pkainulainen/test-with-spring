package com.testwithspring.intermediate.task;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A dummy implementation which ensures that we can run the application.
 */
@Service
class DummyTaskSearchService implements TaskSearchService {

    @Override
    public List<TaskListDTO> search(String searchTerm) {
        return null;
    }
}
