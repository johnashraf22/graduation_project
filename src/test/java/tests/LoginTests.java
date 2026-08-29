package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.JsonDataProviderUtil;

import java.util.List;
import java.util.Map;

@Epic("OrangeHRM Automation")
@Feature("Authentication")
public class LoginTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(LoginTests.class);

    @DataProvider(name = "validLoginData")
    public Object[][] validLoginData() {
        List<Map<String, String>> records = JsonDataProviderUtil.getRecords("validLogin");
        Object[][] data = new Object[records.size()][2];
        for (int i = 0; i < records.size(); i++) {
            data[i][0] = records.get(i).get("username");
            data[i][1] = records.get(i).get("password");
        }
        return data;
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        List<Map<String, String>> records = JsonDataProviderUtil.getRecords("invalidLogin");
        Object[][] data = new Object[records.size()][2];
        for (int i = 0; i < records.size(); i++) {
            data[i][0] = records.get(i).get("username");
            data[i][1] = records.get(i).get("password");
        }
        return data;
    }

    @Test(dataProvider = "validLoginData",
            description = "TC1: Login with valid credentials")
    @Step("Login with valid credentials and verify Dashboard is displayed")
    @Description("Enters valid username/password, submits the login form, and asserts the "
            + "user lands on /dashboard/index with the Dashboard header visible.")
    public void testLoginWithValidCredentials(String username, String password) {
        logger.info("TC1: Logging in with valid credentials for user '{}'", username);

        LoginPage loginPage = new LoginPage(getDriver());
        DashboardPage dashboardPage = loginPage.loginAs(username, password);

        Assert.assertTrue(dashboardPage.getCurrentUrl().contains("/dashboard/index"),
                "URL should contain /dashboard/index after a valid login");
        Assert.assertTrue(dashboardPage.isDashboardHeaderDisplayed(),
                "Dashboard header should be displayed after a valid login");

        logger.info("TC1: Valid login verified successfully");
    }

    @Test(dataProvider = "invalidLoginData",
            description = "TC2: Login with invalid credentials")
    @Step("Login with invalid credentials and verify the error message")
    @Description("Submits an invalid username/password combination and asserts the "
            + "'Invalid credentials' alert is displayed.")
    public void testLoginWithInvalidCredentials(String username, String password) {
        logger.info("TC2: Attempting login with invalid credentials for user '{}'", username);

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.submitLogin(username, password);

        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Invalid credentials"),
                "Expected 'Invalid credentials' error, but got: " + errorMessage);

        logger.info("TC2: Invalid credentials error verified successfully");
    }

    @Test(description = "TC3: Login with empty fields shows Required validation")
    @Step("Submit the login form with empty username and password")
    @Description("Clicks Login without entering any credentials and asserts that "
            + "'Required' validation messages appear under both fields.")
    public void testLoginWithEmptyFields() {
        logger.info("TC3: Submitting login form with empty username and password");

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.clickLoginWithEmptyFields();

        List<String> requiredMessages = loginPage.getRequiredFieldMessages();
        Assert.assertEquals(requiredMessages.size(), 2,
                "Expected two 'Required' validation messages (username and password)");
        requiredMessages.forEach(msg ->
                Assert.assertEquals(msg, "Required", "Validation message should read 'Required'"));

        logger.info("TC3: Empty-field validation verified successfully");
    }
}
