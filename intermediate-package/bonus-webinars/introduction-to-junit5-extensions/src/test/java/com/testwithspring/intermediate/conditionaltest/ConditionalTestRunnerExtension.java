package com.testwithspring.intermediate.conditionaltest;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * This extension disables all test methods whose name
 * starts with the string: 'ignore'.
 */
public class ConditionalTestRunnerExtension implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext extensionContext) {
        Optional<Method> testMethod = extensionContext.getTestMethod();
        if (testMethod.isPresent()) {
            String methodName = testMethod.get().getName();
            if (methodName.startsWith("ignore")) {
                return ConditionEvaluationResult.disabled("The name of the test method starts with the string: ignore");
            }
            else {
                return ConditionEvaluationResult.enabled("The name of the test method doesn't start with the string: ignore");
            }
        }
        return ConditionEvaluationResult.enabled("No test method found");
    }
}
