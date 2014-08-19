package org.bloblines.ui.web;

import org.bloblines.server.GameServer;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class BlobSocketHandler extends WebSocketHandler {

	private GameServer gameServer;

	public BlobSocketHandler(GameServer gameServer) {
		this.gameServer = gameServer;
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.getPolicy().setIdleTimeout(10000);
		factory.setCreator(new BlobSocketCreator(gameServer));
	}

}
