package com.testwithspring.starter.testdata.javabean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is a simple mock that allows us the specify the expected id
 * argument and configure the returned {@code Task} object.
 *
 * If the user wants to verify the {@code deleteById()} method was invoked
 * by using the correct method parameter, he has to invoke the {@code verify()}
 * method.
 */
public final class TaskRepositoryMock implements TaskRepository {

    private Long actualIdArgument;
    private boolean deleteByIdCalled = false;
    private Long expectedIdArgument;
    private Task returnedTask;

    public TaskRepositoryMock() {
    }

    @Override
    public Task deleteById(Long id) {
        actualIdArgument = id;
        deleteByIdCalled = true;

        return returnedTask;
    }

    /**
     * Sets the id that should be passed as a method argument
     * when the {@code deleteById()} method is invoked.
     * @param expectedIdArgument    The expected id.
     */
    public void setExpectedIdArgument(Long expectedIdArgument) {
        this.expectedIdArgument = expectedIdArgument;
    }

    /**
     * Sets the {@code Task} object that is returned when the
     * {@code deleteById()} method is invoked.
     * @param returnedTask  The deleted task.
     */
    public void setReturnedTask(Task returnedTask) {
        this.returnedTask  = returnedTask;
    }

    /**
     * Verifies that the {@code deleteById()} method was invoked and the
     * correct id was passed as a method parameter.
     */
    public void verify() {
        assertThat(deleteByIdCalled)
                .overridingErrorMessage("Expected that deleteById() was called but it was not")
                .isTrue();

        assertThat(actualIdArgument)
                .overridingErrorMessage(
                        "Invalid id argument. Expected: %d but was: %d",
                        expectedIdArgument,
                        actualIdArgument
                )
                .isEqualByComparingTo(expectedIdArgument);
    }
}
