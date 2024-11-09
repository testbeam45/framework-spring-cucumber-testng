package com.solution.ui.utils;

import com.codeborne.selenide.WebDriverRunner;
import com.solution.common.utils.ApplicationProperties;
import com.solution.common.utils.Constants;
import io.appium.java_client.safari.options.SafariOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.NoSuchElementException;

@Component
public class DriverManager {
    private static final ThreadLocal<WebDriver> webDriverThreadLocal = new ThreadLocal<>();
    private final Logger log = LoggerFactory.getLogger(DriverManager.class);

    private WebDriverWait wait;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    @Lazy
    private DriverWait driverWait;

    @Autowired
    private Environment environment;


    public void createSeleniumDriver() throws MalformedURLException {
        if (getWebDriver() == null) {
            if (Arrays.toString(this.environment.getActiveProfiles()).contains("jenkins")) {
                log.info("Remote URL for Selenium: " + applicationProperties.getSeleniumGridUrl());
                setRemoteDriver(new URL(applicationProperties.getSeleniumGridUrl()));
            } else {
                setLocalWebDriver();
            }
        }
        WebDriverRunner.setWebDriver(getWebDriver());
        WebDriverRunner.getWebDriver().manage().deleteAllCookies();//useful for AJAX pages
        WebDriverRunner.getWebDriver().manage().window().maximize();
    }

    public void setLocalWebDriver() {
        switch (applicationProperties.getBrowser()) {
            case ("chrome"):
                WebDriverManager.chromedriver().clearDriverCache().cachePath(Constants.DRIVER_DIRECTORY).avoidOutputTree().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-logging");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--headless");
                chromeOptions.setCapability("platformName", Platform.ANY);
                webDriverThreadLocal.set(new ChromeDriver(chromeOptions));
                break;
            case ("firefox"):
                WebDriverManager.firefoxdriver().clearDriverCache().cachePath(Constants.DRIVER_DIRECTORY).avoidOutputTree().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setCapability("marionette", true);
                webDriverThreadLocal.set(new FirefoxDriver(firefoxOptions));
                break;
            case ("safari"):
                WebDriverManager.safaridriver().clearDriverCache().cachePath(Constants.DRIVER_DIRECTORY).avoidOutputTree().setup();
                webDriverThreadLocal.set(new SafariDriver());
                break;
            case ("edge"):
                WebDriverManager.edgedriver().clearDriverCache().cachePath(Constants.DRIVER_DIRECTORY).avoidOutputTree().setup();
                webDriverThreadLocal.set(new EdgeDriver());
                break;
            default:
                throw new NoSuchElementException("Failed to create or absence of WebDriver for::: " + applicationProperties.getBrowser() + " ::: Please check DriverManager class.");
        }
        driverWait.getDriverWaitThreadLocal().set(new WebDriverWait(webDriverThreadLocal.get(), Constants.timeoutShort, Constants.pollingShort));
    }

    private void setRemoteDriver(URL hubUrl) {
        Capabilities capability;
        switch (applicationProperties.getBrowser()) {
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setCapability("enableVNC", true); // Add any custom capabilities here
                webDriverThreadLocal.set(new RemoteWebDriver(hubUrl, firefoxOptions));
                break;
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setCapability("enableVNC", true);
                chromeOptions.setCapability("enableVideo", true);
                chromeOptions.setCapability("enableLog", true);
                chromeOptions.setCapability("screenResolution", "1920x1080x24");
                webDriverThreadLocal.set(new RemoteWebDriver(hubUrl, chromeOptions));
                break;
            case "ie":
                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                webDriverThreadLocal.set(new RemoteWebDriver(hubUrl, ieOptions));
                break;
            case "safari":
                SafariOptions safariOptions = new SafariOptions();
                webDriverThreadLocal.set(new RemoteWebDriver(hubUrl, safariOptions));
                break;
            case ("edge"):
                EdgeOptions edgeOptions = new EdgeOptions();
                webDriverThreadLocal.set(new RemoteWebDriver(hubUrl, edgeOptions));
                break;
            default:
                throw new NoSuchElementException("Failed to create an instance of RemoteWebDriver for: " + applicationProperties.getBrowser());
        }
        driverWait.getDriverWaitThreadLocal().set(new WebDriverWait(webDriverThreadLocal.get(), Constants.timeoutShort, Constants.pollingShort));
    }

    public WebDriver getWebDriver() {
        return webDriverThreadLocal.get();
    }

    public JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) getWebDriver();
    }

    public boolean isDriverExisting() {
        File geckoDriver = new File(Constants.DRIVER_DIRECTORY + "/geckodriver" + getExtension());
        File chromedriver = new File(Constants.DRIVER_DIRECTORY + "/chromedriver" + getExtension());
        return geckoDriver.exists() && chromedriver.exists();
    }

    public void downloadDriver() {
        try {
            Process process;
            if (getOperatingSystem().equals("win")) {
                process = Runtime.getRuntime().exec("cmd.exe /c downloadDriver.sh", null, new File(Constants.COMMON_RESOURCES));
            } else {
                process = Runtime.getRuntime().exec(new String[]{"sh", "-c", Constants.COMMON_RESOURCES + "/downloadDriver.sh"});
            }
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                log.debug(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getOperatingSystem() {
        String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ROOT);
        log.debug("Operative System Detected: " + os);
        if (os.contains("mac") || os.contains("darwin")) {
            return "mac";
        } else if (os.contains("win")) {
            return "win";
        } else {
            return "linux";
        }
    }

    private String getExtension() {
        String extension = "";
        if (getOperatingSystem().contains("win")) {
            return ".exe";
        }
        return extension;
    }
}
