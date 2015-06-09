package org.bloblines.ui.manage;

import org.bloblines.Game;
import org.bloblines.data.game.Blob;
import org.bloblines.ui.BlobScreen;
import org.bloblines.ui.map.BlobMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class BlobStats extends BlobScreen {

	private Stage stage;

	private OrthographicCamera camera;

	private Window statusWindow;
	private TextButton btnBack;

	public BlobStats(Game game) {
		super(game);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1024, 768);

		stage = new Stage(new ScreenViewport(camera));
		initWindow();

		InputMultiplexer inputs = new InputMultiplexer(stage, this);
		Gdx.input.setInputProcessor(inputs);
	}

	private void initWindow() {
		statusWindow = new Window("Blobs Status", getDefaultSkin());
		int margin = 50;
		int w = Gdx.graphics.getWidth() - margin;
		int h = Gdx.graphics.getHeight() - 2 * margin;
		statusWindow.setWidth(w);
		statusWindow.setHeight(h);
		statusWindow.setPosition((Gdx.graphics.getWidth() - w) / 2, (Gdx.graphics.getHeight() - h + margin) / 2);
		statusWindow.setMovable(false);
		stage.addActor(statusWindow);

		btnBack = new TextButton("Back to Map", getDefaultSkin(), "default");
		btnBack.setBounds(Gdx.graphics.getWidth() - 200 - margin, margin / 2, 200, 25);
		stage.addActor(btnBack);
		btnBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				backToMap();
			}
		});

		// Display blobs
		Skin s = getDefaultSkin();
		for (Blob b : game.player.blobs) {

			Table blobTable = new Table(s);
			blobTable.setColor(0.5f, 0.3f, 0.8f, 0.7f);
			blobTable.add("Name").width(120);
			blobTable.add(b.name).width(220);
			blobTable.row();
			blobTable.add("Age").width(120);
			blobTable.add(String.valueOf(b.age)).width(220);
			blobTable.row();
			blobTable.add("Life").width(120);
			blobTable.add(b.lifeCurrent + "/" + b.lifeMax).width(220);
			blobTable.setHeight(h / 4);
			blobTable.setWidth(w - 2 * margin);
			statusWindow.add(blobTable);
			statusWindow.add("").expandX();
			statusWindow.row();
			statusWindow.add("").height(50);
			statusWindow.row();
		}
		statusWindow.add("").expandY();
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
