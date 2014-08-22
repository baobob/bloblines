package org.bloblines.data.event;

import org.bloblines.data.game.Player;
import org.bloblines.data.world.Area;

public abstract class Event {

	/** Area where this Event can happen */
	public Area area;

	public String text;

	public Event(Area a) {
		area = a;
	}

	public abstract boolean happens(Player player);

}
