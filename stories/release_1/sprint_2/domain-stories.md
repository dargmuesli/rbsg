<!-- In Jira sind die tasks das Äquivalent zu diesen domain stories. -->

# Webclient

## Mock REST Request Tests
Currently, the RESTRequestTests.java makes real requests to the game server. The responses should be mocked instead, using the already used Mockito.

# N/A

## Fix Checkstyle Warnings
The build of the new "checkstyle" branch introduces a lot of style related warnings, that are to be fixed. This should be done by correcting the code or if that's not reasonable by altering the checkstyle configuration, which must be confirmed by the PO or SM. Again: the latter option must be avoided.
