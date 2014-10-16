package org.bloblines.data.map;

import java.util.Set;

/**
 * This is a target possible inside a location.
 * 
 * Targets contain both the destination location as well as the different required data.
 */
public class Target {
	public Location destination;
	public Set<Requirement> requirements;

	public Target() {
	}

	public Target(Location location) {
		destination = location;
	}
}
