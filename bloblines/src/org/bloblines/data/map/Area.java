package org.bloblines.data.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bloblines.data.action.Action;
import org.bloblines.data.action.BattleAction;
import org.bloblines.utils.XY;

public class Area {
	public double width;
	public double height;
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
	 ************************************************************************************************************************/

	public String name;

	public Set<Border> borders = new HashSet<>();

	public Location startLocation = null;

	public void createLocations(int width, int height) {
		this.width = width;
		this.height = height;
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				Location location = new Location();
				location.pos = new XY(x, y);
				location.name = "Location #" + (x + y * width);
				locationsByPos.put(location.pos, location);
				locations.add(location);
			}
		}

		for (Location l : locations) {
			if (l.pos.x < width - 1) {
				Location rightNeighbor = locationsByPos.get(new XY(l.pos.x + 1, l.pos.y));
				Border border = new Border(l, rightNeighbor);
				l.addBorder(border);
				rightNeighbor.addBorder(border);
				borders.add(border);
			}
			if (l.pos.y < height - 1 && (l.pos.y % 2 == 0 || l.pos.x < width - 1)) {
				Location bottomRightNeighbor = locationsByPos.get(new XY((l.pos.y % 2 == 0) ? l.pos.x : l.pos.x + 1, l.pos.y + 1));
				Border border = new Border(l, bottomRightNeighbor);
				l.addBorder(border);
				bottomRightNeighbor.addBorder(border);
				borders.add(border);
			}
			if (l.pos.y < height - 1 && (l.pos.y % 2 == 1 || l.pos.x > 0)) {
				Location bottomLeftNeighbor = locationsByPos.get(new XY((l.pos.y % 2 == 0) ? l.pos.x - 1 : l.pos.x, l.pos.y + 1));
				Border border = new Border(l, bottomLeftNeighbor);
				l.addBorder(border);
				bottomLeftNeighbor.addBorder(border);
				borders.add(border);
			}
		}
	}

	public void riseMountains(Random random) {
		List<Integer> elevations = new ArrayList<>(locations.size());
		for (int i = 0; i < locations.size(); i++) {
			elevations.add(1 + random.nextInt(10));
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

	public void setBiomes(int seaLevel, int hillLevel, int mountainLevel) {
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
					if (l.elevation >= mountainLevel) {
						l.biome = Biome.MOUNTAIN;
					} else if (l.elevation >= hillLevel) {
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
			Action a = new BattleAction("Random encouter", l.biome);
			l.actions.add(a);
			if (random.nextInt(5) == 0) {
				Action shopAction = new Action(ActionType.SHOP, "Enter Shop", l.biome);
				l.actions.add(shopAction);
			}
			if (random.nextInt(5) == 0) {
				String npc = "farmer";
				if (l.biome == Biome.BEACH) {
					npc = "surfer";
				} else if (l.biome == Biome.HILL) {
					npc = "dwarf";
				}
				Action npcAction = new Action(ActionType.SPEAK_TO_NPC, "Talk to travelling " + npc, l.biome);
				l.actions.add(npcAction);
			}
		}
	}
}
