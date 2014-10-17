package org.bloblines.data.game;

import org.bloblines.data.map.Area;

public class Game {

	public Player player;

	public Area area;

	public Game(String playerName) {
		player = new Player(playerName);
	}
}
