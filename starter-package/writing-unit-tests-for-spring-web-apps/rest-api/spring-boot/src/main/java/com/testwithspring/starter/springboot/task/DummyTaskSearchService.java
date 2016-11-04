package com.testwithspring.starter.springboot.task;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class exists only because it allows us to run the
 * example application.
 */
@Service
public class DummyTaskSearchService implements TaskSearchService {
    @Override
    public List<TaskListDTO> search(String searchTerm) {
        return null;
    }
}
