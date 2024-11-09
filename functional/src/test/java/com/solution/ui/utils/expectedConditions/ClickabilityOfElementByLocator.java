package com.solution.ui.utils.expectedConditions;

import com.solution.common.utils.Constants;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

public class ClickabilityOfElementByLocator implements ExpectedCondition<WebElement> {

    private final By locator;

    public ClickabilityOfElementByLocator(By locator) {
        this.locator = locator;
    }

    @Override
    public WebElement apply(WebDriver webDriver) {

        final Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withTimeout(Constants.timeoutShort)
                .pollingEvery(Constants.pollingShort)
                .ignoring(java.util.NoSuchElementException.class,
                        StaleElementReferenceException.class);

        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (StaleElementReferenceException | NoSuchElementException | ElementNotInteractableException e) {
            return webDriver.findElement(locator);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

}
