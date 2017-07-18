package com.testwithspring.master.message;

import java.util.Optional;

/**
 * Provides CRUD operations for messages.
 */
interface MessageRepository {

    /**
     * Finds a message from the database by using its id as a search criteria.
     * @param id    The id of the retrieved message.
     * @return      An {@code Optional} that contains the found message. If
     *              no message is found, this method returns an empty {@code Optional}
     */
    Optional<Message> findById(Long id);

    /**
     * Saves a new message to the database.
     * @param message   The saved message.
     * @return          The saved message.
     */
    Message save(Message message);
}
