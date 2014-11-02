package org.bloblines.data.game;

import java.util.ArrayList;
import java.util.List;

import org.bloblines.data.map.Location;
import org.bloblines.utils.XY;

public class Player {

	public String name;
	public List<Blob> blobs;
	public Location location;
	public XY pos;

	public Player(String playerName, Location startPos) {
		name = playerName;
		location = startPos;
		pos = new XY(location.pos);
		blobs = new ArrayList<>();
	}
}
