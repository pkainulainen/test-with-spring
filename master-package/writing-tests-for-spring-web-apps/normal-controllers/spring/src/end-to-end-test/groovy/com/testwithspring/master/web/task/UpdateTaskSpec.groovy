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
class UpdateTaskSpec extends Specification {

    private static final NEW_DESCRIPTION = 'The old lesson was not good'
    private static final NEW_TITLE = 'Rewrite an existing lesson'

    @SeleniumWebDriver
    @Shared WebDriver browser

    UpdateTaskPage updateTaskPage

    def setup() {
        updateTaskPage = new UpdateTaskPage(browser, EndToEndTestTasks.WriteLesson.ID)
    }

    def 'Update the information of an existing task by using valid information as a registered user'() {

        TaskPage shownPage

        given: 'A user is authenticated successfully'
        LoginPage loginPage = new LoginPage(browser).open()
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD)

        when: 'A user opens the update task page and submits the update task form by using valid information'
        def updateTaskForm = updateTaskPage.open().getForm()
        updateTaskForm.typeTitle(NEW_TITLE)
        updateTaskForm.typeDescription(NEW_DESCRIPTION)
        shownPage = updateTaskForm.submitTaskForm()

        then: 'Should open the view task page'
        shownPage.isOpen()

        and: 'Should display the title of the updated task'
        shownPage.getTaskTitle() == NEW_TITLE

        and: 'Should display the description of the updated task'
        shownPage.getTaskDescription() == NEW_DESCRIPTION

        and: 'Should not display the assignee name'
        def lifecycleFields = shownPage.getTaskLifecycleFields()
        !lifecycleFields.isAssigneeNameVisible()

        and: 'Should display the name of the original creator'
        lifecycleFields.getCreatorName() == EndToEndTestTasks.WriteLesson.Creator.NAME

        and: 'Should display the updated name of the modifier'
        lifecycleFields.getModifierName() == JohnDoe.NAME

        and: 'Should not display the lifecycle fields of a closed task'
        !lifecycleFields.areClosedTaskFieldsVisible()
    }

    def cleanup() {
        new NavigationBar(browser).logUserOut()
    }
}
