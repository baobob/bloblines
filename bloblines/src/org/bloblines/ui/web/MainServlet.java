package org.bloblines.ui.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bloblines.server.GameServer;

public class MainServlet extends HttpServlet {
	/** serialUID */
	private static final long serialVersionUID = 4456094697752397619L;

	private GameServer gameServer;

	public MainServlet(GameServer gameServer) {
		this.gameServer = gameServer;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//		if (request.getSession().getAttribute("user") == null) {
		//			serveHtmlFile(response, "web/login.html");
		//		} else {
		//			serveHtmlFile(response, "web/game.html");
		//		}
		if ("/".equals(request.getServletPath())) {
			Files.copy(Paths.get("web/bloblines.html"),
					response.getOutputStream());
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			super.doGet(request, response);
		}
	}

	//	@Override
	//	protected void doPost(HttpServletRequest request,
	//			HttpServletResponse response) throws ServletException, IOException {
	//		if ("/login".equals(request.getServletPath())) {
	//			doLogin(request, response);
	//		}
	//	}

	//	private void doLogin(HttpServletRequest request,
	//			HttpServletResponse response) {
	//		String login = request.getParameter("login");
	//		if (gameServer.login(login)) {
	//			request.getSession().setAttribute("user", login);
	//			serveHtmlFile(response, "web/game.html");
	//		} else {
	//			serveHtmlFile(response, "web/login.html");
	//		}
	//		return;
	//	}
	//
	//	private void serveHtmlFile(HttpServletResponse response, String filename) {
	//		try {
	//			Files.copy(Paths.get(filename), response.getOutputStream());
	//			response.setContentType("text/html");
	//			response.setStatus(HttpServletResponse.SC_OK);
	//		} catch (IOException ex) {
	//			ex.printStackTrace();
	//		}
	//	}

}
