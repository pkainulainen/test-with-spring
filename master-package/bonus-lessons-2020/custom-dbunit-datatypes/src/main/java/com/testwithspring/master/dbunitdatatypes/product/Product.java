package com.testwithspring.master.dbunitdatatypes.product;

import java.util.List;
import java.util.Set;

/**
 * Contains the information of a single product that's
 * found from the <code>products</code> database table.
 */
public class Product {

    private Long id;
    private String description;
    private String name;
    private List<Review> reviews;
    private Set<String> tags;

    public Product() {}

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
