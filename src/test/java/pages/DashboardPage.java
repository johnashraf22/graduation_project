package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DashboardPage extends BasePage {

    private final By dashboardHeader = By.xpath("//h6[text()='Dashboard']");
    private final By sidebarMenuItems = By.cssSelector(".oxd-main-menu-item-wrapper .oxd-text");
    private final By pimMenuLink = By.xpath("//span[text()='PIM']");
    private final By adminMenuLink = By.xpath("//span[text()='Admin']");
    // Narrowed to "Inc" specifically: a broader 'OrangeHRM' match also
    // catches the "OrangeHRM OS 5.9" version tag elsewhere on the page,
    // which appears earlier in the DOM and was being matched first.
    private final By footerText = By.xpath("//p[contains(.,'Inc')]");
    private final By footerBrandLink = By.xpath("//p[contains(.,'Inc')]//a");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDashboardHeaderDisplayed() {
        return waitVisible(dashboardHeader).isDisplayed();
    }

    public List<String> getSidebarMenuTexts() {
        return waitAllVisible(sidebarMenuItems)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public PimPage goToPim() {
        click(pimMenuLink);
        return new PimPage(driver);
    }

    public AdminUsersPage goToAdmin() {
        click(adminMenuLink);
        return new AdminUsersPage(driver);
    }

    public String getFooterText() {
        return waitVisible(footerText).getText();
    }

    /** Clicks the branding link in the footer and switches to the newly opened tab. Returns its URL. */
    public String clickFooterBrandLinkAndGetNewTabUrl() {
        String originalWindow = driver.getWindowHandle();
        click(footerBrandLink);

        wait.until(d -> d.getWindowHandles().size() > 1);
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        wait.until(d -> driver.getCurrentUrl().contains("orangehrm.com"));
        String newTabUrl = driver.getCurrentUrl();

        driver.close();
        driver.switchTo().window(originalWindow);
        return newTabUrl;
    }
}
