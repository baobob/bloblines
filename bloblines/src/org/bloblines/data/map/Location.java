package org.bloblines.data.map;

import java.util.HashSet;
import java.util.Set;

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

	public XY getCoords() {
		return new XY(pos.x / 16, pos.y / 16);
	}

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

}
