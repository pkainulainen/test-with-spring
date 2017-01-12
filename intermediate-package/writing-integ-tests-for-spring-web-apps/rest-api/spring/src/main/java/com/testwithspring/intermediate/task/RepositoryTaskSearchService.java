package com.testwithspring.intermediate.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class RepositoryTaskSearchService implements TaskSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTaskSearchService.class);

    private final TaskRepository repository;

    @Autowired
    RepositoryTaskSearchService(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TaskListDTO> search(String searchTerm) {
        LOGGER.info("Finding tasks by using search term: {}", searchTerm);

        List<TaskListDTO> results = repository.search(searchTerm);
        LOGGER.info("Found {} tasks by using search term: {}", results.size(), searchTerm);

        return results;
    }
}
