package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class PimPage extends BasePage {

    // The autocomplete "Employee Name" filter field is the only input on this
    // page with this placeholder, which is more resilient than chaining
    // relative xpath off the <label>, whose exact wrapper nesting can change.
    private final By employeeNameInput = By.cssSelector("input[placeholder='Type for hints...']");
    private final By searchButton = By.cssSelector("button[type='submit']");
    private final By addEmployeeButton = By.xpath("//button[normalize-space()='Add']");
    private final By resultRows = By.cssSelector(".oxd-table-body .oxd-table-row");
    private final By resultEmployeeNameCells = By.cssSelector(".oxd-table-body .oxd-table-row .oxd-table-cell:nth-child(4)");
    private final By noRecordsFoundMessage = By.xpath("//span[text()='No Records Found']");

    public PimPage(WebDriver driver) {
        super(driver);
    }

    public void searchByEmployeeName(String name) {
        type(employeeNameInput, name);
        click(searchButton);
    }

    public boolean isEmployeeInResults(String name) {
        return waitAllVisible(resultRows)
                .stream()
                .anyMatch(row -> row.getText().contains(name));
    }

    public List<String> getResultEmployeeNames() {
        return driver.findElements(resultEmployeeNameCells)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public boolean isNoRecordsFoundDisplayed() {
        return waitVisible(noRecordsFoundMessage).isDisplayed();
    }

    public AddEmployeePage goToAddEmployee() {
        click(addEmployeeButton);
        return new AddEmployeePage(driver);
    }
}
