package org.bloblines.ui;

import org.bloblines.Bloblines;
import org.bloblines.ui.map.BlobMap;
import org.bloblines.utils.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class BlobMenu extends BlobScreen {

	private Stage stage;

	private int btnYPos = 280;
	private int btnXPos = 50;

	private TextButton btnStart;
	private TextButton btnContinue;
	private TextButton btnOptions;
	private TextButton btnQuit;

	OrthographicCamera camera;

	public BlobMenu(Bloblines b) {
		super(b);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 600);

		stage = new Stage();

		btnStart = initTextButton("Start new game", stage);
		btnContinue = initTextButton("Continue", stage);
		btnOptions = initTextButton("Options", stage);
		btnQuit = initTextButton("Quit game", stage);

		addMenuListeners();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		b.batch.begin();
		b.batch.draw(getTexture(Assets.TEXTURE_SPLASH_SCREEN), 250, -85);
		getDefaultFont().setScale(3);
		getDefaultFont().draw(b.batch, "Bloblines", 50, Gdx.graphics.getHeight() - 50);

		b.batch.end();

		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	private TextButton initTextButton(String txt, Stage stage) {
		TextButton btn = new TextButton(txt, getDefaultSkin(), "default");
		btn.setBounds(btnXPos, btnYPos, 200, 25);
		btnYPos -= 50;
		stage.addActor(btn);
		return btn;
	}

	private final void startGame() {
		b.setScreen(new BlobMap(b));
		dispose();
	}

	private void quitGame() {
		Gdx.app.exit();
	}

	private void addMenuListeners() {
		btnStart.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				startGame();
			}
		});

		btnContinue.setDisabled(true);
		btnContinue.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				btnContinue.setText("Not available yet");
			}
		});

		btnOptions.setDisabled(true);
		btnOptions.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				btnOptions.setText("Not available yet");
			}
		});

		btnQuit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				quitGame();
			}
		});
		Gdx.input.setInputProcessor(stage);

	}
}
