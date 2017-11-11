package com.testwithspring.master.web.task

import com.testwithspring.master.EndToEndTest
import com.testwithspring.master.EndToEndTestTasks
import com.testwithspring.master.NavigationBar
import com.testwithspring.master.SeleniumTest
import com.testwithspring.master.TaskTrackerApplication
import com.testwithspring.master.config.Profiles
import com.testwithspring.master.web.login.LoginPage
import org.junit.experimental.categories.Category
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import spock.lang.Specification

import static com.testwithspring.master.EndToEndTestUsers.*
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD

@SpringBootTest(classes = TaskTrackerApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@SeleniumTest(driver = ChromeDriver.class)
@SqlGroup([
        @Sql(value = [
                'classpath:/com/testwithspring/master/users.sql',
                'classpath:/com/testwithspring/master/tasks.sql'
        ]),
        @Sql(
                value = 'classpath:/com/testwithspring/master/cleandb.sql',
                executionPhase = AFTER_TEST_METHOD
        )
])
@ActiveProfiles(Profiles.END_TO_END_TEST)
@Category(EndToEndTest.class)
class RenderViewTaskPageSpec extends Specification {

    @Autowired
    WebDriver browser

    TaskPage viewTaskPage

    def setup() {
        viewTaskPage = new TaskPage(browser, EndToEndTestTasks.WriteExampleApp.ID)
    }

    def 'Open view task page as a registered user'() {

        TaskPage shownPage

        given: 'A user is authenticated successfully'
        LoginPage loginPage = new LoginPage(browser).open()
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD)

        when: 'A user opens the view task page of a closed task'
        shownPage = viewTaskPage.open()

        then: 'Should open the view task page'
        shownPage.isOpen()

        and: 'Should display the title of the shown task'
        shownPage.getTaskTitle() == EndToEndTestTasks.WriteExampleApp.TITLE

        and: 'Should display the description of the shown task'
        shownPage.getTaskDescription() == EndToEndTestTasks.WriteExampleApp.DESCRIPTION

        and: 'Should display the assignee information of the shown task'
        def lifecycleFields = shownPage.getTaskLifecycleFields()
        lifecycleFields.isAssigneeNameVisible()

        and: 'Should display the name of the assignee'
        lifecycleFields.getAssigneeName() == EndToEndTestTasks.WriteExampleApp.Assignee.NAME

        and: 'Should display the name of the creator'
        lifecycleFields.getCreatorName() == EndToEndTestTasks.WriteExampleApp.Creator.NAME

        and: 'Should display the name of the modifier'
        lifecycleFields.getModifierName() == EndToEndTestTasks.WriteExampleApp.Modifier.NAME

        and: 'Should display the lifecycle fields of a closed task'
        lifecycleFields.areClosedTaskFieldsVisible()

        and: 'Should display the name of the closer'
        lifecycleFields.getCloserName() == EndToEndTestTasks.WriteExampleApp.Closer.NAME

        and: 'A user must be able to navigate to the update task page'
        def actions = shownPage.getTaskActions()
        UpdateTaskPage updateTaskPage = actions.updateTask()
        updateTaskPage.isOpen()
    }

    def cleanup() {
        new NavigationBar(browser).logUserOut()
    }
}
