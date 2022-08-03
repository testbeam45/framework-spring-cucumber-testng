package com.solution.ui.utils;

import com.solution.common.utils.ApplicationProperties;
import com.solution.common.utils.Constants;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Locale;
import java.util.NoSuchElementException;

@Component
public class DriverManager {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private final Logger log = LoggerFactory.getLogger(DriverManager.class);

    private WebDriverWait wait;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private DriverWait driverWait;

    @Autowired
    private Environment environment;



    public void createDriver() throws MalformedURLException {
        if (getDriver() == null) {
            if (Arrays.toString(this.environment.getActiveProfiles()).contains("jenkins")) {
                setRemoteDriver(new URL(applicationProperties.getGridUrl()));
            }else if (Arrays.toString(this.environment.getActiveProfiles()).contains("docker")) {
                setDockerDriver(new URL(applicationProperties.getGridUrl()));
            } else {
                setLocalWebDriver();
            }
            WebDriverRunner.setWebDriver(getDriver());
            WebDriverRunner.getWebDriver().manage().deleteAllCookies();//useful for AJAX pages
        }
    }

    public void setLocalWebDriver() {
        switch (applicationProperties.getBrowser()) {
            case ("chrome"):
                if(!applicationProperties.getIsWebDriverOn()){
                    System.setProperty("webdriver.chrome.driver", Constants.DRIVER_DIRECTORY + "/chromedriver" + getExtension());
                }
                else {
                    WebDriverManager.chromedriver ( ).cachePath ( Constants.DRIVER_DIRECTORY ).avoidOutputTree ( ).setup ( );
                }
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-logging");
                chromeOptions.addArguments ( "--no-sandbox" );
                chromeOptions.addArguments ( "--disable-dev-shm-usage" );
                chromeOptions.setCapability ("platformName", Platform.ANY);
                driverThreadLocal.set(new ChromeDriver(chromeOptions));
                break;
            case ("firefox"):
                if(!applicationProperties.getIsWebDriverOn()){
                    System.setProperty("webdriver.gecko.driver", Constants.DRIVER_DIRECTORY + "/geckodriver" + getExtension());
                    System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
                }else {
                    WebDriverManager.firefoxdriver ().cachePath ( Constants.DRIVER_DIRECTORY ).avoidOutputTree ().setup ();
                }
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setCapability("marionette", true);
                driverThreadLocal.set(new FirefoxDriver(firefoxOptions));
                break;
            case ("safari"):
                if(!applicationProperties.getIsWebDriverOn()){
                    System.setProperty("webdriver.opera.driver", Constants.DRIVER_DIRECTORY + "/operadriver" + getExtension());
                }else {
                    WebDriverManager.safaridriver ().cachePath ( Constants.DRIVER_DIRECTORY ).avoidOutputTree ().setup ();
                }
                driverThreadLocal.set(new OperaDriver());
                break;
            case ("edge"):
                if(!applicationProperties.getIsWebDriverOn()){
                    System.setProperty("webdriver.edge.driver", Constants.DRIVER_DIRECTORY + "/MicrosoftWebDriver" + getExtension());
                }else {
                    WebDriverManager.edgedriver ().cachePath ( Constants.DRIVER_DIRECTORY ).avoidOutputTree ().setup ();
                }
                driverThreadLocal.set(new EdgeDriver());
                break;
            default:
                throw new NoSuchElementException("Failed to create or absence of WebDriver for::: " + applicationProperties.getBrowser() + " ::: Please check DriverManager class.");
        }
        driverWait.getDriverWaitThreadLocal().set(new WebDriverWait(driverThreadLocal.get(), Constants.timeoutShort, Constants.pollingShort));
    }

    private void setRemoteDriver(URL hubUrl) {
        Capabilities capability;
        switch (applicationProperties.getBrowser()) {
            case "firefox":
                capability = DesiredCapabilities.firefox();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
                break;
            case "chrome":
                capability = DesiredCapabilities.chrome();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
                break;
            case "ie":
                capability = DesiredCapabilities.internetExplorer();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
                break;
            case "safari":
                capability = DesiredCapabilities.safari();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
                break;
            case ("edge"):
                capability = DesiredCapabilities.edge();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
                break;
            default:
                throw new NoSuchElementException("Failed to create an instance of RemoteWebDriver for: " + applicationProperties.getBrowser());
        }
        driverWait.getDriverWaitThreadLocal().set(new WebDriverWait(driverThreadLocal.get(), Constants.timeoutShort, Constants.pollingShort));
    }

    private void setDockerDriver(URL hubUrl){
        Capabilities capability;
        switch (applicationProperties.getBrowser()){
            case("chrome"):
                capability = DesiredCapabilities.chrome();
                driverThreadLocal.set(WebDriverManager.chromedriver().remoteAddress ( hubUrl ).capabilities ( capability).browserInDocker ().create ());
                break;
            case("firefox"):
                capability = DesiredCapabilities.firefox();
                driverThreadLocal.set(WebDriverManager.firefoxdriver ().remoteAddress ( hubUrl ).capabilities ( capability).browserInDocker ().create ());
                break;
            case "ie":
                capability = DesiredCapabilities.internetExplorer();
                driverThreadLocal.set(WebDriverManager.iedriver ().remoteAddress ( hubUrl ).capabilities ( capability).browserInDocker ().create ());
                break;
            case "safari":
                capability = DesiredCapabilities.safari();
                driverThreadLocal.set(WebDriverManager.safaridriver ().remoteAddress ( hubUrl ).capabilities ( capability).browserInDocker ().create ());
                break;
            case ("edge"):
                capability = DesiredCapabilities.edge();
                driverThreadLocal.set(WebDriverManager.edgedriver ().remoteAddress ( hubUrl ).capabilities ( capability).browserInDocker ().create ());
                break;
            default:
                throw new NoSuchElementException("Failed to create an instance of RemoteWebDriver for: " + applicationProperties.getBrowser());
        }
        driverWait.getDriverWaitThreadLocal().set(new WebDriverWait(driverThreadLocal.get(), Constants.timeoutShort, Constants.pollingShort));
    }

    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) getDriver();
    }

    public boolean isDriverExisting() {
        File geckoDriver = new File(Constants.DRIVER_DIRECTORY + "/geckodriver" + getExtension());
        File chromedriver = new File(Constants.DRIVER_DIRECTORY + "/chromedriver" + getExtension());
        return geckoDriver.exists() && chromedriver.exists();
    }

    public void downloadDriver() {
        if(!applicationProperties.getIsWebDriverOn()) {
            try {
                Process process;
                if ( getOperatingSystem ( ).equals ( "win" ) ) {
                    process = Runtime.getRuntime ( ).exec ( "cmd.exe /c downloadDriver.sh" , null ,
                            new File ( Constants.COMMON_RESOURCES ) );
                } else {
                    process = Runtime.getRuntime ( ).exec (
                            new String[]{"sh" , "-c" , Constants.COMMON_RESOURCES + "/downloadDriver.sh"} );
                }
                process.waitFor ( );
                BufferedReader reader = new BufferedReader ( new InputStreamReader ( process.getInputStream ( ) ) );
                String line = reader.readLine ( );
                while (line != null) {
                    log.debug ( line );
                    line = reader.readLine ( );
                }
            } catch (Exception e) {
                e.printStackTrace ( );
            }
        }
    }

    private String getOperatingSystem() {
        String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ROOT);
        log.debug ( "Operative System Detected: " + os);
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


    public AndroidDriver setRemoteDriverAppium(){
        AndroidDriver appium = null;
        String remoteContainer = "http://localhost:4444/wd/hub";
        String app = "/tmp/spotify.apk";
        String emulatorName = "emulator-5554";

        try {
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities ( );
            // Set emulator name: execute adb devices on device container to see the name
            desiredCapabilities.setCapability ( "deviceName" ,  emulatorName);
            // Set Platform
            desiredCapabilities.setCapability ( "platformName" , "Android" );
            // Set UIAutomator to use with Android to execute xpath easier on real devices
            // and emulators
            desiredCapabilities.setCapability ( MobileCapabilityType.AUTOMATION_NAME , "UIAutomator2" );
            // Copy app on the container file device and execute from there
            desiredCapabilities.setCapability ( "app" , app );
            // Reset environment: remove app and install it again
            desiredCapabilities.setCapability ( MobileCapabilityType.FULL_RESET , true );
            desiredCapabilities.setCapability ( MobileCapabilityType.NO_RESET , false );

            // Create Remote Connection to Appium Grid
            appium = new AndroidDriver ( new URL ( applicationProperties.getAppiumUrl () ), desiredCapabilities );

            // Wait to load mobile elements
            wait = new WebDriverWait ( appium , applicationProperties.getWaitTimeout () );
        }catch (MalformedURLException e){
            e.printStackTrace ();
        }
        return appium;
    }
}
