package org.bloblines.ui.rich;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.bloblines.data.game.Player;
import org.bloblines.data.life.blob.Blob;
import org.bloblines.data.world.Area.Dir;
import org.bloblines.data.world.Cell;
import org.bloblines.data.world.Cell.Type;
import org.bloblines.data.world.Pos;
import org.bloblines.server.GameServer;

public class GameCanvas extends JPanel implements KeyListener {

	/** serialUID */
	private static final long serialVersionUID = 9209893731544307734L;

	private Bloblines client;
	private GameServer server;
	private Player player;

	private Pos viewPos = new Pos(11, 20);

	public GameCanvas(Bloblines client, GameServer server, Player player)
			throws IOException {
		this.client = client;
		this.server = server;
		this.player = player;
		setFocusable(true);
		setOpaque(false);
		addKeyListener(this);
		tileset = ImageIO.read(new File(this.getClass()
				.getResource("overworld-tileset.png").getFile()));
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

		int x = BLOBS_X + 20;
		int y = BLOBS_Y + 20;
		for (Blob b : player.blobs) {
			drawBlobInfo(b, x, y, g);
			y += 100;
		}
	}

	private void drawBlobInfo(Blob b, int x, int y, Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString("Name : " + b.name, x, y);
		g.drawString("Age : " + b.age, x, y + 20);
		g.drawString("Life : " + b.life + "/" + b.lifeMax, x, y + 40);
	}

	private final static int MAP_X = SPACING + BLOBS_W + SPACING;
	private final static int MAP_Y = SPACING;

	private final static int CELL_SIZE = 32;

	private BufferedImage tileset;
	private int TILE_SIZE = 16;

	private Pos TILE_GRASS = new Pos(16, 16);
	private Pos TILE_FOREST = new Pos(112, 64);
	private Pos TILE_MOUNTAIN = new Pos(224, 96);
	private Pos TILE_WATER = new Pos(64, 128);

	/**
	 * Utility method
	 * @param srcTile
	 * @param dstCell
	 * @param g
	 */
	private void drawImageAt(Pos srcTile, Pos dstCell, Graphics g) {
		int dstx = MAP_X + CELL_SIZE * dstCell.x;
		int dsty = MAP_Y + CELL_SIZE * dstCell.y;

		g.drawImage(tileset, dstx, dsty, dstx + CELL_SIZE, dsty + CELL_SIZE,
				srcTile.x, srcTile.y, srcTile.x + TILE_SIZE, srcTile.y
						+ TILE_SIZE, null);

	}

	private int MAP_CELLS = 13;
	private int HALF_MAP_CELLS = MAP_CELLS / 2;

	/**
	 * We'll draw a MAP_CELLS * MAP_CELLS cell grid. This will display world map
	 * as currently known by player
	 * @param g
	 */
	private void drawMap(Graphics g) {
		// Draw background (put grass everywhere)
		for (int x = 0; x < MAP_CELLS; x++) {
			for (int y = 0; y < MAP_CELLS; y++) {
				drawImageAt(TILE_GRASS, new Pos(x, y), g);
			}
		}

		Pos p = viewPos;
		for (int xi = 0; xi < MAP_CELLS; xi++) {
			for (int yi = 0; yi < MAP_CELLS; yi++) {
				Cell c = server.world.cells.get(new Pos(p.x + xi, p.y + yi));
				if (c.type == Type.MOUNTAIN) {
					drawImageAt(TILE_MOUNTAIN, new Pos(xi, yi), g);
				} else if (c.type == Type.FOREST) {
					drawImageAt(TILE_FOREST, new Pos(xi, yi), g);
				} else {
					drawImageAt(TILE_WATER, new Pos(xi, yi), g);
				}
				//				g.fillRect(MAP_X + CELL_SIZE * xi + 1, MAP_Y + CELL_SIZE * yi
				//						+ 1, CELL_SIZE - 1, CELL_SIZE - 1);
				//g.setColor(Color.BLACK);
				//				g.drawString(c.p.x + "/" + c.p.y, MAP_X + CELL_SIZE * xi + 5,
				//						MAP_Y + CELL_SIZE * yi + 15);
				//				g.drawString(c.type.name().substring(0, 3), MAP_X + CELL_SIZE
				//						* xi + 5, MAP_Y + CELL_SIZE * yi + 25);
			}
		}

	}

	private void drawThings(Graphics g) {
		// Draw player
		g.setColor(Color.RED);
		g.fillOval(MAP_X + CELL_SIZE * (player.pos.x - viewPos.x) + 1, MAP_Y
				+ CELL_SIZE * (player.pos.y - viewPos.y) + 1, CELL_SIZE - 1,
				CELL_SIZE - 1);
	}

	private void drawMessages(Graphics g) {

	}

	private void clientMove(Dir d) {
		if (player.move(d)) {
			// Try to keep the player at the center 
			switch (d) {
			case NORTH:
				if (viewPos.y > 0
						&& (player.pos.y - viewPos.y) < HALF_MAP_CELLS) {
					viewPos.y--;
				}
				break;
			case WEST:
				if (viewPos.x > 0
						&& (player.pos.x - viewPos.x) < HALF_MAP_CELLS) {
					viewPos.x--;
				}
				break;
			case SOUTH:
				if (viewPos.y < server.world.height - MAP_CELLS
						&& (player.pos.y - viewPos.y > HALF_MAP_CELLS)) {
					viewPos.y++;
				}
				break;
			case EAST:
				if (viewPos.x < server.world.width - MAP_CELLS
						&& (player.pos.x - viewPos.x) > HALF_MAP_CELLS) {
					viewPos.x++;
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		switch (arg0.getKeyCode()) {
		case 38: // UP
			clientMove(Dir.NORTH);
			break;
		case 37: // LEFT
			clientMove(Dir.WEST);
			break;
		case 40: // DOWN
			clientMove(Dir.SOUTH);
			break;
		case 39: // RIGHT
			clientMove(Dir.EAST);
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
