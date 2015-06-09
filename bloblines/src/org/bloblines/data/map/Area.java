package org.bloblines.data.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bloblines.utils.XY;

public class Area {
	public Set<Location> locations = new HashSet<>();
	public Map<String, Location> locationsByName = new HashMap<>();
	public Map<XY, Location> locationsByPos = new HashMap<>();

	public void addLocation(Location l) {
		l.pos.x = (int) ((int) (l.pos.x / 16)) * 16;
		l.pos.y = (int) ((int) (l.pos.y / 16)) * 16;
		locations.add(l);
		locationsByName.put(l.name, l);
		locationsByPos.put(new XY(l.pos.x / 16, l.pos.y / 16), l);
	}

	public static Area createArea() {
		Area a = new Area();

		Location l1 = new Location();
		l1.name = "Location 1";
		l1.description = "First location of the game";
		l1.pos = new XY(50, 50);
		a.addLocation(l1);

		Location l2 = new Location();
		l2.name = "Location 2";
		l2.description = "Second location of the game";
		l2.pos = new XY(750, 230);
		a.addLocation(l2);

		Location l3 = new Location();
		l3.name = "Location 3";
		l3.description = "Third location of the game";
		l3.pos = new XY(630, 880);
		a.addLocation(l3);

		return a;
	}
}
