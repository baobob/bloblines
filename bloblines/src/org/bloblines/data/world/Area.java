package org.bloblines.data.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bloblines.data.life.LivingThing;

public class Area {

	public int height;
	public int width;

	public Pos spawnPoint;

	public Map<Pos, Cell> cells = new HashMap<Pos, Cell>();

	/**
	 * List of sub areas contained inside this area.
	 */
	public List<Area> subAreas;

	/**
	 * All registered living things currently in this area.
	 */
	public Set<LivingThing> livingThings;

	/**
	 * Future living things that will get born at the end of turn.
	 */
	public Set<LivingThing> futureLivingThings;

	/**
	 * Future dead things that will disappear at the end of turn.
	 */
	public Set<LivingThing> futureDeadThings;

	public enum Dir {
		NORTH, WEST, SOUTH, EAST
	}

	/**
	 * Construct an empty area.
	 */
	public Area(int w, int h) {
		subAreas = new ArrayList<>();
		livingThings = new HashSet<>();
		futureLivingThings = new HashSet<>();
		futureDeadThings = new HashSet<>();
		width = w;
		height = h;
	}

	/**
	 * Register a new thing to be added in the list of managed things.
	 * 
	 * @param thing the new thing to add
	 */
	public void register(LivingThing thing) {
		futureLivingThings.add(thing);
		thing.p = spawnPoint;
	}

	/**
	 * Removes a living thing from the things in this area.
	 * @param thing
	 */
	public void unregister(LivingThing thing) {
		futureDeadThings.add(thing);
	}

	/**
	 * Make all things live in this area and all sub areas.
	 */
	public void live() {
		for (LivingThing livingThing : livingThings) {
			livingThing.live();
		}
		for (Area area : subAreas) {
			area.live();
		}
		// Remove dead things from living things list
		for (LivingThing deadThing : futureDeadThings) {
			livingThings.remove(deadThing);
		}
		futureDeadThings.clear();
		// Add newly born things
		for (LivingThing babyThing : futureLivingThings) {
			livingThings.add(babyThing);
		}
		futureLivingThings.clear();
	}

	public Cell getCell(Pos p, Dir d) {
		// Map border
		if (d == Dir.NORTH && p.y == 0 || d == Dir.SOUTH && p.y == height
				|| d == Dir.WEST && p.x == 0 || d == Dir.EAST && p.x == width) {
			return null;
		}
		return cells.get(Area.getPos(p, d));
	}

	public static Pos getPos(Pos p, Dir d) {
		switch (d) {
		case NORTH:
			return new Pos(p.x, p.y - 1);
		case SOUTH:
			return new Pos(p.x, p.y + 1);
		case WEST:
			return new Pos(p.x - 1, p.y);
		case EAST:
			return new Pos(p.x + 1, p.y);
		default:
			break;
		}
		return null;
	}
}
