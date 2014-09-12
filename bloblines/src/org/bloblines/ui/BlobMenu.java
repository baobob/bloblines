package org.bloblines.ui;

import org.bloblines.Bloblines;
import org.bloblines.ui.map.BlobMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class BlobMenu extends BlobScreen {

	private Skin skin;
	private Stage stage;
	private Texture splash;
	// private BitmapFont splashFont;

	private int btnYPos = Gdx.graphics.getHeight() - 150;
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

		splash = new Texture(Gdx.files.internal("img/hourglass.png"));
		// splash.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		// TextureRegion splashRegion = new TextureRegion(splash, 0, 0, 800,
		// 420);
		// sprite = new Sprite(region);
		// sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		// sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		// sprite.setPosition(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
		// // TTF
		// // needs
		// TrueTypeFontFactory
		// splashFont = new TrueFont(
		// Gdx.files.internal("fonts/SplashBlobsnDots.ttf"));
		skin = new Skin(Gdx.files.internal("skins/ui.json"));
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
		// b.batch.setProjectionMatrix(camera.combined);

		b.batch.begin();
		b.batch.draw(splash, 250, -85);
		b.font.setScale(3);
		b.font.draw(b.batch, "Bloblines", 50, Gdx.graphics.getHeight() - 50);

		b.batch.end();

		stage.draw();
		// TextButton button = new TextButton("Click me!", s);
		// button.draw(b.batch, 1);

		// if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.ANY_KEY)) {
		// b.setScreen(new BlobMapScreen(b));
		// dispose();
		// }
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	private TextButton initTextButton(String txt, Stage stage) {
		TextButton btn = new TextButton(txt, skin, "default");
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
		// Should we dispose something before ?
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
