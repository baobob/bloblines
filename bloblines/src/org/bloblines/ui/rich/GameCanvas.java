package org.bloblines.ui.rich;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import org.bloblines.data.game.Player;
import org.bloblines.data.world.Cell;
import org.bloblines.data.world.Pos;
import org.bloblines.server.GameServer;

public class GameCanvas extends JPanel implements KeyListener {

	/** serialUID */
	private static final long serialVersionUID = 9209893731544307734L;

	private Bloblines client;
	private GameServer server;
	private Player player;

	private Pos viewPos = new Pos(0, 0);

	public GameCanvas(Bloblines client, GameServer server, Player player) {
		this.client = client;
		this.server = server;
		this.player = player;
		setFocusable(true);
		addKeyListener(this);
	}

	public void paintComponent(Graphics g) {
		drawBlobs(g);
		drawMap(g);
		drawThings(g);
		drawMessages(g);
	}

	private final static int SPACING = 20;

	private final static int BLOBS_X = SPACING;
	private final static int BLOBS_Y = SPACING;
	private final static int BLOBS_W = 200;
	private final static int BLOBS_H = 400;

	private void drawBlobs(Graphics g) {
		g.setColor(Color.RED);
		g.drawRect(BLOBS_X, BLOBS_Y, BLOBS_W, BLOBS_H);

		System.out.println(player.blobs);
	}

	private final static int MAP_X = SPACING + BLOBS_W + SPACING;
	private final static int MAP_Y = SPACING;
	private final static int MAP_W = 400;
	private final static int MAP_H = 400;

	private final static int CELL_SIZE = 40;

	/**
	 * We'll draw a 10 * 10 cell grid. This will display world map as currently
	 * known by player
	 * @param g
	 */
	private void drawMap(Graphics g) {
		// Frame
		g.setColor(Color.RED);
		g.drawRect(MAP_X - 1, MAP_Y - 1, MAP_W + 2, MAP_H + 2);

		// Grid
		g.setColor(Color.BLACK);
		// Vertical lines
		for (int x = MAP_X; x <= MAP_X + MAP_W; x += CELL_SIZE) {
			g.drawLine(x, MAP_Y, x, MAP_Y + MAP_H);
		}
		// Horizontal lines
		for (int y = MAP_Y; y <= MAP_Y + MAP_H; y += CELL_SIZE) {
			g.drawLine(MAP_X, y, MAP_X + MAP_W, y);
		}

		Pos p = viewPos;
		for (int xi = 0; xi < 10; xi++) {
			for (int yi = 0; yi < 10; yi++) {
				Cell c = server.world.cells.get(new Pos(p.x + xi, p.y + yi));
				g.drawString(c.p.x + "/" + c.p.y, MAP_X + CELL_SIZE * xi + 5,
						MAP_Y + CELL_SIZE * yi + 15);
				g.drawString(c.type.name().substring(0, 3), MAP_X + CELL_SIZE
						* xi + 5, MAP_Y + CELL_SIZE * yi + 25);
			}
		}

	}

	private void drawThings(Graphics g) {
		// Draw player

	}

	private void drawMessages(Graphics g) {

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		client.info("Key Pressed " + arg0.getKeyChar() + " : "
				+ arg0.getKeyCode());
		switch (arg0.getKeyCode()) {
		case 37: // LEFT
			if (viewPos.x > 0) {
				viewPos.x--;
			}
			break;
		case 38: // UP
			if (viewPos.y > 0) {
				viewPos.y--;
			}
			break;
		case 39: // RIGHT
			if (viewPos.x < server.world.width) {
				viewPos.x++;
			}
			break;
		case 40: // DOWN
			if (viewPos.y < server.world.height) {
				viewPos.y++;
			}
			break;
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}
}
