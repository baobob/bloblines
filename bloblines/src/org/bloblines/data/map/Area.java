package org.bloblines.data.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bloblines.utils.XY;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.hoten.delaunay.geom.Point;
import com.hoten.delaunay.geom.Rectangle;
import com.hoten.delaunay.voronoi.nodename.as3delaunay.Edge;
import com.hoten.delaunay.voronoi.nodename.as3delaunay.LineSegment;
import com.hoten.delaunay.voronoi.nodename.as3delaunay.Voronoi;

public class Area {
	public List<Location> locations = new ArrayList<>();
	public Map<String, Location> locationsByName = new HashMap<>();
	public Map<XY, Location> locationsByPos = new HashMap<>();

	public Area() {

	}

	public void addLocationDeprecated(Location l) {
		l.pos.x = (int) ((int) (l.pos.x / 16)) * 16;
		l.pos.y = (int) ((int) (l.pos.y / 16)) * 16;
		locations.add(l);
		locationsByName.put(l.name, l);
		locationsByPos.put(new XY(l.pos.x / 16, l.pos.y / 16), l);
	}

	public void addLocation(Location l) {
		locations.add(l);
		locationsByName.put(l.name, l);
		locationsByPos.put(l.pos, l);
	}

	public static Area createArea() {
		Area a = new Area();

		Location l1 = new Location();
		l1.name = "Location 1";
		l1.description = "First location of the game";
		l1.pos = new XY(50, 50);
		a.addLocationDeprecated(l1);

		Location l2 = new Location();
		l2.name = "Location 2";
		l2.description = "Second location of the game";
		l2.pos = new XY(750, 230);
		a.addLocationDeprecated(l2);

		Location l3 = new Location();
		l3.name = "Location 3";
		l3.description = "Third location of the game";
		l3.pos = new XY(630, 880);
		a.addLocationDeprecated(l3);

		return a;
	}

	/***************************************************************************************************************************
	 * ************************************** New Graph Structure based on Voronoi *********************************************
	 * ************************************************************************************************************************/

	public String name;

	public double width;
	public double height;

	public Set<Border> borders = new HashSet<>();

	public Location startLocation = null;

	public Pixmap pixmap;

	public Area(Voronoi v, String name) {
		this.name = name;
		Rectangle r = v.get_plotBounds();
		width = r.width;
		height = r.height;

		int index = 0;

		for (Point p : v.siteCoords()) {
			Location location = new Location();
			location.pos = new XY(p);
			location.name = "Location #" + index++;

			locationsByPos.put(location.pos, location);
			locations.add(location);
			// This initializes some stuff in voronoi graph
			v.region(p);
		}

		for (Edge e : v.edges()) {
			Location left = locationsByPos.get(new XY(e.get_leftSite().get_coord()));
			Location right = locationsByPos.get(new XY(e.get_rightSite().get_coord()));
			LineSegment borderLine = e.voronoiEdge();
			if (borderLine.p0 != null && borderLine.p1 != null) {
				Border border = new Border(left, right, new XY(borderLine.p0), new XY(borderLine.p1));
				left.addBorder(border);
				right.addBorder(border);
				borders.add(border);
			}
		}
	}

	public void riseMountains(Random random) {
		List<Integer> elevations = new ArrayList<>(locations.size());
		for (int i = 0; i < locations.size(); i++) {
			elevations.add(1 + random.nextInt(100));
		}
		Collections.sort(elevations);
		Collections.reverse(elevations);

		Location topMountain = locations.get(random.nextInt(locations.size()));
		List<Location> locationsToRise = new ArrayList<>();
		locationsToRise.add(topMountain);
		while (locationsToRise.size() > 0) {
			Location l = locationsToRise.remove(0);
			if (l.elevation > 0) {
				continue;
			}
			l.elevation = elevations.remove(0);
			for (Location n : l.neighbors.values()) {
				if (n.elevation == 0) {
					locationsToRise.add(n);
				}
			}
		}
	}

	public void setBiomes(int seaLevel) {
		for (Location l : locations) {
			if (l.elevation <= seaLevel) {
				l.biome = Biome.OCEAN;
			}
		}
		for (Location l : locations) {
			if (l.biome == null) {
				for (Location n : l.neighbors.values()) {
					if (n.biome == Biome.OCEAN) {
						l.biome = Biome.BEACH;
					}
				}
				if (l.biome == null) {
					if (l.elevation > 90) {
						l.biome = Biome.MOUNTAIN;
					} else if (l.elevation > 70) {
						l.biome = Biome.HILL;
					} else {
						l.biome = Biome.GRASSLAND;
					}
				}
			}
		}
	}

	public void setRoads(Random random) {
		for (Location l : locations) {
			if (l.biome == Biome.OCEAN || l.biome == Biome.MOUNTAIN) {
				l.reachable = false;
				for (Border b : l.borders.values()) {
					b.notPassable();
				}
			}
		}
		for (Location l : locations) {
			if (l.passablePaths == 0) {
				continue;
			}
			// We will randomly remove some roads
			int roadsToRemove = random.nextInt(l.passablePaths);
			List<Border> borders = new ArrayList<>(l.borders.values());
			while (roadsToRemove > 0) {
				Border randomBorder = borders.get(random.nextInt(borders.size()));
				if (l.passablePaths > 1 && randomBorder.other(l).passablePaths > 1) {
					randomBorder.notPassable();
				}
				roadsToRemove--;
			}
		}
	}

	public void setRandomStart(Random random) {
		while (startLocation == null || startLocation.biome != Biome.GRASSLAND) {
			startLocation = locations.get(random.nextInt(locations.size()));
		}
	}

	public void addQuests(Random random) {
		for (Location l : locations) {
			Action a = new Action(ActionType.FIGHT, "Random encouter - " + l.biome);
			l.actions.add(a);
			if (random.nextInt(5) == 0) {
				Action shopAction = new Action(ActionType.SHOP, "Enter Shop");
				l.actions.add(shopAction);
			}
			if (random.nextInt(5) == 0) {
				String npc = "farmer";
				if (l.biome == Biome.BEACH) {
					npc = "surfer";
				} else if (l.biome == Biome.HILL) {
					npc = "dwarf";
				}
				Action npcAction = new Action(ActionType.SPEAK_TO_NPC, "Talk to travelling " + npc);
				l.actions.add(npcAction);
			}
		}
	}

	public void buildPixmap() {
		pixmap = new Pixmap((int) width, (int) height, Format.RGBA8888);
		for (Location l : locations) {
			pixmap.setColor(l.biome.getColor());
			List<XY> corners = l.getCorners();
			int x1 = (int) corners.get(0).x;
			int y1 = (int) corners.get(0).y;

			int x2 = (int) corners.get(1).x;
			int y2 = (int) corners.get(1).y;

			int x3 = (int) corners.get(2).x;
			int y3 = (int) corners.get(2).y;

			pixmap.fillTriangle(x1, y1, x2, y2, x3, y3);

			for (int i = 3; i < corners.size(); i++) {
				x2 = x3;
				y2 = y3;
				x3 = (int) corners.get(i).x;
				y3 = (int) corners.get(i).y;
				pixmap.fillTriangle(x1, y1, x2, y2, x3, y3);
			}

			pixmap.setColor(Color.BLACK);
			for (Border border : borders) {
				// TODO : use 2 triangles to create a thick line
				pixmap.drawLine((int) border.leftCorner.x, (int) border.leftCorner.y, (int) border.rightCorner.x,
						(int) border.rightCorner.y);
			}

			// pixmap.setColor(Color.BLACK);
			// pixmap.fillCircle((int) l.pos.x, (int) l.pos.y, 10);

		}
	}
}
