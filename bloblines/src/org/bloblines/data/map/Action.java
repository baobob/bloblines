package org.bloblines.data.map;

/**
 * This is an action available to the user.
 */
public class Action {
	public String description;
	public ActionType type;

	public Action(ActionType type, String description) {
		this.type = type;
		this.description = description;
	}
}
