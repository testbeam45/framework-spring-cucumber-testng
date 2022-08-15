package com.solution.ui.step;

import com.solution.common.utils.ApplicationProperties;
import com.solution.common.utils.HookUtil;
import com.solution.ui.config.AbstractTestDefinition;
import com.solution.ui.utils.DriverManager;
import com.codeborne.selenide.WebDriverRunner;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.net.MalformedURLException;
import java.util.Arrays;

@CucumberContextConfiguration
public class Hooks extends AbstractTestDefinition {

    private static final Object lock = new Object();
    private static boolean initialized = false;
    @Autowired
    private HookUtil hookUtil;
    @Autowired
    private DriverManager driverManager;

    @Before
    public void beforeScenario(Scenario scenario) throws MalformedURLException {
        synchronized (lock) {
            if (!initialized) {
                if (!driverManager.isDriverExisting()) {
                    driverManager.downloadDriver();
                }
                initialized = true;
            }
        }
        driverManager.createSeleniumDriver ();
    }

    @After
    public void afterScenario(Scenario scenario) {
        hookUtil.endOfTest(scenario, driverManager.getWebDriver ());
        hookUtil.takeScreenshot ( scenario,  driverManager.getWebDriver ());
        if (driverManager.getWebDriver() != null) {
            WebDriverRunner.closeWebDriver();
        }
    }
}