package org.bloblines.data.action;

import org.bloblines.data.map.ActionType;
import org.bloblines.data.map.Biome;

/**
 * This is an action available to the user.
 */
public class Action {
	public String description;
	public ActionType type;
	public Biome biome;

	public Action(ActionType type, String description, Biome biome) {
		this.type = type;
		this.description = description;
		this.biome = biome;
	}
}
