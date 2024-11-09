package com.solution.common.utils;

import io.appium.java_client.AppiumDriver;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HookUtil {

    private final Logger log = LoggerFactory.getLogger(HookUtil.class);

    public void startScenario(Scenario scenario) {
        log.info("");
        log.info("==========================================================================");
        log.info("================================Executing Scenario: " + scenario.getName() + " ===============================");
        log.info("==========================================================================");
        log.info("");
    }

    public void endOfTest(Scenario scenario, AppiumDriver appiumDriver) {
        if (scenario.getStatus() != null) {
            if (scenario.isFailed()) {
                takeScreenshot(scenario, appiumDriver);
            }
        }

        log.info("");
        log.info("==========================================================================");
        log.info("================================ 1 Test: " + scenario.getStatus().toString() + " ===============================");
        log.info("==========================================================================");
        log.info("");
    }

    public void endOfTest(Scenario scenario, WebDriver webDriver) {
        if (scenario.getStatus() != null) {
            if (scenario.isFailed()) {
                takeScreenshot(scenario, webDriver);
            }
        }

        log.info("");
        log.info("==========================================================================");
        log.info("================================ 2 Test: " + scenario.getStatus().toString() + " ===============================");
        log.info("==========================================================================");
        log.info("");
    }

    public void endOfTest(Scenario scenario) {
        log.info("");
        log.info("==========================================================================");
        log.info("================================ 3 Test: " + scenario.getStatus().toString() + " ===============================");
        log.info("==========================================================================");
        log.info("");
    }

    public void takeScreenshot(Scenario scenario, AppiumDriver appiumDriver) {
        try {
            String filename = scenario.getName().replaceAll("\\s+", "_");
            final byte[] ss = ((TakesScreenshot) appiumDriver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(ss, "image/png", filename);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void takeScreenshot(Scenario scenario, WebDriver webDriver) {
        try {
            String filename = scenario.getName().replaceAll("\\s+", "_");
            final byte[] ss = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(ss, "image/png", filename);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }
}