package com.solution.mobile.utils;

import com.solution.common.utils.ApplicationProperties;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.support.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DriverWait {

    private static final ThreadLocal < Wait < AppiumDriver > > driverWaitThreadLocal = new ThreadLocal <> ( );

    private final DriverManager driverManager;


    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    public DriverWait (DriverManager driverManager) {
        this.driverManager = driverManager;
    }

    public Wait < AppiumDriver > getDriverWait ( ) {
        return driverWaitThreadLocal.get ( );
    }

    public ThreadLocal < Wait < AppiumDriver > > getDriverWaitThreadLocal ( ) {
        return driverWaitThreadLocal;
    }

    public  WebDriverWait getWebDriverWait(){
        return new WebDriverWait ( driverManager.getAppiumDriver (), applicationProperties.getWaitTimeout () );
    }
}
