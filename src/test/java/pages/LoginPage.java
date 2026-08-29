package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

public class LoginPage extends BasePage {

    private final By usernameInput = By.name("username");
    private final By passwordInput = By.name("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By errorAlert = By.cssSelector(".oxd-alert-content-text");
    private final By requiredFieldMessages = By.cssSelector(".oxd-input-group__message");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public DashboardPage loginAs(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
        return new DashboardPage(driver);
    }

    /** Submits the form without asserting a successful login (used for negative cases). */
    public void submitLogin(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
    }

    public void clickLoginWithEmptyFields() {
        click(loginButton);
    }

    public String getErrorMessage() {
        return waitVisible(errorAlert).getText();
    }

    public List<String> getRequiredFieldMessages() {
        return waitAllVisible(requiredFieldMessages)
                .stream()
                .map(el -> el.getText().trim())
                .collect(Collectors.toList());
    }
}
