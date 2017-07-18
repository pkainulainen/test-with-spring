package com.testwithspring.master.message;

/**
 * Provides CRUD operations for messages.
 */
interface MessageRepository {

    /**
     * Saves a new message to the database.
     * @param message   The saved message.
     * @return          The saved message.
     */
    Message save(Message message);
}
