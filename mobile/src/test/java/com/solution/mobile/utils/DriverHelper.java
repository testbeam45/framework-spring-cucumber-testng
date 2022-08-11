package com.solution.mobile.utils;

import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
public class DriverHelper {

    //private final Logger logger = LoggerFactory.getLogger ( DriverHelper.class );
    private final DriverManager driverManager;

    private MobileElement element;
    private final DriverWait driverWait;

    @Autowired
    public DriverHelper (DriverManager driverManager , DriverWait driverWait) {
        this.driverManager = driverManager;
        this.driverWait = driverWait;
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 500), include = {RetryException.class})
    public void click (By locator) {
        if ( isElementDisplayed ( locator ) ) {
            driverManager.getAppiumDriver ( ).findElement ( locator ).click ( );
        }
    }


    /**
     * Send Keys to the specified element, clears the element first
     */
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 500), include = {RetryException.class})
    public void sendKeys (By locator , String value) {
        try {
            Thread.sleep ( 5000 );
            if ( value != null ) {
                element = (MobileElement) driverManager.getAppiumDriver ( ).findElement ( locator );
                if ( value.length ( ) > 0 ) {
                    element.clear ( );
                    element.sendKeys ( value );
                } else {
                    element.clear ( );
                }
            }
        } catch (Exception e) {
            System.out.println ( e.getLocalizedMessage ( ) );
        }
    }

    public String getText (By locator) {
       try{
           Thread.sleep ( 5000 );
           return driverManager.getAppiumDriver ( ).findElement ( locator ).getText ( );
       }catch (Exception e){
           e.printStackTrace ();
       }
       return null;
    }


    public boolean isElementDisplayed (By locator) {
        return driverWait.getWebDriverWait ( ).until ( ExpectedConditions.visibilityOf ( driverManager.getAppiumDriver ( ).findElement ( locator ) ) ).isDisplayed ( );
    }

    /**
     * Checks if the specified element is displayed
     */
    public boolean isElementDisplayed (WebElement element) {
        boolean present = false;
        try {
            present = element.isDisplayed ( );
        } catch (Exception ignored) {
        }
        return present;
    }
}
