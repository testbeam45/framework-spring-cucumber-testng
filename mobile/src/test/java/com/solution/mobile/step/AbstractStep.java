package com.solution.mobile.step;

import com.solution.mobile.utils.DriverHelper;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractStep {

    @Autowired
    private DriverHelper driverHelper;

    protected void click(By locator) throws NoSuchFieldException {
        driverHelper.click(locator);
    }

    protected void writeText(By locator, String value) {
        driverHelper.sendKeys(locator, value);
    }

    protected String getText(By locator) {
        return driverHelper.getText(locator);
    }
}
