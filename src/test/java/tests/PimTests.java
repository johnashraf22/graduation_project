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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.AddEmployeePage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.PimPage;
import utils.JsonDataProviderUtil;

import java.util.List;
import java.util.Map;

@Epic("OrangeHRM Automation")
@Feature("PIM Module")
public class PimTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(PimTests.class);

    /**
     * Logs in before every test in this class. TestNG always runs inherited
     * (superclass) @BeforeMethod configuration methods - here BaseTest.setUp(),
     * which creates the driver and opens the base URL - before methods declared
     * in the subclass, so the driver is guaranteed to be ready at this point.
     */
    @BeforeMethod(alwaysRun = true)
    public void loginBeforeEachTest() {
        String username = JsonDataProviderUtil.getField("validLogin", "username");
        String password = JsonDataProviderUtil.getField("validLogin", "password");
        new LoginPage(getDriver()).loginAs(username, password);
    }

    @DataProvider(name = "existingEmployeeData")
    public Object[][] existingEmployeeData() {
        return toSingleColumn(JsonDataProviderUtil.getRecords("existingEmployee"), "employeeName");
    }

    @DataProvider(name = "nonExistingEmployeeData")
    public Object[][] nonExistingEmployeeData() {
        return toSingleColumn(JsonDataProviderUtil.getRecords("nonExistingEmployee"), "employeeName");
    }

    @DataProvider(name = "newEmployeeData")
    public Object[][] newEmployeeData() {
        List<Map<String, String>> records = JsonDataProviderUtil.getRecords("newEmployee");
        Object[][] data = new Object[records.size()][2];
        for (int i = 0; i < records.size(); i++) {
            data[i][0] = records.get(i).get("firstName");
            data[i][1] = records.get(i).get("lastName");
        }
        return data;
    }

    private Object[][] toSingleColumn(List<Map<String, String>> records, String field) {
        Object[][] data = new Object[records.size()][1];
        for (int i = 0; i < records.size(); i++) {
            data[i][0] = records.get(i).get(field);
        }
        return data;
    }

    @Test(dataProvider = "existingEmployeeData",
            description = "TC4: Search for an existing employee in PIM")
    @Step("Search PIM Employee List for an existing employee")
    @Description("Navigates to PIM > Employee List, searches by a known employee name, "
            + "and asserts the results table contains that name.")
    public void testSearchExistingEmployee(String employeeName) {
        logger.info("TC4: Searching for existing employee '{}'", employeeName);

        DashboardPage dashboardPage = new DashboardPage(getDriver());
        PimPage pimPage = dashboardPage.goToPim();
        pimPage.searchByEmployeeName(employeeName);

        Assert.assertTrue(pimPage.isEmployeeInResults(employeeName),
                "Expected '" + employeeName + "' to appear in the search results. "
                        + "Note: demo data may change over time - update testdata.json if this fails.");

        logger.info("TC4: Existing employee search verified successfully");
    }

    @Test(dataProvider = "nonExistingEmployeeData",
            description = "TC5: Search for a non-existing employee in PIM")
    @Step("Search PIM Employee List for a non-existing employee")
    @Description("Searches the Employee List for a name that should not exist and asserts "
            + "the 'No Records Found' message is displayed.")
    public void testSearchNonExistingEmployee(String employeeName) {
        logger.info("TC5: Searching for non-existing employee '{}'", employeeName);

        DashboardPage dashboardPage = new DashboardPage(getDriver());
        PimPage pimPage = dashboardPage.goToPim();
        pimPage.searchByEmployeeName(employeeName);

        Assert.assertTrue(pimPage.isNoRecordsFoundDisplayed(),
                "Expected 'No Records Found' message for a non-existing employee search");

        logger.info("TC5: Non-existing employee search verified successfully");
    }

    @Test(description = "TC6: Open Add Employee page")
    @Step("Navigate to PIM > Add Employee")
    @Description("Clicks Add Employee from the PIM menu and asserts the URL contains "
            + "/pim/addEmployee with the First Name and Last Name fields visible.")
    public void testOpenAddEmployeePage() {
        logger.info("TC6: Opening Add Employee page");

        DashboardPage dashboardPage = new DashboardPage(getDriver());
        PimPage pimPage = dashboardPage.goToPim();
        AddEmployeePage addEmployeePage = pimPage.goToAddEmployee();

        Assert.assertTrue(addEmployeePage.getCurrentUrl().contains("/pim/addEmployee"),
                "URL should contain /pim/addEmployee");
        Assert.assertTrue(addEmployeePage.isFirstNameFieldDisplayed(), "First Name field should be displayed");
        Assert.assertTrue(addEmployeePage.isLastNameFieldDisplayed(), "Last Name field should be displayed");

        logger.info("TC6: Add Employee page verified successfully");
    }

    @Test(description = "TC7: Add employee with empty required First Name field")
    @Step("Submit Add Employee form with First Name left empty")
    @Description("Enters only the Last Name and clicks Save, asserting the 'Required' "
            + "validation error appears under First Name.")
    public void testAddEmployeeWithEmptyFirstName() {
        logger.info("TC7: Submitting Add Employee form with empty First Name");

        DashboardPage dashboardPage = new DashboardPage(getDriver());
        PimPage pimPage = dashboardPage.goToPim();
        AddEmployeePage addEmployeePage = pimPage.goToAddEmployee();

        addEmployeePage.enterLastNameOnly("Smith");
        addEmployeePage.clickSave();

        Assert.assertEquals(addEmployeePage.getRequiredFieldMessage(), "Required",
                "Expected 'Required' validation message under First Name");

        logger.info("TC7: Empty First Name validation verified successfully");
    }

    @Test(dataProvider = "newEmployeeData",
            description = "TC8: End-to-end - add a new employee successfully")
    @Step("Add a new employee end-to-end and verify it appears in Employee List")
    @Description("Adds a new employee via PIM > Add Employee, confirms the Personal Details "
            + "page opens, then searches the Employee List and asserts the employee appears.")
    public void testAddNewEmployeeEndToEnd(String firstName, String lastName) {
        // Make the name unique per run so repeated executions don't collide.
        String uniqueLastName = lastName + System.currentTimeMillis() % 100000;
        logger.info("TC8: Adding new employee '{} {}'", firstName, uniqueLastName);

        DashboardPage dashboardPage = new DashboardPage(getDriver());
        PimPage pimPage = dashboardPage.goToPim();
        AddEmployeePage addEmployeePage = pimPage.goToAddEmployee();

        addEmployeePage.addEmployee(firstName, uniqueLastName);

        Assert.assertTrue(addEmployeePage.getCurrentUrl().contains("/pim/viewPersonalDetails/"),
                "Expected to land on the Personal Details page for the new employee");

        // Navigate back to Employee List via the UI (not a hardcoded URL —
        // that's what caused this to fail) and confirm the new employee is searchable.
        DashboardPage dashboardPageAgain = new DashboardPage(getDriver());
        PimPage pimPageAgain = dashboardPageAgain.goToPim();
        pimPageAgain.searchByEmployeeName(firstName + " " + uniqueLastName);

        Assert.assertTrue(pimPageAgain.isEmployeeInResults(firstName),
                "Newly added employee should appear in the Employee List search results");

        logger.info("TC8: End-to-end employee creation verified successfully");
    }
}
