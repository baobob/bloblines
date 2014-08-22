package org.bloblines.server;

import java.util.HashMap;
import java.util.Map;

import org.bloblines.data.game.Player;
import org.bloblines.data.world.Area;

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
		world = Area.getHardcodedWorld();
	}
}
