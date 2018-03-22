package com.testwithspring.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Builds the url addresses used to communicate with the
 * remote message API.
 */
@Component
class MessageApiUrlBuilder {

    private static final String CREATE_MESSAGE_PATH = "/api/message";
    private static final String FIND_MESSAGE_PATH = "/api/message/{id}";

    private final String apiBaseUrl;

    @Autowired
    MessageApiUrlBuilder(@Value("${message.api.base.url}") String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    /**
     * Build the url address that is used to create new messages.
     * @return
     */
    String buildCreateMessageUrl() {
        return UriComponentsBuilder.fromUriString(apiBaseUrl)
                .path(CREATE_MESSAGE_PATH)
                .build()
                .toUriString();
    }

    /**
     * Build the url address that is used to find a single message.
     * @param id    The id of the requested message.
     * @return
     */
    String buildFindMessageUrl(Long id) {
        return UriComponentsBuilder.fromUriString(apiBaseUrl)
                .path(FIND_MESSAGE_PATH)
                .buildAndExpand(id)
                .toUriString();
    }
}
