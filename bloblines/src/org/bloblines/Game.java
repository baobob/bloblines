package org.bloblines;

import org.bloblines.data.game.Player;
import org.bloblines.ui.BlobMenu;
import org.bloblines.ui.map.BlobOverworld;
import org.bloblines.utils.Assets;
import org.bloblines.utils.XY;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Game extends com.badlogic.gdx.Game {

	public SpriteBatch batch;

	public Assets assets;

	public Player player;

	public BlobOverworld world;

	public void create() {
		// Force assets loading. We can do something with a pretty progress bar when it gets too long.
		assets = new Assets();
		assets.finishLoading();

		batch = new SpriteBatch();
		this.setScreen(new BlobMenu(this));
	}

	public void start(String playerName) {
		world = new BlobOverworld(new TmxMapLoader().load("world/world1.tmx"));
		player = new Player(playerName, new XY(world.area.locations.get("Start").pos));
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		assets.dispose();
		batch.dispose();
	}

}