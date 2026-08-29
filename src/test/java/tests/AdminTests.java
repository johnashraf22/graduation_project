package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AdminUsersPage;
import pages.DashboardPage;
import pages.LoginPage;
import utils.JsonDataProviderUtil;

@Epic("OrangeHRM Automation")
@Feature("Admin Module")
public class AdminTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(AdminTests.class);

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeEachTest() {
        String username = JsonDataProviderUtil.getField("validLogin", "username");
        String password = JsonDataProviderUtil.getField("validLogin", "password");
        new LoginPage(getDriver()).loginAs(username, password);
    }

    @Test(description = "TC9: Verify Admin > Add User page fields")
    @Step("Navigate to Admin > User Management > Users and open the Add User form")
    @Description("Navigates to Admin, clicks Add, and asserts the Add User form contains "
            + "the User Role, Employee Name, Username, and Password fields.")
    public void testAddUserPageFields() {
        logger.info("TC9: Verifying Admin > Add User page fields");

        DashboardPage dashboardPage = new DashboardPage(getDriver());
        AdminUsersPage adminUsersPage = dashboardPage.goToAdmin();
        adminUsersPage.clickAdd();

        Assert.assertTrue(adminUsersPage.isUserRoleFieldDisplayed(), "User Role field should be displayed");
        Assert.assertTrue(adminUsersPage.isEmployeeNameFieldDisplayed(), "Employee Name field should be displayed");
        Assert.assertTrue(adminUsersPage.isUsernameFieldDisplayed(), "Username field should be displayed");
        Assert.assertTrue(adminUsersPage.isPasswordFieldDisplayed(), "Password field should be displayed");

        logger.info("TC9: Add User page fields verified successfully");
    }
}
