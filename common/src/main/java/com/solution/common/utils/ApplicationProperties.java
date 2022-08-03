package com.solution.common.utils;

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
    @Value("${gridUrl}")
    private String gridUrl;
    @Value ( "${web.driver.manager.active}" )
    private Boolean activateWebDriverManager;
    @Value ( "${appium.GridUrl}" )
    private String gridAppiumUrl;
    @Value ( "${application.timeout}" )
    private int waitTimeout;

    public int getWaitTimeout(){return waitTimeout;}
    public String getAppiumUrl(){return gridAppiumUrl;}
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
    public String getGridUrl() {
        return gridUrl;
    }
}