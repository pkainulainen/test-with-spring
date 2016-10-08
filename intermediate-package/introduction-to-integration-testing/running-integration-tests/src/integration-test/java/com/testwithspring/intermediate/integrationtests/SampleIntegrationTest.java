package com.testwithspring.intermediate.integrationtests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IntegrationTest.class)
public class SampleIntegrationTest {

    @Test
    public void sampleIntegrationTest() {
        System.out.println("Category: IntegrationTest");
    }
}
