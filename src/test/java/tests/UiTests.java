package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.JsonDataProviderUtil;

import java.util.Arrays;
import java.util.List;

@Epic("OrangeHRM Automation")
@Feature("UI / Layout")
public class UiTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(UiTests.class);

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeEachTest() {
        String username = JsonDataProviderUtil.getField("validLogin", "username");
        String password = JsonDataProviderUtil.getField("validLogin", "password");
        new LoginPage(getDriver()).loginAs(username, password);
    }

    @Test(description = "TC10: Verify the OrangeHRM footer/branding link")
    @Step("Scroll to footer and verify OrangeHRM branding link")
    @Description("Scrolls to the footer, asserts it contains 'OrangeHRM, Inc', clicks the "
            + "branding link, and asserts the new tab URL contains orangehrm.com.")
    public void testFooterBrandingLink() {
        logger.info("TC10: Verifying footer branding and link");

        DashboardPage dashboardPage = new DashboardPage(getDriver());

        ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0, document.body.scrollHeight);");

        String footerText = dashboardPage.getFooterText();
        Assert.assertTrue(footerText.contains("OrangeHRM, Inc"),
                "Footer text should contain 'OrangeHRM, Inc' but was: " + footerText);

        String newTabUrl = dashboardPage.clickFooterBrandLinkAndGetNewTabUrl();
        Assert.assertTrue(newTabUrl.contains("orangehrm.com"),
                "Expected new tab URL to contain orangehrm.com, but was: " + newTabUrl);

        logger.info("TC10: Footer branding link verified successfully");
    }

    @Test(description = "TC11: Verify sidebar menu UI")
    @Step("Verify the sidebar menu contains all expected modules")
    @Description("Asserts the left sidebar menu contains: Admin, PIM, Leave, Time, "
            + "Recruitment, My Info, Performance, Dashboard, Directory.")
    public void testSidebarMenuItems() {
        logger.info("TC11: Verifying sidebar menu items");

        List<String> expectedItems = Arrays.asList(
                "Admin", "PIM", "Leave", "Time", "Recruitment",
                "My Info", "Performance", "Dashboard", "Directory");

        DashboardPage dashboardPage = new DashboardPage(getDriver());
        List<String> actualItems = dashboardPage.getSidebarMenuTexts();

        for (String expected : expectedItems) {
            Assert.assertTrue(actualItems.contains(expected),
                    "Sidebar menu should contain '" + expected + "' but found: " + actualItems);
        }

        logger.info("TC11: Sidebar menu items verified successfully");
    }
}
