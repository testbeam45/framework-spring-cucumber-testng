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

@CucumberContextConfiguration
public class Hooks extends AbstractTestDefinition {

    private static boolean initialized = false;
    @Autowired
    private HookUtil hookUtil;
    @Autowired
    private DriverManager driverManager;

    @Before
    public void beforeScenario(Scenario scenario) throws MalformedURLException {
        driverManager.createAppiumDriver ();
    }

    @After
    public void afterScenario(Scenario scenario) {
        hookUtil.endOfTest(scenario);
        if(driverManager.getAppiumDriver () != null){
            driverManager.getAppiumDriver ().quit ();
        }
    }
}