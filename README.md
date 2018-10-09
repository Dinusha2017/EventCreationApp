# EventCreationApp
This repository contains the code for a sample application which adds an Event to the Google Calendar. Please visit: https://dinusha2017.blogspot.com/2018/10/adding-event-to-google-calendar-using.html if you would like to know more about how to add an Event to the Google Calendar.

Before carrying out the following steps, please ensure that you have properly installed and configured JAVA and MAVEN.

Please carry out the steps in https://dinusha2017.blogspot.com/2018/10/adding-event-to-google-calendar-using.html to register the application, and enable OAuth 2.0 access to obtain your own Client ID and Client Secret values.

Please do the following changes to the files mentioned:<br />
-> In index.html, you may change the client id to your one in the JavaScript method, redirectToGoogleOAuthEndpoint().<br />
-> In application.properties, you may change the client_id, and client_secret values to the values you obtained when you created the credentials in Google API Console.

To test this application:
1. Clone this repository to your machine.
2. Open the Command Prompt, and go to the root or the home of the cloned folder.
3. Execute the following commands while you are at the above stated directory.
4. Run the command: mvn clean compile package
5. Run the command: java -jar target/EventCreationApp-0.0.1-SNAPSHOT.jar
6. Access the index.html by giving the url: http://localhost:8081/index.html in the browser.
