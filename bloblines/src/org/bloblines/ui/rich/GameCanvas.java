package org.bloblines.ui.rich;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.LookupOp;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.bloblines.data.event.Event;
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
		blobs = GameCanvas.makeColorTransparent(
				ImageIO.read(new File(this.getClass().getResource("blobs2.png")
						.getFile())), Color.WHITE);
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
	private final static int BLOBS_W = 150;
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
		g.drawString(b.name, x, y);
		g.drawString("Age : " + b.age, x, y + 20);
		g.drawString("Life : " + b.life + "/" + b.lifeMax, x, y + 40);
	}

	private final static int MAP_X = SPACING + BLOBS_W + SPACING;
	private final static int MAP_Y = SPACING;

	private final static int CELL_SIZE = 32;

	private BufferedImage tileset;
	private int MAP_TILE_SIZE = 16;

	private Pos TILE_GRASS = new Pos(16, 16);
	private Pos TILE_FOREST = new Pos(112, 64);
	private Pos TILE_MOUNTAIN = new Pos(224, 96);
	private Pos TILE_WATER = new Pos(64, 128);

	private BufferedImage blobs;
	private int TILE_BLOB_SIZE = 38;
	private Pos TILE_BLOB = new Pos(108, 36);

	/**
	 * Utility method
	 * @param srcTile
	 * @param dstCell
	 * @param g
	 */
	private void drawMapAt(Pos srcTile, Pos dstCell, Graphics g) {
		drawSomethingAt(tileset, srcTile, MAP_TILE_SIZE, dstCell, g);
	}

	private void drawBlobAt(Pos srcTile, int srcTileSize, Pos dstCell,
			Graphics g) {
		drawSomethingAt(blobs, srcTile, srcTileSize, dstCell, g);
	}

	private void drawSomethingAt(BufferedImage tiles, Pos srcTile,
			int srcTileSize, Pos dstCell, Graphics g) {
		int dstx = MAP_X + CELL_SIZE * dstCell.x;
		int dsty = MAP_Y + CELL_SIZE * dstCell.y;
		g.drawImage(tiles, dstx, dsty, dstx + CELL_SIZE, dsty + CELL_SIZE,
				srcTile.x, srcTile.y, srcTile.x + srcTileSize, srcTile.y
						+ srcTileSize, null);

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
				drawMapAt(TILE_GRASS, new Pos(x, y), g);
			}
		}

		Pos p = viewPos;
		for (int x = 0; x < MAP_CELLS; x++) {
			for (int y = 0; y < MAP_CELLS; y++) {
				Cell c = server.world.cells.get(new Pos(p.x + x, p.y + y));
				Pos xy = new Pos(x, y);
				if (c.type == Type.MOUNTAIN) {
					drawMapAt(TILE_MOUNTAIN, xy, g);
				} else if (c.type == Type.FOREST) {
					drawMapAt(TILE_FOREST, xy, g);
				} else {
					drawMapAt(TILE_WATER, xy, g);
				}

				if (player.area.fixedEvents.get(c.p) != null) {
					int dstx = MAP_X + CELL_SIZE * x;
					int dsty = MAP_Y + CELL_SIZE * y;

					Graphics2D g2 = (Graphics2D) g;
					byte lut[] = new byte[256];
					for (int j = 0; j < 256; j++) {
						lut[j] = (byte) (256 - j);
					}
					ByteLookupTable blut = new ByteLookupTable(0, lut);
					LookupOp lop = new LookupOp(blut, null);
					BufferedImage manaTree = tileset.getSubimage(TILE_FOREST.x,
							TILE_FOREST.y, MAP_TILE_SIZE, MAP_TILE_SIZE);
					manaTree = lop.filter(manaTree, null);

					AffineTransformOp aop = new AffineTransformOp(
							AffineTransform.getScaleInstance(CELL_SIZE
									/ MAP_TILE_SIZE, CELL_SIZE / MAP_TILE_SIZE),
							AffineTransformOp.TYPE_BICUBIC);
					g2.drawImage(manaTree, aop, dstx, dsty);
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
		drawBlobAt(TILE_BLOB, TILE_BLOB_SIZE, new Pos(player.pos.x - viewPos.x,
				player.pos.y - viewPos.y), g);
	}

	private void drawMessages(Graphics g) {
		for (Event e : player.area.events) {
			if (e.happens(player)) {
				client.info(e.text);
			}
		}
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

	public static BufferedImage makeColorTransparent(final BufferedImage im,
			final Color color) {
		final ImageFilter filter = new RGBImageFilter() {
			// the color we are looking for (white)... Alpha bits are set to opaque  
			public int markerRGB = color.getRGB() | 0xFFFFFFFF;

			public final int filterRGB(final int x, final int y, final int rgb) {
				if ((rgb | 0xFF000000) == markerRGB) {
					// Mark the alpha bits as zero - transparent  
					return 0x00FFFFFF & rgb;
				} else {
					// nothing to do  
					return rgb;
				}
			}
		};

		final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		Image image = Toolkit.getDefaultToolkit().createImage(ip);

		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
				image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		return bufferedImage;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
