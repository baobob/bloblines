package org.bloblines.ui.map;

import org.bloblines.Game;
import org.bloblines.data.map.Area;
import org.bloblines.data.map.Location;
import org.bloblines.ui.BlobScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class BlobWorld extends BlobScreen {

	private ShapeRenderer renderer;

	private Area area;

	private boolean dragMap = false;

	public BlobWorld(Game game) {
		super(game);

		InputMultiplexer inputs = new InputMultiplexer(stage, this);
		Gdx.input.setInputProcessor(inputs);

		renderer = new ShapeRenderer();

		initWorld();
		initPlayer();
		TextButton btn = new TextButton("bla bla", getDefaultSkin(), "default");
		btn.setBounds(50, 50, 200, 25);
		stage.addActor(btn);
	}

	private void initWorld() {
		area = Area.createArea();
		for (Location l : area.locations) {
			stage.addActor(new LocationActor(l, this, game, renderer));
		}
	}

	private void initPlayer() {

	}

	public void resize(int width, int height) {
		// See below for what true means.
		stage.getViewport().update(width, height, true);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return super.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.MIDDLE) {
			dragMap = true;
		}
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (dragMap) {
			stage.getViewport().getCamera().translate(1, 0, 0);
			stage.getViewport().getCamera().update();
			System.out.println(stage.getViewport().getCamera().position);
		}
		return super.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.MIDDLE) {
			dragMap = false;
		}
		return super.touchUp(screenX, screenY, pointer, button);
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
