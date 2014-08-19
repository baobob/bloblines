package org.bloblines.ui.web;

import org.bloblines.server.GameServer;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.parser.JSONParser;

@WebSocket
public class BlobSocket {

	private GameServer gameServer;

	private JSONParser parser = new JSONParser();

	private Session session;

	public BlobSocket(GameServer gameServer) {
		this.gameServer = gameServer;
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		System.out.println("Close: statusCode=" + statusCode + ", reason="
				+ reason);
	}

	@OnWebSocketError
	public void onError(Throwable t) {
		System.out.println("Error: " + t.getMessage());
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		System.out.println("Connect: "
				+ session.getRemoteAddress().getAddress());
		this.session = session;
	}

	@OnWebSocketMessage
	public void onMessage(String message) {
		System.out.println("Message: " + message);
		//		try {
		//			JSONObject msg = (JSONObject) parser.parse(message);
		//			String type = (String) msg.get("type");
		//			if (BlobMessage.TYPE_LOGIN.equals(type)) {
		//				String login = (String) msg.get("login");
		//				LoginRequest request = new LoginRequest(login);
		//				LoginResponse challenge = gameServer.loginRequest(request);
		//				JSONObject challengeMessage = new JSONObject();
		//				challengeMessage.put("type", TYPE_CHALLENGE);
		//				challengeMessage.put("challenge", challenge);
		//				session.getRemote().sendString(challengeMessage.toJSONString());
		//			} else if (TYPE_CHALLENGE.equals(type)) {
		//
		//			} else if (TYPE_GET.equals(type)) {
		//
		//			} else if (TYPE_DO.equals(type)) {
		//
		//			}
		//
		//		} catch (ParseException | IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
	}
}