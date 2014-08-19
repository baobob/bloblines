package org.bloblines.ui.rich;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.bloblines.data.game.Player;
import org.bloblines.server.GameServer;

public class GamePanel extends JPanel {

	/** serialUID */
	private static final long serialVersionUID = -5711393827708697543L;

	/**
	 * Create the panel.
	 */
	public GamePanel(Bloblines client, GameServer server, Player player) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JLabel lblServerName = new JLabel(server.name);
		add(lblServerName);

		JPanel gameCanvas = new GameCanvas(client, server, player);
		add(gameCanvas);
		gameCanvas.requestFocusInWindow();

	}

}
