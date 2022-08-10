package com.solution.mobile.page;

import com.solution.mobile.annotations.PageObject;
import com.solution.mobile.utils.DriverManager;

@PageObject
public class Application extends AbstractPage {

    public Application (DriverManager driverManager) {
        super(driverManager);
    }

    public void start(String apk) {
        launchApp(apk);
    }

}

