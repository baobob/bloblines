package org.bloblines.data.game;

import org.bloblines.data.map.Map;

public class Game {

	public Player player;

	public Map map;

	public Game(String playerName) {
		player = new Player(playerName);
	}
}
