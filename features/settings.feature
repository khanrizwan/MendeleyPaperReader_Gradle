Feature: Test settings functionality

@checkBox_text
Scenario: As a logged in user test checkBox "sync on load"
Given I wait for the Sign in button to appear
Then I wait for the Mendeley oauth login page
Then I enter "pdrolourenco@gmail.com" into the "username" input field
Then I enter "000000" into the "password" input field
Then I touch "Authorize"
Then I wait for the "Contacting Mendeley ..." progress dialog to close
Then I wait for the "Sync data..." progress dialog to close
Then I validate if it's the right activity - MainMenuActivity
Then I see Menu list
Then I touch on "Settings" button
Then I validate if it's the right activity - Settings
Then I check the checkBox "syncOnLoad"
Then I go back
Then I validate if it's the right activity - MainMenuActivity
Then I see Menu list
Then I touch on "Settings" button
Then I validate if it's the right activity - Settings
Then I confirm that the checkBox "syncOnLoad" are checked
Then I uncheckBox checkbox "syncOnLoad"



@Sync_files_on_load
Scenario: As a logged in user sync files on load
Given I wait for the Sign in button to appear
Then I wait for the Mendeley oauth login page
Then I enter "pdrolourenco@gmail.com" into the "username" input field
Then I enter "000000" into the "password" input field
Then I touch "Authorize"
Then I wait for the "Contacting Mendeley ..." progress dialog to close
Then I wait for the "Sync data..." progress dialog to close
Then I validate if it's the right activity - MainMenuActivity
Then I touch on "All Documents"
Then I scroll until I see the "Genome Biology and Evolution 2013"
Then I touch on "Genome Biology and Evolution 2013"
Then I confirm that the file has not been downloaded
Then I go back
Then I sleep for 9 seconds
Then I go back
Then I validate if it's the right activity - MainMenuActivity
Then I see Menu list
Then I touch on "Settings" button
Then I validate if it's the right activity - Settings
Then I check the checkBox "syncOnLoad"
Then I go back
Then I press refresh button
Then I wait for the "Sync data..." progress dialog to close
Then I touch on "All Documents"
Then I scroll until I see the "Genome Biology and Evolution 2013"
Then I touch on "Genome Biology and Evolution 2013"
Then I check if the file has been downloaded
