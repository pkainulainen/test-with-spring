package com.testwithspring.starter.unittests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * All test methods of this test class belong to the category A.
 *
 * @author Petri Kainulainen
 */
@Category(UnitTest.class)
public class CategoryTest {

    @Test
    public void categoryTest() {
        System.out.println("The category: UnitTest");
    }
}
