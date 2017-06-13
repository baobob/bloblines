package org.bloblines.data.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bloblines.mapgen.HexaMapGenerator;

/**
 * Represents the whole BlobWorld. Currently, it's just a container for the main Area. Maybe we'll use this later when we can move from Area
 * to Area.
 */
public class World {

	/** seed for Random number generator. Given as an argument to constructor */
	private static final long SEED = 80514;
	private static final int WIDTH = 25;
	private static final int HEIGHT = 20;
	private static final int EVENTS = 30;

	public static final Random RANDOM = new Random(SEED);

	public Map<String, Area> areas = new HashMap<>();

	public Area startArea = null;

	public World() {
		HexaMapGenerator generator = new HexaMapGenerator(RANDOM, WIDTH, HEIGHT, EVENTS);
		startArea = generator.generate();
		areas.put(startArea.name, startArea);
	}
}
