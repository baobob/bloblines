package org.bloblines;

import org.bloblines.ui.BlobMenu;
import org.bloblines.utils.Assets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bloblines extends Game {

	public SpriteBatch batch;

	public Assets assets;

	public void create() {
		// Force assets loading. We can do something with a pretty progress bar when it gets too long.
		assets = new Assets();
		assets.finishLoading();

		batch = new SpriteBatch();
		this.setScreen(new BlobMenu(this));
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		assets.dispose();
		batch.dispose();
	}

}
