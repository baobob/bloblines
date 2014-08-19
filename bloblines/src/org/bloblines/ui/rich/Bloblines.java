package org.bloblines.ui.rich;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.bloblines.data.game.Player;
import org.bloblines.server.GameServer;

public class Bloblines {

	public JFrame mainWindow;

	private JTextPane textAreaMessages;

	private JTabbedPane tabbedPane;

	private GameServer server = new GameServer("Local Server", null);
	private Player player = null; // current player, null when player is not connected

	private GamePanel gamePanel;

	/**
	 * Create the application.
	 */
	public Bloblines() {
		initialize();
		mainWindow.setTitle("Bloblines");
		mainWindow.setVisible(true);
		mainWindow.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				stopServer();
				super.windowClosing(e);
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainWindow = new JFrame();
		mainWindow.setTitle("Bloblines");
		mainWindow.setSize(800, 600);
		mainWindow.setMinimumSize(new Dimension(800, 600));
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.getContentPane().setLayout(
				new BoxLayout(mainWindow.getContentPane(), BoxLayout.Y_AXIS));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setMinimumSize(new Dimension(800, 500));
		tabbedPane.setPreferredSize(new Dimension(800, 500));
		tabbedPane.setMaximumSize(new Dimension(800, 500));
		mainWindow.getContentPane().add(tabbedPane);
		tabbedPane.add("Server", new ServerPanel(this));

		textAreaMessages = new JTextPane();
		JScrollPane scroll = new JScrollPane(textAreaMessages);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		mainWindow.getContentPane().add(scroll);
		// frmBloblines.getContentPane().add(textAreaMessages);
		textAreaMessages.setText("Welcome to Bloblines");
		textAreaMessages.setEditable(false);
		textAreaMessages.setFocusable(false);
	}

	public void startServer() {
		info("Starting server...");
		server.start();
		info("Server started");
	}

	public void stopServer() {
		info("Stoping server...");
		server.stop();
		tabbedPane.remove(gamePanel);
		player = null;
		info("Server stopped");
	}

	public void connect(String login) {
		if (player != null) {
			error("Already connected as " + player);
			return;
		}
		if (!server.started) {
			error("Server not running");
		}
		player = server.connect(login);
		gamePanel = (GamePanel) tabbedPane.add("Game", new GamePanel(this,
				server, player));
		tabbedPane.setSelectedComponent(gamePanel);
		info("Connected to " + server.name + " as " + player.name);
	}

	public boolean isServerStarted() {
		return server.started;
	}

	public void info(String msg) {
		addMessage(msg, null);
	}

	public void error(String errorMsg) {
		SimpleAttributeSet style = new SimpleAttributeSet();
		StyleConstants.setForeground(style, Color.RED);
		StyleConstants.setBackground(style, Color.YELLOW);
		StyleConstants.setBold(style, true);

		addMessage(errorMsg, style);
	}

	private void addMessage(String msg, SimpleAttributeSet style) {
		try {
			textAreaMessages.getStyledDocument().insertString(
					textAreaMessages.getStyledDocument().getLength(),
					"\n" + msg, style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
