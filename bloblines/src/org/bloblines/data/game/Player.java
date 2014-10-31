package org.bloblines.data.game;

import java.util.ArrayList;
import java.util.List;

import org.bloblines.utils.XY;

public class Player {

	public String name;
	public List<Blob> blobs;
	public XY pos;

	public Player(String playerName, XY startPos) {
		name = playerName;
		pos = startPos;
		blobs = new ArrayList<>();
	}
}