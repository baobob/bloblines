package org.bloblines.mapgen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.bloblines.data.map.Area;
import org.bloblines.data.map.Border;
import org.bloblines.data.map.Location;

import com.hoten.delaunay.examples.TestGraphImpl;
import com.hoten.delaunay.geom.Point;
import com.hoten.delaunay.voronoi.nodename.as3delaunay.Voronoi;

public class MapGenerator {

	/**
	 * Number of Lloyd relaxations
	 */
	private static final int LLOYD_RELAXATIONS = 2;

	private static final int SEA_LEVEL = 30;

	/**
	 * Random number generator based on seed parameter
	 */
	public Random random = null;

	/** seed for Random number generator. Given as an argument to constructor */
	public long seed;

	/** width of generated map */
	public int width;
	/** height of generated map */
	public int height;
	/** number of locations in the map */
	public int locations;

	/** underlying voronoi diagram */
	private Voronoi voronoi;

	public MapGenerator(long seed, int width, int height, int events) {
		this.seed = seed;
		this.random = new Random(seed);
		this.width = width;
		this.height = height;
		this.locations = events;
	}

	public Area generate(String name) {
		// Underlying Voronoi graph
		voronoi = new Voronoi(locations, width, height, random, null);
		// Relax to have a smoother graph
		voronoi = lloydRelaxation(voronoi, LLOYD_RELAXATIONS);

		// Area area = new Area(voronoi, name);
		// area.riseMountains(random);
		// area.setBiomes(SEA_LEVEL);
		// area.setRoads(random);
		// area.addQuests(random);
		// return area;
		return null;
	}

	public void show(Area area) {
		final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = img.createGraphics();

		// draw via triangles
		for (Location l : area.locations) {
			com.badlogic.gdx.graphics.Color biomeColor = l.biome.getColor();
			Color color = new Color(biomeColor.r, biomeColor.g, biomeColor.b);
			graphics.setColor(color);
			Polygon p = new Polygon();
			// for (XY corner : l.getCorners()) {
			// p.addPoint((int) corner.x, (int) corner.y);
			// }
			graphics.fillPolygon(p);

			if (l.reachable) {
				graphics.setColor(Color.BLACK);
				graphics.fillOval((int) l.pos.x - 5, (int) l.pos.y - 5, 10, 10);
			}
			// drawPolygon(g, c, drawBiomes ? getColor(c.biome) : defaultColors[c.index]);
			// drawPolygon(pixelCenterGraphics, c, new Color(c.index));
		}

		graphics.setColor(Color.BLACK);
		for (Border border : area.borders) {
			if (border.isPassable()) {
				graphics.drawLine((int) border.left.pos.x, (int) border.left.pos.y, (int) border.right.pos.x, (int) border.right.pos.y);
			}
		}

		final JFrame frame = new JFrame() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(img, 25, 35, null);
			}
		};

		frame.setTitle("Bloblines Map");
		frame.setVisible(true);
		frame.setSize(img.getWidth() + 50, img.getHeight() + 50);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void show() {
		TestGraphImpl graph = new TestGraphImpl(voronoi, LLOYD_RELAXATIONS, random);
		final BufferedImage img = graph.createMap();

		// File file = new File(String.format("output/seed-%s-sites-%d-lloyds-%d.png",
		// new Object[] { Long.valueOf(seed), Integer.valueOf(locations), Integer.valueOf(LLOYD_RELAXATIONS) }));
		// file.mkdirs();
		// ImageIO.write(img, "PNG", file);

		final JFrame frame = new JFrame() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(img, 25, 35, null);
			}
		};

		frame.setTitle("java fortune");
		frame.setVisible(true);
		frame.setSize(img.getWidth() + 50, img.getHeight() + 50);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * Lloyd relaxation algorithm. Move points at polygon center and recreate a Voronoi.
	 * 
	 * @param v
	 *            Voronoi to relax
	 * @return Relaxed Voronoi (new instance)
	 */
	public Voronoi lloydRelaxation(Voronoi v, int numberOfRelaxations) {
		for (int i = 0; i < LLOYD_RELAXATIONS; i++) {
			ArrayList<Point> points = v.siteCoords();
			for (Point p : points) {
				ArrayList<Point> region = v.region(p);
				double x = 0;
				double y = 0;
				for (Point c : region) {
					x += c.x;
					y += c.y;
				}
				x /= region.size();
				y /= region.size();
				p.x = x;
				p.y = y;
			}
			v = new Voronoi(points, null, v.get_plotBounds());
		}
		return v;
	}

}
