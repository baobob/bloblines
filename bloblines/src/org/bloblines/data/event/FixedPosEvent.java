package org.bloblines.data.event;

import org.bloblines.data.game.Player;
import org.bloblines.data.world.Area;
import org.bloblines.data.world.Pos;

public class FixedPosEvent extends Event {

	public Pos ePos;

	public FixedPosEvent(Area a, Pos p) {
		super(a);
		ePos = p;
	}

	@Override
	public boolean happens(Player player) {
		return player.pos.equals(ePos);
	}

}
