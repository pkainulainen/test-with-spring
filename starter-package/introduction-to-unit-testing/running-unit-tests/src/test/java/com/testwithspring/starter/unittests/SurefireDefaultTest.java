package com.testwithspring.starter.unittests;

import org.junit.Test;

/**
 * All test methods of this test class are invoked when we use
 * the default configuration of the Maven Surefire plugin.
 *
 * @author Petri Kainulainen
 */
public class SurefireDefaultTest {

    @Test
    public void testB() {
        System.out.println("The default configuration of Maven Surefire plugin.");
    }
}
