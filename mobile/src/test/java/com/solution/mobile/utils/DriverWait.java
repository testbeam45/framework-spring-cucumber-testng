package com.solution.mobile.utils;

import com.solution.common.utils.ApplicationProperties;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DriverWait {

    private static final ThreadLocal<Wait<AppiumDriver>> driverWaitThreadLocal = new ThreadLocal<>();


    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    public DriverWait(DriverManager driverManager) {
    }

    public Wait<AppiumDriver> getDriverWait() {
        return driverWaitThreadLocal.get();
    }

    public ThreadLocal<Wait<AppiumDriver>> getDriverWaitThreadLocal() {
        return driverWaitThreadLocal;
    }

    public WebDriverWait getWebDriverWait() {
        return new WebDriverWait(DriverManager.getAppiumDriver(), applicationProperties.getWaitTimeout());
    }
}
