package org.bloblines;

import org.bloblines.data.game.Player;
import org.bloblines.ui.BlobMenu;
import org.bloblines.ui.map.BlobOverworld;
import org.bloblines.utils.Assets;
import org.bloblines.utils.XY;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Game extends com.badlogic.gdx.Game {

	public SpriteBatch batch;

	public static Assets assets;

	public Player player;

	public BlobOverworld world;

	public ShapeRenderer bgShapeRenderer;
	public ShapeRenderer fgShapeRenderer;

	@Override
	public void create() {
		bgShapeRenderer = new ShapeRenderer();
		fgShapeRenderer = new ShapeRenderer();

		// Force assets loading. We can do something with a pretty progress bar when it gets too long.
		Game.assets = new Assets();
		Game.assets.finishLoading();

		batch = new SpriteBatch();
		this.setScreen(new BlobMenu(this));
	}

	public void start(String playerName) {
		world = new BlobOverworld(new TmxMapLoader().load("world/world1.tmx"));
		player = new Player(playerName, new XY(world.area.locations.get("Start").pos));
	}

	@Override
	public void render() {
		super.render(); // important!
	}

	@Override
	public void dispose() {
		Game.assets.dispose();
		batch.dispose();
	}

}
