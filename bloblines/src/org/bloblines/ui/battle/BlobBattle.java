package org.bloblines.ui.battle;

import org.bloblines.Game;
import org.bloblines.data.map.Action;
import org.bloblines.ui.BlobScreen;
import org.bloblines.ui.map.BlobMap;
import org.bloblines.utils.Assets.Textures;

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

public class BlobBattle extends BlobScreen {

	public Action action;

	private Stage stage;

	private OrthographicCamera camera;

	private TextButton btnBack;

	public BlobBattle(Game b, Action action) {
		super(b);
		this.action = action;

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

		game.batch.begin();
		game.batch.draw(getTexture(Textures.BATTLE_SCREEN), 200, 50);
		// getDefaultFont().setScale(3);
		getBiggerFont().draw(game.batch, "Fight !", 50, Gdx.graphics.getHeight() - 50);
		// getDefaultFont().setScale(1);
		getDefaultFont().draw(game.batch, "You fight for your life. Or fortune. Or glory. Or something else, probably worthless. ", 80,
				Gdx.graphics.getHeight() - 120);
		game.batch.end();

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
