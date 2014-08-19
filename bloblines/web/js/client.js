var ws; 

$( document ).ready(function() {
	ws = initWebSocket(); 
});

function initLoginForm() {
	addMessage("Please, login"); 
	$('#content').html('<div><span>Login</span><input type="text" id="input-login"></div><div><span>Password</span><input type="password" id="input-password"></div><div><button id="button-login">Login</button></div>'); 
	$('#button-login').click(function() {loginRequest($('#input-login').val())}); 
}

function loginRequest(login) {
	console.log('login: ' + login);
	var message = new socketMessage("LOGIN");
	message.login = login; 
	// we send a login request, server will send a challenge
	ws.send(JSON.stringify(message));
}

function challengeResponse(challenge) {
	// challenge sent by server, reply with password
	// TODO implement challenge
	var message = new socketMessage("CHALLENGE");
	message.challenge = challenge + $('#input-password').val(); 
	ws.send(JSON.stringify(message)); 
}

function addMessage(message) {
	$('#messages ul').append('<li><span class="message">' + message + '</span></li>');
	console.log(message); 
}

function socketMessage(type)
{
	this.type = type;
}
