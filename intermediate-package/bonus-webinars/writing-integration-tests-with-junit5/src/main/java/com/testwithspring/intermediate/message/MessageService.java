package com.testwithspring.intermediate.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Declares a method that is used to query information from our database.
 */
@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository repository;

    @Autowired
    MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    /**
     * Finds all messages from the database.
     * @return
     */
    @Transactional(readOnly = true)
    public List<MessageDTO> findAll() {
        LOGGER.info("Finding all messages from the database");

        List<Message> messages = repository.findAll();
        LOGGER.info("Found {} messages", messages.size());

        return mapToDTOs(messages);
    }

    private List<MessageDTO> mapToDTOs(List<Message> models) {
        List<MessageDTO> dtos = new ArrayList<>();

        models.forEach(model -> {
            MessageDTO dto = new MessageDTO();
            dto.setId(model.getId());
            dto.setText(model.getText());
            dtos.add(dto);
        });

        return dtos;
    }
}
