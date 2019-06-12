package com.testwithspring.master.kotlin.matchers.stubbing

import com.testwithspring.master.kotlin.*
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unitTest")
@DisplayName("Verify interactions when the function takes one simple object as a function parameter ")
class FindByIdMockExampleTest {

    companion object {

        private const val ID = 1L
    }

    private lateinit var repository: TaskRepository

    @BeforeEach
    fun createRepositoryStub() {
        repository = mockk<TaskRepository>(relaxed = true)
    }

    @Test
    @DisplayName("By default, simple function parameters are matched with eq()")
    fun shouldMatchSimpleFunctionParameterWithDefaultMatcher() {
        repository.findById(ID)

        verify { repository.findById(ID) }
    }

    @Test
    @DisplayName("Ensures that the function parameter is equal to the provided value")
    fun shouldMatchFunctionParameterWithEq() {
        repository.findById(ID)

        verify { repository.findById(eq(ID)) }
    }

    @Test
    @DisplayName("Ensures that the function parameter is not equal to the provided value")
    fun shouldMatchFunctionParameterWithNeq() {
        repository.findById(ID)

        verify { repository.findById(neq(-1L)) }
    }

    @Test
    @DisplayName("Ensures that the function parameter is less than the provided value")
    fun shouldMatchFunctionParameterWithLess() {
        repository.findById(ID)

        verify { repository.findById(less(ID + 1)) }
    }

    @Test
    @DisplayName("Ensures that the function parameter is less than or equal to the provided value")
    fun shouldMatchFunctionParameterWithLessAndEqual() {
        repository.findById(ID)

        verify { repository.findById(less(value = ID, andEquals = true)) }
    }

    @Test
    @DisplayName("Ensures that the function parameter is greater than the provided value")
    fun shouldMatchFunctionParameterWithMore() {
        repository.findById(ID)

        verify { repository.findById(more(ID - 1)) }
    }

    @Test
    @DisplayName("Ensures that the function parameter is greater than or equal to the provided value")
    fun shouldMatchFunctionParameterWithMoreAndEqual() {
        repository.findById(ID)

        verify { repository.findById(more(value = ID, andEquals = true)) }
    }

    @Test
    @DisplayName("Ensures that any object is given as a function parameter")
    fun shouldMatchFunctionParameterWithAny() {
        repository.findById(ID)

        verify { repository.findById(any()) }
    }

    @Test
    @DisplayName("Ensures that a Long object is given as a function parameter")
    fun shouldMatchFunctionParameterWithOfType() {
        repository.findById(ID)

        verify { repository.findById(ofType(Long::class)) }
    }

    @Test
    @DisplayName("Ensures that the function parameter fulfills the given predicate")
    fun shouldMatchFunctionParameterWithMatch() {
        repository.findById(ID)

        verify { repository.findById(match { it == ID }) }
    }
}