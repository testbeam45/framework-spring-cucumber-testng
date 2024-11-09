package com.solution.mobile.utils.expectedConditions;

import com.solution.common.utils.Constants;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

public class ClickabilityOfElement implements ExpectedCondition<WebElement> {

    private final WebElement element;

    public ClickabilityOfElement(WebElement element) {
        this.element = element;
    }

    @Override
    public WebElement apply(WebDriver webDriver) {

        final Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withTimeout(Constants.timeoutShort)
                .pollingEvery(Constants.pollingShort)
                .ignoring(java.util.NoSuchElementException.class,
                        StaleElementReferenceException.class);
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (StaleElementReferenceException | NoSuchElementException | ElementNotInteractableException e) {
            return element;
        } catch (Throwable t) {
            throw new Error(t);
        }
    }
}
