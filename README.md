
User can registered to app himself using a form registration.
Added data to form, send to server. Server verifying data, saving user
and transfer response with status "201 OK" to front.

Server pass a link to user email for verifying email. User should be 
to click on link.
Front getting token from user and pass it to server. 
Server do verification the token. If token is valid, server send status "Ok"
to front, what allow user to get access to site.

Front shows to user information about verify email and passing



### Also admin can added new user to app and choose for him one or more roles.
Admin using another form registration from his account.