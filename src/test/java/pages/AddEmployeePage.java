package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddEmployeePage extends BasePage {

    private final By firstNameInput = By.cssSelector("input[name='firstName']");
    private final By lastNameInput = By.cssSelector("input[name='lastName']");
    private final By saveButton = By.xpath("//button[normalize-space()='Save']");
    private final By requiredFieldMessage = By.cssSelector(".oxd-input-group__message");

    public AddEmployeePage(WebDriver driver) {
        super(driver);
    }

    public boolean isFirstNameFieldDisplayed() {
        return waitVisible(firstNameInput).isDisplayed();
    }

    public boolean isLastNameFieldDisplayed() {
        return waitVisible(lastNameInput).isDisplayed();
    }

    public void enterLastNameOnly(String lastName) {
        type(lastNameInput, lastName);
    }

    public void clickSave() {
        click(saveButton);
    }

    public String getRequiredFieldMessage() {
        return waitVisible(requiredFieldMessage).getText();
    }

    /** Fills first + last name, saves, and waits for the redirect to Personal Details. */
    public void addEmployee(String firstName, String lastName) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        click(saveButton);
        // The save click triggers an async navigation; without this wait the
        // test can read the URL before the redirect completes.
        waitUrlContains("/pim/viewPersonalDetails/");
    }
}
