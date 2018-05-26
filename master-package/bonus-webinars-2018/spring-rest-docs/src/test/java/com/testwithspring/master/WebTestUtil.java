package com.testwithspring.master;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

final class WebTestUtil {

    /**
     * Prevents instantiation.
     */
    private WebTestUtil() {}


    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(object);
    }
}
