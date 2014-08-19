package org.bloblines.ui.web;

import org.bloblines.server.GameServer;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class BlobSocketCreator implements WebSocketCreator {

	private BlobSocket blobSocket;

	public BlobSocketCreator(GameServer gameServer) {
		this.blobSocket = new BlobSocket(gameServer);
	}

	@Override
	public Object createWebSocket(ServletUpgradeRequest req,
			ServletUpgradeResponse resp) {
		return blobSocket;
	}

}
