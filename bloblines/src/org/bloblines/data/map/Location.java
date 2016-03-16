package org.bloblines.data.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bloblines.data.action.Action;
import org.bloblines.utils.XY;

/**
 * This is a location where various types of events can take place.
 * 
 * Locations correspond to the "event" type in the TMX map.
 */
public class Location {
	public Set<Event> events = new HashSet<Event>();
	public Set<Action> actions = new HashSet<Action>();
	public Set<Target> targets = new HashSet<Target>();

	public String name;
	public String description;
	public XY pos;

	public boolean discovered = false;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/***************************************************************************************************************************
	 * ************************************** New Graph Structure based on Voronoi *********************************************
	 * ************************************************************************************************************************/

	// public Set<Border> borders = new HashSet<Border>();
	public Map<Border, Location> neighbors = new HashMap<>();
	public Map<Location, Border> borders = new HashMap<>();

	/** Elevation between 1 and 100. 0 is not initialized */
	public int elevation = 0;
	public boolean reachable = true;

	public Biome biome = null;
	public int passablePaths = 0;

	/** corners of the Location zone. Use method to initialize AFTER we get all borders */
	private List<XY> corners = null;

	public void addBorder(Border b) {
		if (b.left.equals(this)) {
			neighbors.put(b, b.right);
			borders.put(b.right, b);
		} else {
			neighbors.put(b, b.left);
			borders.put(b.left, b);
		}
		if (b.isPassable()) {
			passablePaths++;
		}
	}

	public List<XY> getCorners() {
		if (corners == null) {
			corners = new ArrayList<>();
			List<Border> unorderedBorders = new ArrayList<>(borders.values());
			while (unorderedBorders.size() > 0) {
				Border b = unorderedBorders.remove(0);
				if (corners.size() == 0) {
					corners.add(b.leftCorner);
					corners.add(b.rightCorner);
				} else if (b.leftCorner.equals(corners.get(corners.size() - 1))) {
					corners.add(b.rightCorner);
				} else if (b.rightCorner.equals(corners.get(corners.size() - 1))) {
					corners.add(b.leftCorner);
				} else if (b.leftCorner.equals(corners.get(0))) {
					corners.add(0, b.rightCorner);
				} else if (b.rightCorner.equals(corners.get(0))) {
					corners.add(0, b.leftCorner);
				} else {
					unorderedBorders.add(b);
				}
			}
		}
		return corners;
	}

	public Set<Location> getAccessibleNeighbors() {
		Set<Location> accessibleLocations = new HashSet<>();
		for (Entry<Border, Location> e : neighbors.entrySet()) {
			if (e.getKey().isPassable() && e.getValue().reachable) {
				accessibleLocations.add(e.getValue());
			}
		}
		return accessibleLocations;
	}

	@Override
	public String toString() {
		return name;
	}

}
