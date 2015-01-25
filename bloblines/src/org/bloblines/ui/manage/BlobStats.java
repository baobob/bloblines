package org.bloblines.ui.manage;

import org.bloblines.Game;
import org.bloblines.ui.BlobScreen;
import org.bloblines.ui.map.BlobMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class BlobStats extends BlobScreen {

	private Stage stage;

	private OrthographicCamera camera;

	private TextButton btnBack;

	public BlobStats(Game b) {
		super(b);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1024, 768);

		stage = new Stage(new ScreenViewport(camera));
		btnBack = new TextButton("Back to Map", getDefaultSkin(), "default");
		btnBack.setBounds(600, 550, 200, 25);
		stage.addActor(btnBack);
		btnBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				backToMap();
			}
		});

		InputMultiplexer inputs = new InputMultiplexer(stage, this);
		Gdx.input.setInputProcessor(inputs);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		stage.act(delta);
		stage.draw();
	}

	private void backToMap() {
		game.setScreen(new BlobMap(game));
		dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ENTER) {
			backToMap();
			return true;
		}
		return super.keyDown(keycode);
	}

}
