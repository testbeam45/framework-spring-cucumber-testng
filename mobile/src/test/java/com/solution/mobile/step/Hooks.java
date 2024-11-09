package com.solution.mobile.step;

import com.solution.common.utils.HookUtil;
import com.solution.mobile.config.AbstractTestDefinition;
import com.solution.mobile.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

@CucumberContextConfiguration
public class Hooks extends AbstractTestDefinition {

    @Autowired
    private HookUtil hookUtil;
    @Autowired
    private DriverManager driverManager;

    @Before
    public void beforeScenario(Scenario scenario) throws MalformedURLException, URISyntaxException {
        driverManager.createAppiumDriver();
        hookUtil.startScenario(scenario);
    }

    @After
    public void afterScenario(Scenario scenario) {
        hookUtil.takeScreenshot(scenario, driverManager.getAppiumDriver());
        hookUtil.endOfTest(scenario, driverManager.getAppiumDriver());
        if (driverManager.getAppiumDriver() != null) {
            driverManager.getAppiumDriver().quit();

        }
    }
}