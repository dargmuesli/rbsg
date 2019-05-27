<!-- Aufgrund von anfänglichen Unklarheiten sind diese domain stories in Jira als stories eingetragen, nicht als tasks. -->

# Design

## Basic FXML
Screen layout files for the welcome, register and main screen need to exist, so that GUI elements can be added. All scale properly with different window dimensions.

## Darkmode
Create application stylesheets that theme a dark background with light text and input elements. A harmonic UX is focused.

## Fades
Smooth transitions between screens are to be created. They too should scale well with different window sizes.

## Error Handling
Error messages returned by the webclient need to be visually represented to the user. A class needs to be implemented that can receive error messages as strings and to which one, as a screen, can subscribe to display them.

# Persistence

## JSON De-&Serialization
A class which can be called to load and save data to files needs to be created. Individual keys should be targetable.

## En-&Decrypting Values
As a special feature, e.g. a custom server could be utilized to act as a secret provider or processing unit to be able to save and load encrypted (private) data.

## YAML Game Saves
For use in a future release the current state of the game should be savable using fulibYaml.

# Webclient

## HTTP Manager
A class with functions for GET, POST and DELETE requests needs to be written. Their parameters should be "URI uri, Header[] headers, HttpEntity body" and they should return a "String responseBody".

## Response Body
A function getResponseBody(HttpResponse httpResponse) needs to be written, which deals with the response itself, its status code and which throws error messages.

## REST Endpoint
For every API endpoint a separate request class needs to be written, with each one featuring an inner builder class. They'll be created by the main API class and invoked by the http manager. An interface should define the overall layout.

## Socket Endpoint
A class that acts as a web socket client needs to be implemented to which message handlers, like the chat class, can be subscribed.

## API
An RbsgApi class needs to be written that contains getter functions for each endpoint's builder class object with required parameters as parameters.

## Chat
A class that represents every feature of a chat, like history and scopes, needs to be created.
