package com.solution.ui.page;

import com.solution.ui.annotations.PageObject;
import com.solution.ui.utils.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

@PageObject
public class WikipediaCommonPage extends AbstractPage {

    @FindBy(how = How.CLASS_NAME, using = "mainpage-welcome-sitename")
    private WebElement centralLogo;

    public WikipediaCommonPage(DriverManager driverManager) {
        super(driverManager);
    }

    public WebElement getCentralLogo() {
        return centralLogo;
    }
}

