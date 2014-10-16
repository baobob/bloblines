package org.bloblines.data.map;

import java.util.List;

/**
 * This is a target possible inside a location.
 * 
 * Targets contain both the destination location as well as the different
 * required data.
 */
public class Target {
	public Location destination;
	public List<Requirement> requirements;
}
