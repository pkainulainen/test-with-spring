package com.testwithspring.master.kotlin.mocking

import com.testwithspring.master.kotlin.*
import com.testwithspring.master.kotlin.matchers.mock.CreateMockExampleTest
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unitTest")
@DisplayName("Demonstrates how we verify the interactions which happend between SUT and a mock")
class TaskRepositoryMockTest {

    companion object {

        private const val CREATOR_ID = 5L
        private const val TASK_ID = 1L
        private const val TITLE = "Example task"
    }

    private lateinit var repository: TaskRepository

    @BeforeEach
    fun createStrictMock() {
        repository = mockk(relaxed = true)
    }

    @Test
    @DisplayName("Should verify that the create() function was not invoked")
    fun shouldVerifyThatCreateFunctionWasNotInvoked() {
        repository.findById(TASK_ID)

        verify(exactly = 0) { repository.create(any()) }
    }

    @Test
    @DisplayName("Should verify that findById() function was invoked once")
    fun shouldVerifyThatFindByIdFunctionWasInvokedOnce() {
        repository.findById(TASK_ID)

        verify { repository.findById(TASK_ID) }
    }

    @Test
    @DisplayName("Should verify that the findById() function has only one invocation")
    fun shouldVerifyThatFindByIdFunctionHasOnlyOneInvocation() {
        repository.findById(TASK_ID)

        verify { repository.findById(TASK_ID) }

        //This helps us to verify that unverified invocations didn't happen between
        //the SUT and our mock. In other words, if we haven't verified every
        //interaction between the SUT and our mock, our test will fail.
        confirmVerified(repository)
    }

    @Test
    @DisplayName("Should verify invocations but ignore order")
    fun shouldVerifyInvocationsButIgnoreOrder() {
        repository.findById(5L)
        repository.findById(TASK_ID)

        verifyAll {
            repository.findById(TASK_ID)
            repository.findById(5L)
        }
    }

    @Test
    @DisplayName("Should verify a sequence of function invocations")
    fun shouldVerifySequenceOfFunctionInvocations() {
        repository.findById(5L)
        repository.findById(TASK_ID)
        repository.findById(99L)

        verifySequence {
            repository.findById(5L)
            repository.findById(TASK_ID)
            repository.findById(99L)
        }
    }

    @Test
    @DisplayName("Should verify the order of function invocations")
    fun shouldVerifyOrderOfFunctionInvocations() {
        repository.findById(5L)
        repository.findById(TASK_ID)
        repository.findById(99L)

        verifyOrder {
            repository.findById(5L)
            repository.findById(99L)
        }
    }

    @Test
    @DisplayName("Should use a simple argument matcher")
    fun shouldUseSimpleArgumentMatchers() {
        repository.findByCreatorAndStatus(1L, TaskStatus.OPEN)

        verify { repository.findByCreatorAndStatus(any(), TaskStatus.OPEN) }
    }

    @Test
    @DisplayName("Should use a complex argument matcher")
    fun shouldUseComplexArgumentMatcher() {
        repository.create( Task(
                id = TASK_ID,
                creator = Creator(id = CREATOR_ID),
                title = TITLE,
                status = TaskStatus.OPEN,
                resolution = null
        ))

        verify { repository.create(match { it.id == TASK_ID })}
    }
}