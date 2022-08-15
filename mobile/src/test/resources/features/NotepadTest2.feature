@Appium-Only
Feature: As a user, I want write one note with title and content

  Scenario Outline: Write Title <title> and content <content>
    Given The user opened the Notepad Application
    And The user clicked on new note
    Then The user write the title <title>
    And The user write the content <content>
    And The user click on save note
    Then The user validate the title with the text <title> displayed


    Examples:
      | title | content|
      | "Jesus Salatiel"   |  "My name"   |


