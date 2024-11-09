package com.solution.mobile.step;

import com.solution.common.utils.ApplicationProperties;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;

import static org.junit.Assert.assertTrue;

public class NotepadPageSteps extends AbstractStep {

    private final ApplicationProperties applicationProperties;

    public NotepadPageSteps(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Given("The user clicked on new note")
    public void the_user_clicked_on_new_note() throws NoSuchFieldException {
        click(By.id("com.onto.notepad:id/add_note"));
    }

    @Then("The user write the title {string}")
    public void the_user_write_the_title(String string) {
        writeText(By.id("com.onto.notepad:id/titleEdit"), string);
    }

    @Then("The user write the content {string}")
    public void the_user_write_the_content(String string) {
        writeText(By.id("com.onto.notepad:id/contentEdit"), string);
    }

    @Then("The user click on save note")
    public void the_user_click_on_save_note() throws NoSuchFieldException {
        click(By.id("com.onto.notepad:id/save_note"));
    }

    @Then("The user validate the title with the text {string} displayed")
    public void the_user_validate_the_title_with_the_text_displayed(String title) {
        assertTrue("Note not added", title.equals(getText(By.xpath("//*[@text='" + title + "']"))));
    }
}
