package com.testwithspring.master.web

import com.fasterxml.jackson.databind.ObjectMapper

import static com.testwithspring.master.web.WebTestConfig.objectMapper

/**
 * Converts objects into JSON byte arrays that can be send to
 * the tested controller method by using the Spring MVC Test
 * framework. Note that the result of this conversion must
 * always be send in the request body.
 */
final class ObjectIntoJsonConverter {

    /**
     * Converts an object into JSON bytes and returns the
     * created byte array.
     * @param object    The converted object.
     * @return          The byte array that contains the information
     *                  of the given object as JSON.
     * @throws IOException  If an error occurs during the conversion.
     */
    def static convertObjectIntoJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = objectMapper()
        return mapper.writeValueAsBytes(object)
    }
}
