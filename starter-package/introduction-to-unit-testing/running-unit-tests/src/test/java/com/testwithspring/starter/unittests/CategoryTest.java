package com.testwithspring.starter.unittests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(UnitTest.class)
public class CategoryTest {

    @Test
    public void categoryTest() {
        System.out.println("The category: UnitTest");
    }
}
