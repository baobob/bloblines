package org.bloblines.data.map;

import java.util.HashMap;
import java.util.Map;

import org.bloblines.mapgen.MapGenerator;

/**
 * Represents the whole BlobWorld. Currently, it's just a container for the main Area. Maybe we'll use this later when we can move from Area
 * to Area.
 */
public class World {

	/** Test purposes values */
	private static final long SEED = 38;
	private static final int WIDTH = 4096;
	private static final int HEIGHT = 3072;
	private static final int EVENTS = 200;

	public Map<String, Area> areas = new HashMap<>();

	public Area startArea = null;

	public World() {
		MapGenerator generator = new MapGenerator(SEED, WIDTH, HEIGHT, EVENTS);
		startArea = generator.generate("Initial Map");
		areas.put(startArea.name, startArea);
		startArea.setRandomStart(generator.random);
		startArea.buildPixmap();
	}
}
