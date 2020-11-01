package com.testwithspring.master.dbunitdatatypes.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * This repository queries data from the <code>products</code>
 * database table.
 */
@Repository
class ProductRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRepository.class);

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_DESCRIPTION = "product_description";
    private static final String COLUMN_NAME_NAME = "product_name";
    private static final String COLUMN_NAME_REVIEWS = "reviews";
    private static final String COLUMN_NAME_TAGS = "tags";

    private static final String QUERY_FIND_BY_ID = "SELECT id, product_description, product_name, reviews, tags FROM products WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    ProductRepository(NamedParameterJdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Finds a product by using its id as a search criteria.
     * @param id    The id of the queried product.
     * @return      An {@code Optional} object that contains the found product.
     *              If no product is found, this method returns an empty {@code Optional}.
     */
    Optional<Product> findById(Long id) {
        LOGGER.info("Finding a product with id: #{}", id);

        Map<String, Object> queryParams = Collections.singletonMap(COLUMN_NAME_ID, id);

        Optional<Product> found = jdbcTemplate.query(QUERY_FIND_BY_ID, queryParams, new ProductResultSetExtractor());
        LOGGER.info("Found product: {} with id: #{}", found, id);

        return found;
    }

    private class ProductResultSetExtractor implements ResultSetExtractor<Optional<Product>> {

        @Override
        public Optional<Product> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            if (resultSet.next()) {
                try {
                    Product product = new Product();

                    product.setId(resultSet.getLong(COLUMN_NAME_ID));
                    product.setDescription(resultSet.getString(COLUMN_NAME_DESCRIPTION));
                    product.setName(resultSet.getString(COLUMN_NAME_NAME));

                    String reviewsAsJson = resultSet.getString(COLUMN_NAME_REVIEWS);
                    List<Review> reviews = Arrays.asList(objectMapper.readValue(reviewsAsJson, Review[].class));
                    product.setReviews(reviews);

                    //This is a hack and I decided to use it only because I had no time
                    //to figure out a better solution. This line is based on the fact
                    //that the string value of an array column uses this format: '{value1,value2}
                    String[] tagValues = resultSet.getString(COLUMN_NAME_TAGS)
                            .replace("\"", "")
                            .replace("{", "")
                            .replace("}", "")
                            .split(",");
                    Set<String> tags = new HashSet<>(Arrays.asList(tagValues));
                    product.setTags(tags);

                    return Optional.of(product);
                }
                catch (JsonProcessingException e) {
                    throw new DataRetrievalFailureException(
                            "Could not transform json string into an because because an error occurred",
                            e
                    );
                }
            }
            else {
                return Optional.empty();
            }
        }
    }
}
