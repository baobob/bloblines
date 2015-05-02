package org.bloblines.data.map;

import java.util.HashMap;
import java.util.Map;

import org.bloblines.utils.XY;

public class Area {
	public Map<String, Location> locations = new HashMap<String, Location>();

	public static Area createArea() {
		Area a = new Area();

		Location l1 = new Location();
		l1.name = "Location 1";
		l1.description = "First location of the game";
		l1.pos = new XY(50, 50);
		a.locations.put(l1.name, l1);

		Location l2 = new Location();
		l2.name = "Location 2";
		l2.description = "Second location of the game";
		l2.pos = new XY(750, 230);
		a.locations.put(l2.name, l2);

		Location l3 = new Location();
		l3.name = "Location 3";
		l3.description = "Third location of the game";
		l3.pos = new XY(630, 880);
		a.locations.put(l3.name, l3);

		return a;
	}
}
