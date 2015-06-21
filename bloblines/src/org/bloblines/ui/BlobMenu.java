package org.bloblines.ui;

import org.bloblines.Game;
import org.bloblines.ui.map.BlobMap;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class BlobMenu extends BlobScreen {

	private int btnYPos = 280;
	private int btnXPos = 50;

	private TextButton btnStart;
	private TextButton btnContinue;
	private TextButton btnOptions;
	private TextButton btnQuit;

	public BlobMenu(Game b) {
		super(b);

		btnStart = initTextButton("Start new game", stage);
		btnContinue = initTextButton("Continue", stage);
		btnOptions = initTextButton("Options", stage);
		btnQuit = initTextButton("Quit game", stage);

		addMenuListeners();
	}

	@Override
	public void resize(int width, int height) {
		// See below for what true means.
		stage.getViewport().update(width, height, true);
	}

	private final void startGame(String playerName) {
		game.start(playerName);
		game.setScreen(new BlobMap(game));
		dispose();
	}

	private void quitGame() {
		Gdx.app.exit();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// camera.update();

		game.spriteBatch.begin();
		game.spriteBatch.draw(getTexture(Textures.SPLASH_SCREEN), 250, -85);
		// getDefaultFont().setScale(3);
		getBiggerFont().draw(game.spriteBatch, "Bloblines", 50, Gdx.graphics.getHeight() - 50);
		// getDefaultFont().setScale(1);
		getDefaultFont().draw(game.spriteBatch, "Press Enter to quickstart as Blob Doe", 80, Gdx.graphics.getHeight() - 120);

		game.spriteBatch.end();

		stage.act(delta);
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

	private TextField nameField = new TextField("Blobonymous", getDefaultSkin());

	private void addMenuListeners() {
		btnStart.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				new Dialog("Start new game", getDefaultSkin(), "dialog") {
					{
						getContentTable().add(new Label("Enter your name:", getDefaultSkin()));
						getContentTable().add(nameField);
					}

					protected void result(Object object) {
						if (object.equals(true)) {
							startGame(nameField.getText());
						}
					}
				}.button("Cancel", false).button("Go !", true).show(stage);
				nameField.addListener(new InputListener() {
					@Override
					public boolean keyDown(InputEvent event, int keycode) {
						if (keycode == Keys.ENTER) {
							startGame(nameField.getText());
						}
						return super.keyDown(event, keycode);
					}
				});
				stage.setKeyboardFocus(nameField);
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

		InputMultiplexer inputs = new InputMultiplexer(stage, this);
		Gdx.input.setInputProcessor(inputs);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ENTER) {
			startGame("Blob Doe");
			return true;
		}
		return super.keyDown(keycode);
	}
}
