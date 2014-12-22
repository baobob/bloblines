package org.bloblines.ui.map;

import org.bloblines.Game;
import org.bloblines.data.map.Location;
import org.bloblines.data.map.Target;
import org.bloblines.ui.BlobScreen;
import org.bloblines.ui.ring.MenuGroup;
import org.bloblines.ui.ring.MenuHelper;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class BlobMap extends BlobScreen implements InputProcessor {

	private OrthographicCamera camera;
	private Stage stage;

	private MenuGroup menuGroup;

	public UiPlayer uiPlayer;

	public enum State {
		HELP, ACTION, MAP
	}

	private int scrollX = 0;
	private int scrollY = 0;

	public State currentState;

	private long mapOpeningTime = System.currentTimeMillis();

	public BlobMap(Game game) {
		super(game);
		uiPlayer = new UiPlayer(game.player);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.zoom = 0.2f;
		camera.position.x = uiPlayer.getPos().x + 8;
		camera.position.y = uiPlayer.getPos().y + 8;
		camera.update();
		stage = new Stage(new ScreenViewport());

		initMenu();
		initIcons();

		currentState = State.MAP;

		InputMultiplexer inputs = new InputMultiplexer(stage, this);
		Gdx.input.setInputProcessor(inputs);

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	private void initMenu() {
		menuGroup = new MenuGroup(game, MenuHelper.getMapMenu(), stage);
		menuGroup.setVisible(false);
	}

	private void initIcons() {
		Image helpImage = new Image(Game.assets.getTexture(Textures.ICON_BOOK));
		helpImage.setBounds(Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50, 32, 32);
		helpImage.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				switchMode(State.HELP);
			}
		});
		stage.addActor(helpImage);

		Image menuImage = new Image(Game.assets.getTexture(Textures.ICON_LOCATION));
		menuImage.setBounds(Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 120, 32, 32);
		menuImage.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				switchMode(State.ACTION);
			}
		});
		stage.addActor(menuImage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(100f / 255f, 100f / 255f, 250f / 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		updatePlayer(delta);
		updateCamera();

		renderBackground();
		renderForeground();

		// Render UI (dialogs / buttons / etc)
		stage.act(delta);
		stage.draw();
	}

	private void renderBackground() {
		// Render base Map
		game.world.renderer.setView(camera);
		game.world.renderer.render();

		game.bgShapeRenderer.setProjectionMatrix(camera.combined);
		game.bgShapeRenderer.begin(ShapeType.Filled);
		game.bgShapeRenderer.setColor(1, 1, 1, 1);
		renderEventsLinks();
		game.bgShapeRenderer.end();

		// Render player / events / moving stuff
		SpriteBatch batch = (SpriteBatch) game.world.renderer.getSpriteBatch();
		batch.begin();
		renderEvents(batch);
		renderPlayer(batch);
		batch.end();

		// if (currentState != State.MAP) {
		// Gdx.gl.glEnable(GL20.GL_BLEND);
		// Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		//
		// game.bgShapeRenderer.begin(ShapeType.Filled);
		// game.bgShapeRenderer.setColor(0.4f, 0.4f, 0.4f, 0.7f);
		// game.bgShapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// game.bgShapeRenderer.end();
		//
		// Gdx.gl.glDisable(GL20.GL_BLEND);
		// }
	}

	private void renderForeground() {
		if (System.currentTimeMillis() - mapOpeningTime < 5000) {
			game.batch.begin();
			getDefaultFont().setScale(1);
			getDefaultFont().draw(game.batch, "Press F1 to display HELP", 50, Gdx.graphics.getHeight() - 50);
			getDefaultFont().draw(game.batch, "Press TAB to switch to ACTION mode", 50, Gdx.graphics.getHeight() - 100);
			game.batch.end();
		}
		if (currentState == State.ACTION) {
			renderMenu();
		} else if (currentState == State.HELP) {
			renderHelp();
		}
	}

	private void updatePlayer(float delta) {
		// update player - Move and stuff
		uiPlayer.update(delta);
	}

	private void updateCamera() {
		// Keep player as centered as possible
		if (currentState == State.ACTION) {
			camera.position.x = uiPlayer.getPos().x + 8;
			camera.position.y = uiPlayer.getPos().y + 8;
			camera.update();
		} else if (currentState == State.MAP) {
			Rectangle view = game.world.renderer.getViewBounds();
			// TODO : DO NOT USE MAGIC NUMBER FOR MAP BOUNDS
			if (view.x + scrollX > 0 && view.x + scrollX < 272) {
				camera.position.x += scrollX;
			}
			if (view.y + scrollY > 0 && view.y + scrollY < 240) {
				camera.position.y += scrollY;
			}
			camera.update();
		}
	}

	private void renderEvents(SpriteBatch batch) {
		for (Location location : game.world.area.locations.values()) {
			Texture t = getTexture(Textures.SPRITE_LOCATION);
			if (location.done) {
				t = getTexture(Textures.SPRITE_LOCATION_DONE);
			}
			batch.draw(t, location.pos.x, location.pos.y);
		}
	}

	private void renderEventsLinks() {
		for (Location location : game.world.area.locations.values()) {
			if (location.done) {
				for (Target target : location.targets) {
					game.bgShapeRenderer.rectLine(location.pos.x + 8, location.pos.y + 8, target.destination.pos.x + 8,
							target.destination.pos.y + 8, 8);
				}
			}
		}
	}

	private void renderPlayer(SpriteBatch batch) {
		TextureRegion frame = uiPlayer.getAnimation();
		batch.draw(frame, uiPlayer.getPos().x, uiPlayer.getPos().y, 16, 16);

	}

	private void renderMenu() {
		game.batch.begin();
		getDefaultFont().setScale(1);
		getDefaultFont().draw(game.batch, "Menu Screen - Use LEFT and RIGHT to select and ENTER to validate (press TAB to return to game)",
				50, Gdx.graphics.getHeight() - 50);
		game.batch.end();
		menuGroup.render(game.fgShapeRenderer);
	}

	private void renderHelp() {
		game.batch.begin();
		getDefaultFont().setScale(1);
		getDefaultFont().draw(game.batch, "Help menu (F1) :", Gdx.graphics.getWidth() - 175, Gdx.graphics.getHeight() - 25);
		getDefaultFont().draw(game.batch, "Game menu (TAB) :", Gdx.graphics.getWidth() - 195, Gdx.graphics.getHeight() - 95);
		game.batch.end();
	}

	private void switchMode(State switchState) {
		if (currentState == State.HELP && switchState == State.HELP || currentState == State.ACTION && switchState == State.ACTION) {
			menuGroup.setVisible(false);
			currentState = State.MAP;
		} else if (switchState == State.HELP) {
			menuGroup.setVisible(false);
			currentState = State.HELP;
		} else if (switchState == State.ACTION) {
			menuGroup.setVisible(true);
			currentState = State.ACTION;
		}
		if (currentState == State.ACTION) {
			camera.position.x = uiPlayer.getPos().x + 8;
			camera.position.y = uiPlayer.getPos().y + 8;
			camera.update();
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		boolean handle = false;
		if (keycode == Keys.TAB) {
			switchMode(State.ACTION);
			handle = true;
		}
		if (keycode == Keys.F1) {
			switchMode(State.HELP);
			handle = true;
		}
		if (keycode == Keys.ESCAPE) {
			if (currentState == State.MAP) {
				Gdx.app.exit();
			} else if (currentState == State.ACTION) {
				if (!menuGroup.keyDown(keycode)) {
					switchMode(State.ACTION);
				}
			} else if (currentState == State.HELP) {
				switchMode(State.HELP);
			}
			handle = true;
		}
		if (currentState == State.ACTION) {
			// Menu should handle this
			handle = menuGroup.keyDown(keycode);
		}
		if (currentState == State.MAP) {
			if (keycode == Keys.LEFT) {
				scrollX -= 2;
			} else if (keycode == Keys.RIGHT) {
				scrollX += 2;
			} else if (keycode == Keys.DOWN) {
				scrollY -= 2;
			} else if (keycode == Keys.UP) {
				scrollY += 2;
			}
		}
		if (currentState != State.MAP) {
			scrollX = 0;
			scrollY = 0;
		}
		return handle;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (currentState == State.MAP) {
			if (keycode == Keys.LEFT) {
				scrollX += 2;
			} else if (keycode == Keys.RIGHT) {
				scrollX -= 2;
			} else if (keycode == Keys.DOWN) {
				scrollY += 2;
			} else if (keycode == Keys.UP) {
				scrollY -= 2;
			}
		}
		return super.keyUp(keycode);
	}

	@Override
	public boolean scrolled(int amount) {
		if (amount < 0) {
			camera.zoom /= 1.1f;
		} else {
			camera.zoom *= 1.1f;
		}
		System.out.println(camera.zoom);
		return true;
	}
}
