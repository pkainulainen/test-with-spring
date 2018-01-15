package com.testwithspring.master;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag(TestTags.INTEGRATION_TEST)
class IntegrationTest {

    @Test
    @DisplayName("This test method should be run when we run our integration tests")
    void shouldBeRunWhenIntegrationTestsAreRun() {
    }
}
