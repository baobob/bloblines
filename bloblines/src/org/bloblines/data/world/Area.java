package org.bloblines.data.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bloblines.data.event.Event;
import org.bloblines.data.event.FixedPosEvent;
import org.bloblines.data.event.RandomEvent;
import org.bloblines.data.world.Cell.Type;

public class Area {

	public int height;
	public int width;

	public Pos spawnPoint;

	public Map<Pos, Cell> cells = new HashMap<Pos, Cell>();

	public Set<Event> events = new HashSet<Event>();
	public Map<Pos, FixedPosEvent> fixedEvents = new HashMap<Pos, FixedPosEvent>();
	/**
	 * List of sub areas contained inside this area.
	 */
	public List<Area> subAreas;

	public enum Dir {
		NORTH, WEST, SOUTH, EAST
	}

	/**
	 * Construct an empty area.
	 */
	public Area(int w, int h) {
		subAreas = new ArrayList<>();
		width = w;
		height = h;
	}

	public Cell getCell(Pos p, Dir d) {
		// Map border
		if (d == Dir.NORTH && p.y == 0 || d == Dir.SOUTH && p.y == height
				|| d == Dir.WEST && p.x == 0 || d == Dir.EAST && p.x == width) {
			return null;
		}
		return cells.get(Area.getPos(p, d));
	}

	public void addEvent(Event e) {
		events.add(e);
		if (e instanceof FixedPosEvent) {
			FixedPosEvent fixed = (FixedPosEvent) e;
			fixedEvents.put(fixed.ePos, fixed);
		}
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

	public static Area getHardcodedWorld() {
		Area a = new Area(100, 100);
		a.spawnPoint = new Pos(17, 26);
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				Pos p = new Pos(i, j);
				Cell c = new Cell(p);
				if (i == 5 && j == 5) {
					// Spawn point is forest
					c.type = Type.FOREST;
				}
				a.cells.put(p, c);
			}
		}
		RandomEvent traveller = new RandomEvent(a, 30);
		traveller.text = "You met a traveller, he's happy";
		a.addEvent(traveller);

		RandomEvent fight = new RandomEvent(a, 30);
		fight.text = "Here comes a fight !";
		a.addEvent(fight);

		for (Cell c : a.cells.values()) {
			if (new Random().nextInt(20) == 0 && c.type == Type.FOREST) {
				FixedPosEvent manaTree = new FixedPosEvent(a, c.p);
				manaTree.text = "This is the Mana Tree (n°" + (c.p.x * c.p.y)
						+ "). Awesome !";
				a.addEvent(manaTree);
			}
		}
		return a;
	}
}
