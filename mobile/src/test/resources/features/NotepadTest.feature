@Appium
Feature: As a user, I want to write the name Jesus Salatiel

  Scenario: I want to write a name
    Given The user opened the Notepad Application
    And The user clicked on new note
    Then The user write the title "Note"
    And The user write the content "Jesus Salatiel"
    And The user click on save note
    Then The user validate the title with the text "Note" displayed



