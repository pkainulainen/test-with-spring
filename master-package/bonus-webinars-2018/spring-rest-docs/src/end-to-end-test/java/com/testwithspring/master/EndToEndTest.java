package com.testwithspring.master;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag(TestTags.END_TO_END_TEST)
class EndToEndTest {

    @Test
    @DisplayName("This test method should be run when we run our end-to-end tests")
    void shouldBeRunWhenEndToEndTestsAreRun() {
    }
}
