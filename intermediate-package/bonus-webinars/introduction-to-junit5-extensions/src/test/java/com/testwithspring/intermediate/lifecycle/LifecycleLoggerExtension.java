package com.testwithspring.intermediate.lifecycle;

import org.junit.jupiter.api.extension.*;

public class LifecycleLoggerExtension implements AfterAllCallback,
        AfterEachCallback,
        AfterTestExecutionCallback,
        BeforeAllCallback,
        BeforeEachCallback,
        BeforeTestExecutionCallback  {

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        System.out.println("afterAll() extension");
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        System.out.println("afterEach() extension");
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        System.out.println("afterTestExecution() extension");
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        System.out.println("beforeAll() extension");
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        System.out.println("beforeEach() extension");
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        System.out.println("beforeTestExecution() extension");
    }
}
