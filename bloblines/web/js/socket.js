
function initWebSocket() {
	var serverName = "127.0.0.1"; 
	var serverPort = 8080; 
	
	var ws = new WebSocket("ws://" + serverName + ":" + serverPort);

	ws.onopen = function() {
		// Login page
		initLoginForm(); 
	};

	ws.onmessage = function (evt) {
		console.log("Message: " + evt.data);
		var message = JSON.parse(evt.data);
		if (message.type == 'CHALLENGE') {
			challengeResponse(message.challenge); 
		}
	};

	ws.onclose = function(evt) {
		console.log("Closed! " + evt);
	};

	ws.onerror = function(err) {
		if (ws.readyState == ws.CONNECTING) {
			addMessage("Error occured during socket initialization. Unable to connect to server. ");
		} else {
			console.log("Error occured. Socket state: " + ws.readyState);
		}
	};
	
	return ws; 
}
