package com.testwithspring.intermediate;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertTrue;

@Category(EndToEndTest.class)
public class PlaceHolderTest {

    @Test
    public void alwaysPass() {
        assertTrue(true);
    }
}
