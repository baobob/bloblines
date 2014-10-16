package org.bloblines.data.map;

import java.util.Set;

/**
 * This is a location where various types of events can take place.
 * 
 * Locations correspond to the "event" type in the TMX map.
 */
public class Location {
	public Set<Event> events;
	public Set<Action> actions;
	public Set<Target> targets;

	public String description;
}
