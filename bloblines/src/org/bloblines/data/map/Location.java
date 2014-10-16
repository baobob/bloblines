package org.bloblines.data.map;

import java.util.List;

/**
 * This is a location where various types of events can take place.
 * 
 * Locations correspond to the "event" type in the TMX map.
 */
public class Location {
	public List<Event> events;
	public List<Action> actions;
	public List<Target> targets;

	public String description;
}
