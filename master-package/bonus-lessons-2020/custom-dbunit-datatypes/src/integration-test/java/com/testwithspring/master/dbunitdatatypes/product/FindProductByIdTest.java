package com.testwithspring.master.dbunitdatatypes.product;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.testwithspring.master.dbunitdatatypes.Products;
import com.testwithspring.master.dbunitdatatypes.config.DatabaseIntegrationTest;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DatabaseIntegrationTest
@DatabaseSetup("/com/testwithspring/master/dbunitdatatypes/products.xml")
@DisplayName("Find a product by using id as a search criteria")
class FindProductByIdTest {

    @Autowired
    private ProductRepository repository;

    @Nested
    @DatabaseIntegrationTest
    @DatabaseSetup("/com/testwithspring/master/dbunitdatatypes/products.xml")
    @DisplayName("When the requested product isn't found")
    class WhenProductIsNotFound {

        @Test
        @DisplayName("Should return an empty optional")
        void shouldReturnEmptyOptional() {
            Optional<Product> found = repository.findById(Products.UNKNOWN_ID);
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @ExtendWith(SoftAssertionsExtension.class)
    @DatabaseIntegrationTest
    @DatabaseSetup("/com/testwithspring/master/dbunitdatatypes/products.xml")
    @DisplayName("When the requested product is found")
    class WhenProductIsFound {

        @Test
        @DisplayName("Should return an optional which contains the found product")
        void shouldReturnOptionalWhichContainsFoundProduct() {
            Optional<Product> found = repository.findById(Products.Product.ID);
            assertThat(found).isNotEmpty();
        }

        @Test
        @DisplayName("Should return a product with the correct id")
        void shouldReturnProductWithCorrectId() {
            Product found = repository.findById(Products.Product.ID).get();
            assertThat(found.getId()).isEqualByComparingTo(Products.Product.ID);
        }

        @Test
        @DisplayName("Should return a product with the correct description")
        void shouldReturnProductWithCorrectDescription() {
            Product found = repository.findById(Products.Product.ID).get();
            assertThat(found.getDescription()).isEqualTo(Products.Product.DESCRIPTION);
        }

        @Test
        @DisplayName("Should return a product with the correct name")
        void shouldReturnProductWithCorrectName() {
            Product found = repository.findById(Products.Product.ID).get();
            assertThat(found.getName()).isEqualTo(Products.Product.NAME);
        }

        @Test
        @DisplayName("Should return a product that has one review")
        void shouldReturnProductThatHasOneReview() {
            Product found = repository.findById(Products.Product.ID).get();
            assertThat(found.getReviews()).hasSize(1);
        }

        @Test
        @DisplayName("Should return a product that has the correct review")
        void shouldReturnProductThatHasCorrectReview(SoftAssertions assertions) {
            Review found = repository.findById(Products.Product.ID)
                    .get()
                    .getReviews()
                    .get(0);

            assertions.assertThat(found.getId())
                    .overridingErrorMessage(
                            "Expecting the id to be: %d but was: %d",
                            Products.Product.Review.ID,
                            found.getId()
                    )
                    .isEqualByComparingTo(Products.Product.Review.ID);
            assertions.assertThat(found.getAuthor())
                    .overridingErrorMessage(
                            "Expected the author to be: %s but was: %s",
                            Products.Product.Review.AUTHOR,
                            found.getAuthor()
                    )
                    .isEqualTo(Products.Product.Review.AUTHOR);
            assertions.assertThat(found.getScore())
                    .overridingErrorMessage(
                            "Expected the score to be: %d but was: %s",
                            Products.Product.Review.SCORE,
                            found.getScore()
                    )
                    .isEqualByComparingTo(Products.Product.Review.SCORE);
            assertions.assertThat(found.getText())
                    .overridingErrorMessage(
                            "Expecting the text to be: %s but was: %s",
                            Products.Product.Review.TEXT,
                            found.getText()
                    )
                    .isEqualTo(Products.Product.Review.TEXT);

            assertions.assertAll();
        }

        @Test
        @DisplayName("Should return a product that has two tags")
        void shouldReturnProductThatHasTwoTags() {
            Product found = repository.findById(Products.Product.ID).get();
            assertThat(found.getTags()).hasSize(2);
        }

        @Test
        @DisplayName("Should return a product that has the correct tags")
        void shouldReturnProductThatHasCorrectTags() {
            Product found = repository.findById(Products.Product.ID).get();
            assertThat(found.getTags()).contains(Products.Product.Tags.TAG_ONE, Products.Product.Tags.TAG_TWO);
        }
    }
}
