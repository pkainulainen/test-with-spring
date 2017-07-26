package com.testwithspring.master

import org.junit.experimental.categories.Category;
import spock.lang.Specification;

/**
 * This spec simply demonstrates that we can run Spock specifications.
 */
@Category(UnitTest.class)
class TestSpec extends Specification {

    def 'should pass'() {
        expect:
        true
    }
}
