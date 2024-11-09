package com.solution.mobile.step;

import com.solution.common.utils.ApplicationProperties;
import com.solution.mobile.page.Application;
import io.cucumber.java.en.Given;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonPageSteps extends AbstractStep {

    private final Logger logger = LoggerFactory.getLogger(CommonPageSteps.class);

    private final Application app;

    private final ApplicationProperties applicationProperties;

    public CommonPageSteps(Application app, ApplicationProperties applicationProperties) {
        this.app = app;
        this.applicationProperties = applicationProperties;
    }

    @Given("The user opened the Notepad Application")
    public void the_user_opened_the_notepad_application() {
        app.start(applicationProperties.getFullPathAppName());
    }
}
