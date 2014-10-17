package org.bloblines.data.map;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Area {
	Set<Location> locations = new HashSet<Location>();

	public static Area createSampleMap() {
		Area area = new Area();
		Location start = new Location();
		Location forestEntrance = new Location();
		Location bridgeHouse = new Location();
		Location desertEntrance = new Location();
		Location landExploration = new Location();
		Location darkCave = new Location();
		Location carrotVillage = new Location();

		start.targets.addAll(Arrays.asList(new Target(bridgeHouse), new Target(forestEntrance), new Target(desertEntrance), new Target(
				landExploration)));
		bridgeHouse.targets.add(new Target(start));
		forestEntrance.targets.add(new Target(start));
		desertEntrance.targets.add(new Target(start));
		landExploration.targets.addAll(Arrays.asList(new Target(start), new Target(darkCave)));
		darkCave.targets.addAll(Arrays.asList(new Target(landExploration), new Target(carrotVillage)));
		carrotVillage.targets.add(new Target(darkCave));

		area.locations.addAll(Arrays.asList(start, forestEntrance, bridgeHouse, desertEntrance, landExploration, darkCave, carrotVillage));

		return area;
	}
}
