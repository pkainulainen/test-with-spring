package com.testwithspring.starter.unittests;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
@Category(UnitTest.class)
public class JUnitParamsCalculatorTest {

    @Test
    @Parameters({
            "0, 0, 0",
            "1, 1, 2"
    })
    public void shouldReturnCorrectSum(int first, int second, int expectedSum) {
        Calculator calculator = new Calculator();
        int actualSum = calculator.sum(first, second);
        assertThat(actualSum).isEqualByComparingTo(expectedSum);
    }
}
