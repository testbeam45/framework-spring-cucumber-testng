package com.solution.mobile.utils;


import com.solution.common.utils.SelenoidType;
import com.solution.common.utils.SelenoidValues;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AutomationName;
//import io.appium.java_client.remote.MobileCapabilityType;  //Deprecated in java-client 9.xx appium
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

@Component
public class DriverManager {

    private static final ThreadLocal<AppiumDriver> appiumDriverThreadLocal = new ThreadLocal<>();
    private final Logger log = LoggerFactory.getLogger(DriverManager.class);

    @Autowired
    private Environment environment;

    public void createAppiumDriver() throws MalformedURLException, URISyntaxException {
        if (getAppiumDriver() == null) {
            if (Arrays.toString(this.environment.getActiveProfiles()).contains("jenkins")) {
                setRemoteDriverAppium("http://127.0.0.1:4723");
            } else {
                setLocalDriverAppium("http://127.0.0.1:4723");

            }
        }
    }


    public void setLocalDriverAppium(String hubUrl) throws MalformedURLException {
        // Initialize the UiAutomator2Options for Android
        UiAutomator2Options options = new UiAutomator2Options();

        // Set the capabilities with "appium:" prefix as required by Appium 2.x
        options.setCapability("appium:deviceName", "Android Emulator");
        options.setCapability("appium:udid", "emulator-5554");
        options.setCapability("appium:app", "/Users/ddshenoy/Documents/framework-spring-cucumber-testng/mobile/src/test/resources/apps/notepad.apk");
        options.setCapability("appium:automationName", "UiAutomator2");
        options.setCapability("appium:platformVersion", "15");

        // Set additional capabilities for timeout and reset options
        options.setCapability("appium:androidInstallTimeout", 180000);
        options.setCapability("appium:acceptInsecureCerts", true);
        options.setCapability("appium:appWaitDuration", 60000);
        options.setCapability("appium:avdLaunchTimeout", 180000);
        options.setCapability("appium:avdReadyTimeout", 180000);
        options.setCapability("appium:newCommandTimeout", 180000);
        options.setCapability("appium:noReset", false);
        options.setCapability("appium:fullReset", true);

        // Log the URL and initialize the driver
        URL url = new URL(hubUrl);
        log.info("Initializing Appium driver with URL: {}", url);
        log.info("Capability options are: {}", options);
        log.info("Driver is: {}",appiumDriverThreadLocal.get());



        try {
            log.info("Initializing Appium driver with URL: {}", url);
            appiumDriverThreadLocal.set(new AndroidDriver(url, options));
            log.info("Appium Driver initialized successfully.");
        } catch (Exception e) {
            log.error("Failed to create AppiumDriver: ", e);
            throw e;
        }

        log.info("Driver initialized: {}", getAppiumDriver());

        // Verify if the driver was successfully set
        if (getAppiumDriver() == null) {
            throw new IllegalStateException("Appium Driver not initialized.");
        }
    }

    /*
     * This method is using to set Selenoid Capabilities
     * https://github.com/aerokube/selenoid/blob/master/docs/special-capabilities.adoc
     *
     * */

   /* public synchronized static void setRemoteDriverAppium(String hubUrl) throws MalformedURLException {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        *//*Define Android Skin and Activate VNC*//*
        capabilities.setCapability(SelenoidType.ENABLE_VNC, true);
        capabilities.setCapability(SelenoidType.SKIN, "WXGA720");
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, SelenoidValues.android);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");

        *//*Set Run Environment*//*
        capabilities.setCapability(SelenoidType.SCREEN_RESOLUTION, "1280x1024x24");
        capabilities.setCapability(SelenoidType.TIME_ZONE, "Europe/Moscow");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "7");
        capabilities.setCapability(MobileCapabilityType.APP, "https://github.com/jesussalatiel/Appium-Cloud/raw/main/notepad.apk");
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);

        *//*Load Waits*//*
        capabilities.setCapability(SelenoidType.ANDROID_INSTALL_TIMEOUT, 180000);
        capabilities.setCapability(SelenoidType.ACCEPT_INSECURE_CERTS, true);
        capabilities.setCapability(SelenoidType.APP_WAIT_DURATION, 60000);
        capabilities.setCapability(SelenoidType.AVD_LAUNCH_TIMEOUT, 180000);
        capabilities.setCapability(SelenoidType.AVD_READY_TIMEOUT, 180000);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 180000);

        *//*Reset app or environment*//*
        capabilities.setCapability(MobileCapabilityType.NO_RESET, false);
        capabilities.setCapability(MobileCapabilityType.FULL_RESET, true);

        // Create Remote Connection to Selenoid
        appiumDriverThreadLocal.set(new AndroidDriver<>(new URL(hubUrl), capabilities));
    }*/

    public synchronized static void setRemoteDriverAppium(String hubUrl) throws MalformedURLException, URISyntaxException {

        // Initialize UiAutomator2Options for Android
        UiAutomator2Options options = new UiAutomator2Options();

        /* Define Android Skin and Activate VNC */
        options.setCapability("enableVNC", true);
        options.setCapability("skin", "WXGA720");
        options.setCapability("browserName", "android");
        options.setCapability("platformName", "Android");

        /* Set Run Environment */
        options.setCapability("screenResolution", "1280x1024x24");
        options.setCapability("timeZone", "Europe/Moscow");
        options.setCapability("platformVersion", "7");
        options.setCapability("app", "https://github.com/jesussalatiel/Appium-Cloud/blob/main/notepad.apk");
        options.setAutomationName("UiAutomator2");  // Replaced MobileCapabilityType.AUTOMATION_NAME

        /* Load Waits */
        options.setCapability("androidInstallTimeout", 180000);
        options.setCapability("acceptInsecureCerts", true);
        options.setCapability("appWaitDuration", 60000);
        options.setCapability("avdLaunchTimeout", 180000);
        options.setCapability("avdReadyTimeout", 180000);
        options.setCapability("newCommandTimeout", 180000);

        /* Reset app or environment */
        options.setNoReset(false);
        options.setFullReset(true);

        // Create Remote Connection to Appium Hub (Selenoid)
        appiumDriverThreadLocal.set(new AndroidDriver(new URI(hubUrl).toURL(), options));
    }

    public synchronized static AppiumDriver getAppiumDriver() {
        return appiumDriverThreadLocal.get();
    }
}
