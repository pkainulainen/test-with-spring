package com.testwithspring.starter.junit4;

import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * All test methods of this test class belong to the category B.
 *
 * @author Petri Kainulainen
 */
@Category(CategoryB.class)
public class BTest {

    @Test
    public void testB() {
        System.out.println("This test belongs to the category B");
    }
}
