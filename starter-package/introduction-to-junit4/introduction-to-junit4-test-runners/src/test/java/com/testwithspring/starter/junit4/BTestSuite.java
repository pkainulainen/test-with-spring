package com.testwithspring.starter.junit4;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This test suite runs all tests that belong to the category B.
 *
 * @author Petri Kainulainen
 */
@RunWith(Categories.class)
@IncludeCategory(CategoryB.class)
@SuiteClasses({ATest.class, BTest.class})
public class BTestSuite {
}
