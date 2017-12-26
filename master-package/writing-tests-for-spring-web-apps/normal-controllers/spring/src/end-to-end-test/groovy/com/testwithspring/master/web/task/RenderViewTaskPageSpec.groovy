package com.testwithspring.master.web.task

import com.testwithspring.master.EndToEndTest
import com.testwithspring.master.EndToEndTestTasks
import com.testwithspring.master.SeleniumTest
import com.testwithspring.master.SeleniumWebDriver
import com.testwithspring.master.web.NavigationBar
import com.testwithspring.master.web.login.LoginPage
import org.junit.experimental.categories.Category
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import spock.lang.Shared
import spock.lang.Specification

import static com.testwithspring.master.EndToEndTestUsers.JohnDoe

@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
class RenderViewTaskPageSpec extends Specification {

    @SeleniumWebDriver
    @Shared WebDriver browser

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
