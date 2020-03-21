package com.testwithspring.master.task

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import com.testwithspring.master.DatabaseIntegrationTest
import com.testwithspring.master.IdColumnReset
import com.testwithspring.master.Tasks
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@DatabaseIntegrationTest
@ExtendWith(SoftAssertionsExtension::class)
@DatabaseSetup(value = [
    "/com/testwithspring/master/users.xml",
    "/com/testwithspring/master/no-tasks-and-tags.xml"
])
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader::class)
class CreateNewTaskTest(
        @Autowired private val jdbcTemplate: NamedParameterJdbcTemplate,
        @Autowired private val repository: TaskRepository
) {

    companion object {

        private val NEXT_FREE_ID = 1L
    }

    private val idColumnReset = IdColumnReset(jdbcTemplate)

    private val INPUT = CreateTask(
            creator = Creator(Tasks.WriteLesson.CREATOR_ID),
            description = Tasks.WriteLesson.DESCRIPTION,
            status = Tasks.WriteLesson.STATUS,
            title = Tasks.WriteLesson.TITLE
    )

    @BeforeEach
    fun resetIdColumn() {
        idColumnReset.resetIdColumns("tasks")
    }

    @Test
    @DisplayName("Should insert a new task into the database")
    @ExpectedDatabase(value = "create-task-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    fun shouldInsertNewTaskIntoDatabase() {
        repository.create(INPUT)
    }

    @Test
    @DisplayName("Should return the information of the created task")
    fun shouldReturnInformationOfCreatedTask(assertions: SoftAssertions) {
        val created = repository.create(INPUT)

        assertions.assertThat(created.assignee)
                .`as`("assignee")
                .isNull()
        assertions.assertThat(created.closer)
                .`as`("closer")
                .isNull()
        assertions.assertThat(created.creator.id)
                .`as`("creatorId")
                .isEqualByComparingTo(Tasks.WriteLesson.CREATOR_ID)
        assertions.assertThat(created.description)
                .`as`("description")
                .isEqualTo(Tasks.WriteLesson.DESCRIPTION)
        assertions.assertThat(created.id)
                .`as`("id")
                .isEqualByComparingTo(NEXT_FREE_ID)
        assertions.assertThat(created.modifier.id)
                .`as`("modifierId")
                .isEqualByComparingTo(Tasks.WriteLesson.CREATOR_ID)
        assertions.assertThat(created.resolution)
                .`as`("resolution")
                .isNull()
        assertions.assertThat(created.status)
                .`as`("status")
                .isEqualTo(Tasks.WriteLesson.STATUS)
        assertions.assertThat(created.tags)
                .`as`("tags")
                .hasSize(0)
        assertions.assertThat(created.title)
                .`as`("title")
                .isEqualTo(Tasks.WriteLesson.TITLE)
    }
}