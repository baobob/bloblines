package org.bloblines.data.map;

import java.util.HashMap;
import java.util.HashSet;
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

	public Map<Border, Location> neighbors = new HashMap<>();
	public Map<Location, Border> borders = new HashMap<>();

	/** Elevation between 1 and 100. 0 is not initialized */
	public int elevation = 0;
	public boolean reachable = false;

	public Biome biome = null;
	public int passablePaths = 0;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
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
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		return true;
	}

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

	public Set<Location> getAccessibleNeighbors() {
		Set<Location> accessibleLocations = new HashSet<>();
		for (Entry<Border, Location> e : neighbors.entrySet()) {
			if (e.getKey().isPassable() && e.getValue().reachable) {
				accessibleLocations.add(e.getValue());
			}
		}
		return accessibleLocations;
	}

	public void discover() {
		discovered = true;
		for (Location neighbor : neighbors.values()) {
			neighbor.discovered = true;
		}
	}

	@Override
	public String toString() {
		return name;
	}

}
