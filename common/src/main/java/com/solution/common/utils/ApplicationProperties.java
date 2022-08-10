package com.solution.common.utils;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {

    @Value("${api.url.value}")
    private String apiAppUrl;
    @Value("${web.url.value}")
    private String webAppUrl;
    @Value("${browser}")
    private String browser;
    @Value("${selenium.gridUrl}")
    private String seleniumGridUrl;
    @Value("${appium.gridUrl}")
    private String appiumGridUrl;
    @Value ( "${web.driver.manager.active}" )
    private Boolean activateWebDriverManager;
    @Value ( "${application.timeout}" )
    private int waitTimeout;
    @Value ( "${appium.deviceName}" )
    private String deviceName;
    @Value ( "${appium.app}" )
    private String appName;
    @Value ( "${appium.activate}" )
    private Boolean isAppiumOn;

    public String getDeviceName(){return deviceName;}
    public String getFullPathAppName(){return appName;}

    public int getWaitTimeout(){return waitTimeout;}
    public Boolean getIsWebDriverOn(){return activateWebDriverManager;}
    public String getWeatherAppUrl() {return apiAppUrl;}
    public void setApiAppUrl(String weatherAppUrl) {
        this.apiAppUrl = weatherAppUrl;
    }
    public String getWebUrl() {return webAppUrl;}
    public void setWebUrl(String webUrl) {
        this.webAppUrl = webUrl;
    }
    public String getBrowser() {
        return browser;
    }
    public void setBrowser(String browser) {
        this.browser = browser;
    }
    public String getSeleniumGridUrl() {return seleniumGridUrl;}
    public String getAppiumGridUrl(){return appiumGridUrl;}
    public Boolean getIsAppiumOn(){return isAppiumOn;}
}