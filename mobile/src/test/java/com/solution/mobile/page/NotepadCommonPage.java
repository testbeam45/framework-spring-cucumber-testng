package com.solution.mobile.page;

import com.solution.mobile.annotations.PageObject;
import com.solution.mobile.utils.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

@PageObject
public class NotepadCommonPage extends AbstractPage {

    @FindBy(how = How.CLASS_NAME, using = "mainpage-welcome-sitename")
    private WebElement centralLogo;

    public NotepadCommonPage(DriverManager driverManager) {
        super(driverManager);
    }

    public WebElement getCentralLogo() {
        return centralLogo;
    }
}

