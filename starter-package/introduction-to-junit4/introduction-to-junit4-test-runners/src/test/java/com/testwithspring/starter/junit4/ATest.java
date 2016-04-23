package com.testwithspring.starter.junit4;

import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * All test methods of this test class belong to the category A.
 *
 * @author Petri Kainulainen
 */
@Category(CategoryA.class)
public class ATest {

    @Test
    public void testA() {
        System.out.println("This test belongs to the category A");
    }
}
