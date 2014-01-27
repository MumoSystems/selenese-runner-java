package jp.vmi.selenium.webdriver;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import jp.vmi.selenium.selenese.utils.PathUtils;

import static jp.vmi.selenium.webdriver.DriverOptions.DriverOption.*;

/**
 * Factory of {@link ChromeDriver}.
 *
 * @see <a href="http://code.google.com/p/chromedriver/">chromedriver - WebDriver for Google Chrome</a>
 */
public class ChromeDriverFactory extends WebDriverFactory {

    @Override
    public WebDriver newInstance(DriverOptions driverOptions) {
        File driver;
        if (driverOptions.has(CHROMEDRIVER)) {
            driver = new File(driverOptions.get(CHROMEDRIVER));
            if (!driver.canExecute())
                throw new IllegalArgumentException("Missing ChromeDriver: " + driver);
        } else {
            driver = PathUtils.searchExecutableFile("chromedriver");
            if (driver == null)
                throw new IllegalStateException("Missing ChromeDriver in PATH");
        }
        ChromeDriverService service = new ChromeDriverService.Builder()
            .usingDriverExecutable(driver)
            .usingAnyFreePort()
            .withEnvironment(getEnvironmentVariables())
            .build();
        ChromeOptions options = new ChromeOptions();
        if (driverOptions.has(PROXY))
            options.addArguments("--proxy-server=http://" + driverOptions.get(PROXY));
        String[] capDefs = driverOptions.getCapabilityDefinitions();
        if (capDefs.length > 0)
            options.addArguments(capDefs);
        return new ChromeDriver(service, options);
    }
}
