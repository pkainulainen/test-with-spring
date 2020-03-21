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
@ExtendWith(SoftAssertionsExtension::class)
@DatabaseSetup(value = [
    "/com/testwithspring/master/users.xml",
    "/com/testwithspring/master/tasks.xml"
])
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader::class)
class FindTaskByIdTest(@Autowired private val repository: TaskRepository) {

    @Nested
    @DatabaseIntegrationTest
    @DatabaseSetup(value = [
        "/com/testwithspring/master/users.xml",
        "/com/testwithspring/master/tasks.xml"
    ])
    @DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader::class)
    inner class WhenNoTaskIsFound {

        @Test
        @DisplayName("Should return null")
        fun shouldReturnNull() {
            val found = repository.findById(Tasks.ID_NOT_FOUND)
            assertThat(found).isNull()
        }
    }

    @Nested
    @DatabaseIntegrationTest
    @DatabaseSetup(value = [
        "/com/testwithspring/master/users.xml",
        "/com/testwithspring/master/tasks.xml"
    ])
    @DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader::class)
    inner class WhenTaskIsFound {

        @Test
        @DisplayName("Should return the information of the correct task when the task is found")
        fun shouldReturnCorrectInformationOfTaskWhenTaskIsFound(assertions: SoftAssertions) {
            val found = repository.findById(Tasks.WriteExampleApplication.ID)

            assertions.assertThat(found?.id)
                    .`as`("id")
                    .isEqualByComparingTo(Tasks.WriteExampleApplication.ID)
            assertions.assertThat(found?.assignee?.id)
                    .`as`("assignee.id")
                    .isEqualByComparingTo(Tasks.WriteExampleApplication.ASSIGNEE_ID)
            assertions.assertThat(found?.closer?.id)
                    .`as`("closer.id")
                    .isEqualByComparingTo(Tasks.WriteExampleApplication.CLOSER_ID)
            assertions.assertThat(found?.creator?.id)
                    .`as`("creator.id")
                    .isEqualByComparingTo(Tasks.WriteExampleApplication.CREATOR_ID)
            assertions.assertThat(found?.description)
                    .`as`("description")
                    .isEqualTo(Tasks.WriteExampleApplication.DESCRIPTION)
            assertions.assertThat(found?.modifier?.id)
                    .`as`("modifier.id")
                    .isEqualByComparingTo(Tasks.WriteExampleApplication.MODIFIER_ID)
            assertions.assertThat(found?.resolution)
                    .`as`("resolution")
                    .isEqualTo(Tasks.WriteExampleApplication.RESOLUTION)
            assertions.assertThat(found?.status)
                    .`as`("status")
                    .isEqualTo(Tasks.WriteExampleApplication.STATUS)
            assertions.assertThat(found?.title)
                    .`as`("title")
                    .isEqualTo(Tasks.WriteExampleApplication.TITLE)
        }

        @Test
        @DisplayName("Should return a task that has one tag")
        fun shouldReturnTaskThatHasOneTag() {
            val found = repository.findById(Tasks.WriteExampleApplication.ID)

            assertThat(found?.tags)
                    .`as`("tags")
                    .hasSize(1)
        }

        @Test
        @DisplayName("Should return the information of the correct tag")
        fun shouldReturnInformationOfCorrectTag(assertions: SoftAssertions) {
            val tag = repository.findById(Tasks.WriteExampleApplication.ID)?.tags?.get(0)

            assertions.assertThat(tag?.id)
                    .`as`("tagId")
                    .isEqualByComparingTo(Tasks.WriteExampleApplication.Tags.Example.ID)
            assertions.assertThat(tag?.name)
                    .`as`("tagName")
                    .isEqualTo(Tasks.WriteExampleApplication.Tags.Example.NAME)
        }
    }
}