package org.bloblines.mapgen;

import java.util.ArrayList;
import java.util.Random;

import org.bloblines.data.map.Area;

public class HexaMapGenerator {

	private static final int SEA_LEVEL = 3;
	private static final int HILL_LEVEL = 7;
	private static final int MOUNTAIN_LEVEL = 9;

	/**
	 * Random number generator based on seed parameter
	 */
	public Random random = null;

	/** width of generated map */
	public int width;
	/** height of generated map */
	public int height;
	/** number of locations in the map */
	public int locations;

	public HexaMapGenerator(Random random, int width, int height, int events) {
		this.random = random;
		this.width = width;
		this.height = height;
		this.locations = events;
	}

	public Area generate() {
		Area area = new Area();
		area.locations = new ArrayList<>(width * height);
		area.createLocations(width, height);
		area.riseMountains(random);
		area.setBiomes(SEA_LEVEL, HILL_LEVEL, MOUNTAIN_LEVEL);
		area.setRoads(random);
		area.addQuests(random);
		return area;
	}
}
