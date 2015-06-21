package org.bloblines.data.map;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the whole BlobWorld. Currently, it's just a container for the main Area. Maybe we'll use this later when we can move from Area
 * to Area.
 */
public class World {

	public Map<String, Area> areas = new HashMap<>();
}
