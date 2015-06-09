package org.bloblines.ui.map;

import org.bloblines.Game;
import org.bloblines.data.map.Action;
import org.bloblines.data.map.Location;
import org.bloblines.data.map.Target;
import org.bloblines.ui.BlobScreen;
import org.bloblines.utils.Assets.Textures;
import org.bloblines.utils.XY;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class BlobMap extends BlobScreen implements InputProcessor {

	private OrthographicCamera camera;

	// private MenuGroup menuGroup;

	public UiPlayer uiPlayer;

	// public enum State {
	// HELP, ACTION, MAP
	// }

	private float moveMapX = 0;
	private float moveMapY = 0;
	private float prevMouseX = 0;
	private float prevMouseY = 0;
	private boolean moveMap = false;

	/** Current context menu */
	private Window contextMenu = null;

	// public State currentState;

	public BlobMap(Game game) {
		super(game);
		uiPlayer = new UiPlayer(game.player);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.zoom = 0.2f;
		camera.position.x = uiPlayer.getCenter().x;
		camera.position.y = uiPlayer.getCenter().y;
		camera.update();

		initMenu();
		initIcons();

		// currentState = State.MAP;
		// switchMode(State.ACTION);

		InputMultiplexer inputs = new InputMultiplexer(stage, this);
		Gdx.input.setInputProcessor(inputs);

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	private void initMenu() {
		// menuGroup = new MenuGroup(game, MenuHelper.getMapMenu(), stage);
		// menuGroup.setVisible(false);
	}

	private void initIcons() {
		Image helpImage = new Image(Game.assets.getTexture(Textures.ICON_BOOK));
		helpImage.setBounds(Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50, 32, 32);
		helpImage.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// switchMode(State.HELP);
			}
		});
		stage.addActor(helpImage);

		Image menuImage = new Image(Game.assets.getTexture(Textures.ICON_LOCATION));
		menuImage.setBounds(Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 120, 32, 32);
		menuImage.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// switchMode(State.ACTION);
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

		// System.out.println("camera position: " + camera.position);
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
		SpriteBatch batch = (SpriteBatch) game.world.renderer.getBatch();
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

	int debugHeight;

	private void renderForeground() {
		game.batch.begin();
		// getDefaultFont().setScale(1f);
		debugHeight = Gdx.graphics.getHeight() - 40;
		debug("camera position: " + camera.position);
		debug("mouse position: " + Gdx.input.getX() + "/" + Gdx.input.getY());
		debug("map coords: " + getMapCoordinates(Gdx.input.getX(), Gdx.input.getY()));
		game.batch.end();
		// }
		// if (currentState == State.ACTION) {
		// renderMenu();
		// } else if (currentState == State.HELP) {
		// renderHelp();
		// }
	}

	private void debug(String message) {
		getDefaultFont().draw(game.batch, message, 50, debugHeight);
		debugHeight -= 30;
	}

	private void updatePlayer(float delta) {
		// update player - Move and stuff
		uiPlayer.update(delta);
	}

	private void updateCamera() {
		// Keep player as centered as possible
		// if (currentState == State.ACTION) {
		// float xDiff = camera.position.x - (uiPlayer.getPos().x + 8);
		// if (xDiff > 0.5) {
		// camera.position.x -= 0.5;
		// } else if (xDiff < -0.5) {
		// camera.position.x += 0.5;
		// } else {
		// camera.position.x = uiPlayer.getPos().x + 8;
		// }
		// float yDiff = camera.position.y - (uiPlayer.getPos().y + 8);
		// if (yDiff > 0.5) {
		// camera.position.y -= 0.5;
		// } else if (yDiff < -0.5) {
		// camera.position.y += 0.5;
		// } else {
		// camera.position.y = uiPlayer.getPos().y + 8;
		// }
		// camera.update();
		// } else if (currentState == State.MAP) {
		Rectangle view = game.world.renderer.getViewBounds();
		// TODO : DO NOT USE MAGIC NUMBER FOR MAP BOUNDS
		if (view.x + moveMapX > 0 && view.x + view.width + moveMapX < 480) {
			camera.position.x += moveMapX;
		}
		if (view.y + moveMapY > 0 && view.y + view.height + moveMapY < 480) {
			camera.position.y += moveMapY;
		}
		moveMapX = 0;
		moveMapY = 0;
		camera.update();
		// }
	}

	private void renderEvents(SpriteBatch batch) {
		for (Location location : game.world.area.locations) {
			Texture t = getTexture(Textures.SPRITE_LOCATION);
			if (location.done) {
				t = getTexture(Textures.SPRITE_LOCATION_DONE);
			}
			batch.draw(t, location.pos.x, location.pos.y);
		}
	}

	private void renderEventsLinks() {
		for (Location location : game.world.area.locations) {
			// if (location.done) {
			for (Target target : location.targets) {
				game.bgShapeRenderer.rectLine(location.pos.x, location.pos.y, target.destination.pos.x, target.destination.pos.y, 5);
			}
			// }
		}
		// if (menuGroup.getCurrentItem() instanceof TravelMenu) {
		// TravelMenu travelTo = (TravelMenu) menuGroup.getCurrentItem();
		// XY destinationPos = travelTo.target.destination.pos;
		// game.bgShapeRenderer.rectLine(uiPlayer.getPos().x + 8, uiPlayer.getPos().y + 8, destinationPos.x + 8, destinationPos.y + 8, 8);
		// }

	}

	private void renderPlayer(SpriteBatch batch) {
		TextureRegion frame = uiPlayer.getAnimation();
		batch.draw(frame, uiPlayer.getPos().x, uiPlayer.getPos().y, 16, 16);

	}

	private void renderMenu() {
		game.batch.begin();
		// getDefaultFont().setScale(1);
		getDefaultFont().draw(game.batch, "Menu Screen - Use LEFT and RIGHT to select and ENTER to validate (press TAB to return to game)",
				50, Gdx.graphics.getHeight() - 50);
		game.batch.end();
		// menuGroup.render(game.fgShapeRenderer);
	}

	private void renderHelp() {
		game.batch.begin();
		// getDefaultFont().setScale(1);
		getDefaultFont().draw(game.batch, "Help menu (F1) :", Gdx.graphics.getWidth() - 175, Gdx.graphics.getHeight() - 25);
		getDefaultFont().draw(game.batch, "Game menu (TAB) :", Gdx.graphics.getWidth() - 195, Gdx.graphics.getHeight() - 95);
		game.batch.end();
	}

	// private void switchMode(State switchState) {
	// if (currentState == State.HELP && switchState == State.HELP || currentState == State.ACTION && switchState == State.ACTION) {
	// // menuGroup.setVisible(false);
	// currentState = State.MAP;
	// } else if (switchState == State.HELP) {
	// // menuGroup.setVisible(false);
	// currentState = State.HELP;
	// } else if (switchState == State.ACTION) {
	// // menuGroup.setVisible(true);
	// currentState = State.ACTION;
	// }
	// if (currentState == State.ACTION) {
	// camera.position.x = uiPlayer.getPos().x + 8;
	// camera.position.y = uiPlayer.getPos().y + 8;
	// camera.update();
	// }
	// }

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.MIDDLE) {
			moveMap = true;
			return true;
		}
		if (button == Buttons.LEFT) {
			if (contextMenu != null) {
				contextMenu.remove();
			}
			XY coords = getMapCoordinates(screenX, screenY);
			Location l = game.world.area.locationsByPos.get(coords);
			if (l != null) {
				contextMenu = getContextMenu(l, screenX, screenY);
				stage.addActor(contextMenu);
			}
		}
		return false;
	}

	private Window getContextMenu(Location location, int x, int y) {
		Window menu = new Window(location.name, getDefaultSkin());
		menu.setWidth(160);
		menu.setHeight(220);
		menu.clearChildren();
		if (location.pos.equals(uiPlayer.getPos())) {
			// Current location contextual menu
			for (Action a : location.actions) {
				Label l = new Label(a.description, getDefaultSkin());
				l.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						event.setBubbles(false);
					}

					@Override
					public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
						((Label) event.getTarget()).setColor(Color.RED);
						event.setBubbles(false);
					}

					@Override
					public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
						((Label) event.getTarget()).setColor(Color.WHITE);
						event.setBubbles(false);
					}
				});
				l.setWrap(true);
				menu.add(l).width(150);
				menu.row();
			}
		} else {
			// Distant location
			Label info = new Label("Information", getDefaultSkin());
			menu.add(info);
			menu.row();
			Label move = new Label("Move here", getDefaultSkin());
			menu.add(move);
			menu.row();
		}
		menu.setPosition(x, Gdx.graphics.getHeight() - y - menu.getHeight());
		menu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				event.setBubbles(false);
				super.clicked(event, x, y);
			}
		});
		return menu;
	}

	/**
	 * Translates screen coordinates into Map coordinates
	 * 
	 * @param x
	 *            screen coordinate X
	 * @param y
	 *            screen coordinate Y
	 * @return translated coordinates based on current camera projection
	 */
	private XY getMapCoordinates(int x, int y) {
		Vector3 screenVector = new Vector3(x, y, 0);
		Vector3 worldVector = camera.unproject(screenVector).scl(1f / BlobOverworld.TILE_WIDTH);
		return new XY((int) worldVector.x, (int) worldVector.y);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.MIDDLE) {
			moveMap = false;
			prevMouseX = -1;
			prevMouseY = -1;
			return true;
		}
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		boolean handle = false;
		if (keycode == Keys.TAB) {
			// switchMode(State.ACTION);
			handle = true;
		}
		if (keycode == Keys.F1) {
			// switchMode(State.HELP);
			handle = true;
		}
		if (keycode == Keys.ESCAPE) {
			// if (currentState == State.MAP) {
			Gdx.app.exit();
			// } else if (currentState == State.ACTION) {
			// // if (!menuGroup.keyDown(keycode)) {
			// // switchMode(State.ACTION);
			// // }
			// } else if (currentState == State.HELP) {
			// switchMode(State.HELP);
			// }
			handle = true;
		}
		// if (currentState == State.ACTION) {
		// Menu should handle this
		// handle = menuGroup.keyDown(keycode);
		// }
		// if (currentState == State.MAP) {
		// if (keycode == Keys.LEFT) {
		// scrollX -= 2;
		// } else if (keycode == Keys.RIGHT) {
		// scrollX += 2;
		// } else if (keycode == Keys.DOWN) {
		// scrollY -= 2;
		// } else if (keycode == Keys.UP) {
		// scrollY += 2;
		// }
		// }
		// if (currentState != State.MAP) {
		// scrollX = 0;
		// scrollY = 0;
		// }
		return handle;
	}

	@Override
	public boolean keyUp(int keycode) {
		// if (currentState == State.MAP) {
		// if (keycode == Keys.LEFT) {
		// scrollX += 2;
		// } else if (keycode == Keys.RIGHT) {
		// scrollX -= 2;
		// } else if (keycode == Keys.DOWN) {
		// scrollY += 2;
		// } else if (keycode == Keys.UP) {
		// scrollY -= 2;
		// }
		// }
		return super.keyUp(keycode);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (moveMap) {
			if (prevMouseX > 0 && prevMouseY > 0) {
				moveMapX += (prevMouseX - screenX) / 2;
				moveMapY += (screenY - prevMouseY) / 2;
			}
			prevMouseX = screenX;
			prevMouseY = screenY;
		}
		return super.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean scrolled(int amount) {
		if (amount < 0) {
			// camera.zoom /= 1.1f;
		} else {
			// camera.zoom *= 1.1f;
		}
		// System.out.println(camera.zoom);
		return true;
	}
}
