package com.testwithspring.master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Provides methods used to create new messages and find
 * existing messages.
 */
@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private final RestTemplate restTemplate;
    private final MessageApiUrlBuilder urlBuilder;

    @Autowired
    MessageService(RestTemplateBuilder restTemplateBuilder, MessageApiUrlBuilder urlBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.urlBuilder = urlBuilder;
    }

    /**
     * Creates a new message.
     * @param messageText   The message text of the created message.
     * @return  The information of the created message.
     */
    public Message create(String messageText) {
        LOGGER.info("Creating a new message with message text: {}", messageText);

        Message input = new Message();
        input.setText(messageText);

        String url = urlBuilder.buildCreateMessageUrl();
        Message created = restTemplate.postForObject(url, input, Message.class);

        LOGGER.info("Created a new message: {}", created);
        return created;
    }

    /**
     * Finds the information of a single message.
     * @param id    The id of the requested message.
     * @return  An {@code Optional} object that contains the found message.
     *          If no message is found, this method returns an empty {@code Optional}.
     */
    public Optional<Message> findById(Long id) {
        LOGGER.info("Finding message by id: {}", id);

        String url = urlBuilder.buildFindMessageUrl(id);

        try {
            Message message = restTemplate.getForObject(url, Message.class);
            LOGGER.info("Found message: {}", message);

            return Optional.of(message);
        }
        catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                LOGGER.info("No message was found with id: {}", id);
                return Optional.empty();
            }
            throw ex;
        }
    }
}
