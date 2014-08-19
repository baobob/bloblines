package org.bloblines.server;

import java.util.HashMap;
import java.util.Map;

import org.bloblines.data.game.Player;
import org.bloblines.data.world.Area;
import org.bloblines.data.world.Cell;
import org.bloblines.data.world.Cell.Type;
import org.bloblines.data.world.Pos;

public class GameServer {

	public String name;

	public boolean started;

	public Area world;

	private Map<String, Player> players = new HashMap<>();

	public GameServer(String serverName, Map<String, String> options) {
		this.name = serverName;
		this.started = false;
	}

	public void start() {
		loadWorld();
		started = true;
	}

	public void stop() {
		started = false;
		players.clear();
	}

	public Player connect(String login) {
		if (players.get(login) == null) {
			players.put(login, new Player(login, world));
		}
		return players.get(login);
	}

	public Player getPlayer(String player) {
		return players.get(player);
	}

	private void loadWorld() {
		world = new Area(100, 100);
		world.spawnPoint = new Pos(17, 26);
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				Pos p = new Pos(i, j);
				Cell c = new Cell(p);
				if (i == 17 && j == 26) {
					// Spawn point is forest
					c.type = Type.FOREST;
				}
				world.cells.put(p, c);
			}
		}
	}
}
