package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;

/**
 * Base class for all test classes.
 *
 * Uses a ThreadLocal<WebDriver> so each TestNG thread (required for
 * parallel="tests" execution in testng.xml) gets its own independent
 * WebDriver instance instead of sharing one across threads.
 */
public class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /** Package/subclass-visible accessor for the current thread's driver. */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        String browser = ConfigReader.getBrowser();
        logger.info("Setting up '{}' driver on thread {}", browser, Thread.currentThread().getId());

        WebDriver driver;
        if ("chrome".equalsIgnoreCase(browser)) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--window-size=1920,1080");
            if (ConfigReader.isHeadless()) {
                options.addArguments("--headless=new");
            }
            driver = new ChromeDriver(options);
        } else {
            throw new IllegalArgumentException("Unsupported browser configured: " + browser);
        }

        driver.manage().window().maximize();
        driverThreadLocal.set(driver);

        logger.info("Navigating to base URL: {}", ConfigReader.getBaseUrl());
        getDriver().get(ConfigReader.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            logger.info("Tearing down driver on thread {}", Thread.currentThread().getId());
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
