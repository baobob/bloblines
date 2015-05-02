package org.bloblines.ui.map;

import org.bloblines.Game;
import org.bloblines.data.map.Area;
import org.bloblines.data.map.Location;
import org.bloblines.ui.BlobScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class BlobWorld extends BlobScreen {

	/** World stage */
	private Stage stage;

	private ShapeRenderer renderer;

	private Area area;

	public BlobWorld(Game game) {
		super(game);

		stage = new Stage(new ScreenViewport());

		InputMultiplexer inputs = new InputMultiplexer(stage, this);
		Gdx.input.setInputProcessor(inputs);

		renderer = new ShapeRenderer();

		initWorld();
		initPlayer();
	}

	private void initWorld() {
		area = Area.createArea();
		for (Location l : area.locations.values()) {
			stage.addActor(new LocationActor(l, this, game, renderer));
		}
	}

	private void initPlayer() {

	}

	public void resize(int width, int height) {
		// See below for what true means.
		stage.getViewport().update(width, height, true);
	}

	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.begin(ShapeType.Filled);
		stage.act(delta);
		stage.draw();
		renderer.end();
	}

	public void dispose() {
		stage.dispose();
	}

}
