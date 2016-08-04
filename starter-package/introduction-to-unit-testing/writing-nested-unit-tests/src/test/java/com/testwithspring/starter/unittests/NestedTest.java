package com.testwithspring.starter.unittests;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

/**
 * This class demonstrates how we can write nested unit tests by using the
 * HierarchicalContextRunner.
 */
@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class NestedTest {

    /**
     * This test method is invoked once before any test method found from this class
     * or from the inner classes is invoked.
     */
    @BeforeClass
    public static void beforeAllTestMethods() {
        System.out.println("Invoked once before all test methods");
    }

    /**
     * This method is invoked before a test method found from this class or
     * from the inner classes is invoked.
     */
    @Before
    public void beforeEachTestMethod() {
        System.out.println("Invoked before each test method");
    }

    /**
     * This method is invoked after a test method found from this class or
     * from the inner classes is invoked.
     */
    @After
    public void afterEachTestMethod() {
        System.out.println("Invoked after each test method");
    }

    /**
     * This test method is invoked once after all test methods found from this class
     * or from the inner classes have been invoked.
     */
    @AfterClass
    public static void afterAllTestMethods() {
        System.out.println("Invoked once after all test methods");
    }

    @Test
    public void testOnRootLevel() {
        System.out.println("Test on root level");
    }

    public class ContextA {

        /**
         * This test method is invoked before a test method, that is found
         * from this inner class or from child classes of this inner
         * class, is invoked.
         */
        @Before
        public void beforeEachTestMethodOfContextA() {
            System.out.println("Invoked before each test method of context A");
        }

        /**
         * This test method is invoked after a test method, that is found
         * from this inner class or from child classes of this inner
         * class, is invoked.
         */
        @After
        public void afterEachTestMethodOfContextA() {
            System.out.println("Invoked after each test method of context A");
        }

        @Test
        public void contextATest() {
            System.out.println("Context A test");
        }

        public class ContextC {

            /**
             * This test method is invoked before a test method, that is found
             * from this inner class, is invoked.
             */
            @Before
            public void beforeEachTestMethodOfContextC() {
                System.out.println("Invoked before each test method of context C");
            }

            /**
             * This test method is invoked after a test method, that is found
             * from this inner class, is invoked.
             */
            @After
            public void afterEachTestMethodOfContextC() {
                System.out.println("Invoked after each test method of context C");
            }

            @Test
            public void contextCTest() {
                System.out.println("Context C test");
            }
        }
    }

    public class ContextB {

        /**
         * This test method is invoked before a test method, that is found
         * from this inner class, is invoked.
         */
        @Before
        public void beforeEachTestMethodInContextB() {
            System.out.println("Invoked before each test method of context B");
        }

        /**
         * This test method is invoked after a test method, that is found
         * from this inner class, is invoked.
         */
        @After
        public void afterEachTestMethodOfContextB() {
            System.out.println("Invoked after each test method of context B");
        }

        @Test
        public void contextBTest() {
            System.out.println("Context B test");
        }
    }
}
