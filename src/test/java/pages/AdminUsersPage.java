package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdminUsersPage extends BasePage {

    private final By addButton = By.xpath("//button[normalize-space()='Add']");
    // Using the `following::` axis (rather than a fixed parent/grandparent
    // chain) is resilient to the exact wrapper nesting under each <label>,
    // which is what caused the original locators to time out.
    private final By userRoleDropdown =
            By.xpath("//label[text()='User Role']/following::div[contains(@class,'oxd-select-text')][1]");
    private final By employeeNameInput = By.cssSelector("input[placeholder='Type for hints...']");
    private final By usernameInput =
            By.xpath("//label[text()='Username']/following::input[1]");
    private final By passwordInput = By.cssSelector("input[type='password']");

    public AdminUsersPage(WebDriver driver) {
        super(driver);
    }

    public void clickAdd() {
        click(addButton);
    }

    public boolean isUserRoleFieldDisplayed() {
        return waitVisible(userRoleDropdown).isDisplayed();
    }

    public boolean isEmployeeNameFieldDisplayed() {
        return waitVisible(employeeNameInput).isDisplayed();
    }

    public boolean isUsernameFieldDisplayed() {
        return waitVisible(usernameInput).isDisplayed();
    }

    public boolean isPasswordFieldDisplayed() {
        return waitVisible(passwordInput).isDisplayed();
    }
}
