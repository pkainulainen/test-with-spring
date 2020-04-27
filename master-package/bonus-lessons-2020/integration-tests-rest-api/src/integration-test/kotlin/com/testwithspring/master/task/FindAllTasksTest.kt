package com.testwithspring.master.task

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import com.testwithspring.master.DatabaseIntegrationTest
import com.testwithspring.master.Tasks
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired

@DatabaseIntegrationTest
@DisplayName("Find all tasks")
class FindAllTasksTest(@Autowired private val repository: TaskRepository) {

    @Nested
    @DatabaseIntegrationTest
    @DatabaseSetup(value = [
        "/com/testwithspring/master/users.xml",
        "/com/testwithspring/master/no-tasks-and-tags.xml"
    ])
    @DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader::class)
    @DisplayName("When no tasks are found")
    inner class WhenNoTasksAreFound {

        @Test
        @DisplayName("Should return an empty list")
        fun shouldReturnEmptyList() {
            val tasks = repository.findAll()
            assertThat(tasks).isEmpty()
        }
    }

    @Nested
    @DatabaseIntegrationTest
    @ExtendWith(SoftAssertionsExtension::class)
    @DatabaseSetup(value = [
        "/com/testwithspring/master/users.xml",
        "/com/testwithspring/master/tasks.xml"
    ])
    @DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader::class)
    @DisplayName("When two tasks are found")
    inner class WhenTwoTasksAreFound {

        @Test
        @DisplayName("Should return two tasks")
        fun shouldReturnTwoTasks() {
            val tasks = repository.findAll()
            assertThat(tasks).hasSize(2)
        }

        @Test
        @DisplayName("Should return the oldest task as the first task of the returned list")
        fun shouldReturnOldestTaskAsFirstTaskOfReturnedList(assertions: SoftAssertions) {
            val firstTask = repository.findAll()[0]

            assertions.assertThat(firstTask.id)
                    .`as`("id")
                    .isEqualByComparingTo(Tasks.WriteExampleApplication.ID)
            assertions.assertThat(firstTask.title)
                    .`as`("title")
                    .isEqualTo(Tasks.WriteExampleApplication.TITLE)
            assertions.assertThat(firstTask.status)
                    .`as`("status")
                    .isEqualTo(Tasks.WriteExampleApplication.STATUS)
        }

        @Test
        @DisplayName("Should return the newest task as the second task of the returned list")
        fun shouldReturnNewestTaskAsSecondTaskOfReturnedList(assertions: SoftAssertions) {
            val secondTask = repository.findAll()[1]

            assertions.assertThat(secondTask.id)
                    .`as`("id")
                    .isEqualByComparingTo(Tasks.WriteLesson.ID)
            assertions.assertThat(secondTask.title)
                    .`as`("title")
                    .isEqualTo(Tasks.WriteLesson.TITLE)
            assertions.assertThat(secondTask.status)
                    .`as`("status")
                    .isEqualTo(Tasks.WriteLesson.STATUS)
        }
    }
}