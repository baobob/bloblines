package org.bloblines.ui.web;

import java.util.HashMap;

import org.bloblines.server.GameServer;
import org.eclipse.jetty.server.Server;

public class WebServer {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);

		//		// Sessions are bound to a context.
		//		ServletContextHandler context = new ServletContextHandler(
		//				ServletContextHandler.SESSIONS);
		//		server.setHandler(context);

		GameServer gameServer = new GameServer("Bloblines 101",
				new HashMap<String, String>());

		////		context.addServlet(new ServletHolder(new MainServlet(gameServer)), "/");
		//		context.addServlet(WebSocketHandler.class, "/socket");
		//		context.get
		BlobSocketHandler handler = new BlobSocketHandler(gameServer);
		server.setHandler(handler);

		server.start();
		server.join();
	}
}
