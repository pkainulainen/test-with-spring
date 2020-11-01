package com.testwithspring.master.dbunitdatatypes.product;

/**
 * Contains the information of a review that's given
 * to a product.
 */
public class Review {

    private Long id;
    private String author;
    private int score;
    private String text;

    public Review() {

    }

    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public int getScore() {
        return score;
    }

    public String getText() {
        return text;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setText(String text) {
        this.text = text;
    }
}
