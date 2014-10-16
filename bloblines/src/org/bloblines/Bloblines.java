package org.bloblines;

import org.bloblines.data.game.Game;
import org.bloblines.ui.BlobMenu;
import org.bloblines.utils.Assets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bloblines extends com.badlogic.gdx.Game {

	public SpriteBatch batch;

	public Assets assets;

	public Game state = null;

	public void create() {
		// Force assets loading. We can do something with a pretty progress bar when it gets too long.
		assets = new Assets();
		assets.finishLoading();

		batch = new SpriteBatch();
		this.setScreen(new BlobMenu(this));
	}

	public void startNewGame(String playerName) {
		state = new Game(playerName);
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		assets.dispose();
		batch.dispose();
	}

}
