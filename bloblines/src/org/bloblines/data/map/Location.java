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
	public boolean done;
}
