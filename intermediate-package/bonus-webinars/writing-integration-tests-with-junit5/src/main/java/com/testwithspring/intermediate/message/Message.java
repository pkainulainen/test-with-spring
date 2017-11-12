package com.testwithspring.intermediate.message;

import javax.persistence.*;

/**
 * Contains the information of a single message that is found
 * from our database.
 */
@Entity
@Table(name = "messages")
class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    Long getId() {
        return id;
    }

    String getText() {
        return text;
    }
}
