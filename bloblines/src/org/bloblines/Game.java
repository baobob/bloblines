package org.bloblines;

import org.bloblines.data.game.Player;
import org.bloblines.data.map.Action;
import org.bloblines.data.map.World;
import org.bloblines.ui.BlobMenu;
import org.bloblines.ui.battle.BlobBattle;
import org.bloblines.ui.manage.BlobStats;
import org.bloblines.utils.Assets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Game extends com.badlogic.gdx.Game {

	public SpriteBatch spriteBatch;

	public static Assets assets;

	public Player player;

	public World world;

	public ShapeRenderer shapeRenderer;

	@Override
	public void create() {
		shapeRenderer = new ShapeRenderer();

		// Force assets loading. We can do something with a pretty progress bar when it gets too long.
		Game.assets = new Assets();
		Game.assets.finishLoading();
		Game.assets.postLoad();

		spriteBatch = new SpriteBatch();
		this.setScreen(new BlobMenu(this));
	}

	public void start(String playerName) {
		world = new World(); // BlobOverworld(new TmxMapLoader().load("world/world1.tmx"));
		player = new Player(playerName, world.startArea, world.startArea.startLocation);
	}

	@Override
	public void render() {
		super.render(); // important!
	}

	@Override
	public void dispose() {
		Game.assets.dispose();
		spriteBatch.dispose();
	}

	public void launchAction(Action action) {
		switch (action.type) {
		case FIGHT:
			// Store current map state
			// Start fight screen
			this.setScreen(new BlobBattle(this, action));
			break;
		case SPEAK_TO_NPC:
			// delegate to BlobMap screen to display NPC conv ? or use a different screen ?
			break;
		case SHOP:
			// Store current map state
			// Start shop screen
			break;
		case STATUS:
			// Open blob status screen
			this.setScreen(new BlobStats(this));
			break;
		default:
			break;
		}
	}
}
