package com.testwithspring.master.kotlin.matchers.stubbing

import com.testwithspring.master.kotlin.*
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unitTest")
@DisplayName("Stub functions which take one simple value as a function parameter")
class FindByIdStubExampleTest {

    companion object {

        private const val ID = 1L
        private const val CREATOR_ID = 5L
        private const val TITLE = "Write stub example"
        private val STATUS = TaskStatus.CLOSED
        private val RESOLUTION = TaskResolution.DONE

        private val TASK = Task(
                id = ID,
                creator = Creator(id = CREATOR_ID),
                title = TITLE,
                status = STATUS,
                resolution = RESOLUTION
        )
    }

    private lateinit var repository: TaskRepository

    @BeforeEach
    fun createRepositoryStub() {
        repository = mockk<TaskRepository>()
    }

    @Test
    @DisplayName("By default, simple function parameters are matched with eq()")
    fun shouldMatchSimpleFunctionParameterWithDefaultMatcher() {
        every { repository.findById(ID) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that the function parameter is equal to the provided value")
    fun shouldMatchFunctionParameterWithEq() {
        every { repository.findById(eq(ID)) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that the function parameter is not equal to the provided value")
    fun shouldMatchFunctionParameterWithNeq() {
        every { repository.findById(neq(-1L)) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that the function parameter is less than the provided value")
    fun shouldMatchFunctionParameterWithLess() {
        every { repository.findById(less(ID + 1)) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that the function parameter is less than or equal to the provided value")
    fun shouldMatchFunctionParameterWithLessAndEqual() {
        every { repository.findById(less(value = ID, andEquals = true)) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that the function parameter is greater than the provided value")
    fun shouldMatchFunctionParameterWithMore() {
        every { repository.findById(more(ID - 1)) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that the function parameter is greater than or equal to the provided value")
    fun shouldMatchFunctionParameterWithMoreAndEqual() {
        every { repository.findById(more(value = ID, andEquals = true)) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that any object is given as a function parameter")
    fun shouldMatchFunctionParameterWithAny() {
        every { repository.findById(any()) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that a Long object is given as a function parameter")
    fun shouldMatchFunctionParameterWithOfType() {
        every { repository.findById(ofType(Long::class)) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that the function parameter fulfills the given predicate")
    fun shouldMatchFunctionParameterWithMatch() {
        every { repository.findById(match { it == ID }) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }
}