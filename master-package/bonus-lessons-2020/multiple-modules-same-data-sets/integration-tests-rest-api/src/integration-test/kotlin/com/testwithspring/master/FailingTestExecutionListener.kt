package com.testwithspring.master

import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListener
import java.lang.NullPointerException

class FailingTestExecutionListener: TestExecutionListener {

    override fun beforeTestExecution(testContext: TestContext) {
        throw NullPointerException("PERSE")
    }
}