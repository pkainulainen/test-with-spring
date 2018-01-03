package com.testwithspring.master.web.task

import com.testwithspring.master.EndToEndTest
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
class CreateTaskSpec extends Specification {

    private static final DESCRIPTION = 'This is a new lesson'
    private static final TITLE = 'Write an extra lesson'

    @SeleniumWebDriver
    @Shared WebDriver browser

    CreateTaskPage createTaskPage

    def setup() {
        createTaskPage = new CreateTaskPage(browser)
    }

    def 'Create a new task by using valid information as a registered user'() {

        TaskPage shownPage

        given: 'A user is authenticated successfully'
        LoginPage loginPage = new LoginPage(browser).open()
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD)

        when: 'A user opens the create task page and submits the create task form by using valid information'
        def createTaskForm = createTaskPage.open().getForm()
        createTaskForm.typeTitle(TITLE)
        createTaskForm.typeDescription(DESCRIPTION)
        shownPage = createTaskForm.submitTaskForm()

        then: 'Should open the view task page'
        shownPage.isOpenWithUnknownTaskId()

        and: 'Should display the title of the created task'
        shownPage.getTaskTitle() == TITLE

        and: 'Should display the description of the created task'
        shownPage.getTaskDescription() == DESCRIPTION

        and: 'Should not display the assignee name'
        def lifecycleFields = shownPage.getTaskLifecycleFields()
        !lifecycleFields.isAssigneeNameVisible()

        and: 'Should display the name of the creator'
        lifecycleFields.getCreatorName() == JohnDoe.NAME

        and: 'Should display the name of the modifier'
        lifecycleFields.getModifierName() == JohnDoe.NAME

        and: 'Should not display the lifecycle fields of a closed task'
        !lifecycleFields.areClosedTaskFieldsVisible()
    }

    def cleanup() {
        new NavigationBar(browser).logUserOut()
    }
}
