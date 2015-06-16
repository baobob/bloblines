package org.bloblines.ui.map;

import java.util.HashSet;
import java.util.Set;

import org.bloblines.Game;
import org.bloblines.data.game.Blob;
import org.bloblines.data.map.Action;
import org.bloblines.data.map.Location;
import org.bloblines.data.map.Target;
import org.bloblines.ui.BlobScreen;
import org.bloblines.utils.Assets.Textures;
import org.bloblines.utils.XY;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class BlobMap extends BlobScreen implements InputProcessor {

	private OrthographicCamera camera;

	// private MenuGroup menuGroup;

	public UiPlayer uiPlayer;

	private int debugHeight;
	private boolean showDebugPanel = false;

	private float moveMapX = 0;
	private float moveMapY = 0;
	private float prevMouseX = 0;
	private float prevMouseY = 0;
	private boolean moveMap = false;

	/** Current context menu */
	private Window contextMenu = null;

	/** Status window */
	private Window statusWindow = null;

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

		initStatusWindow();

		InputMultiplexer inputs = new InputMultiplexer(stage, this);
		Gdx.input.setInputProcessor(inputs);

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	private void initStatusWindow() {
		statusWindow = new Window("Blobs Status", getDefaultSkin());
		int margin = 50;
		int w = 300;
		statusWindow.setWidth(w);
		statusWindow.setMovable(false);
		stage.addActor(statusWindow);

		// Display blobs
		for (Blob b : game.player.blobs) {

			Table blobTable = new Table(getDefaultSkin());
			blobTable.setColor(0.5f, 0.3f, 0.8f, 0.7f);
			blobTable.add("Name").width(120);
			blobTable.add(b.name).width(220);
			blobTable.row();
			blobTable.add("Age").width(120);
			blobTable.add(String.valueOf(b.age)).width(220);
			blobTable.row();
			blobTable.add("Life").width(120);
			blobTable.add(b.lifeCurrent + "/" + b.lifeMax).width(220);
			blobTable.setWidth(w - 2 * margin);
			statusWindow.add(blobTable);
			statusWindow.add("").expandX();
			statusWindow.row();
			statusWindow.add("").height(50);
			statusWindow.row();
		}
		statusWindow.setHeight(120 * game.player.blobs.size());
		statusWindow.add("").expandY();
		statusWindow.setPosition(margin, Gdx.graphics.getHeight() - margin - statusWindow.getHeight());

	}

	// private void initIcons() {
	// Image helpImage = new Image(Game.assets.getTexture(Textures.ICON_BOOK));
	// helpImage.setBounds(Gdx.graphics.getWidth() / 2 - 30, Gdx.graphics.getHeight() - 40, 32, 32);
	// helpImage.addListener(new ClickListener() {
	// @Override
	// public void clicked(InputEvent event, float x, float y) {
	// // Display Help ?
	// }
	// });
	// stage.addActor(helpImage);
	//
	// Image menuImage = new Image(Game.assets.getTexture(Textures.ICON_STATUS));
	// menuImage.setBounds(Gdx.graphics.getWidth() / 2 + 30, Gdx.graphics.getHeight() - 40, 32, 32);
	// menuImage.addListener(new ClickListener() {
	// @Override
	// public void clicked(InputEvent event, float x, float y) {
	// game.setScreen(new BlobStats(game));
	// }
	// });
	// stage.addActor(menuImage);
	//
	// }

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
		// game.bgShapeRenderer.begin(ShapeType.Filled);
		// game.bgShapeRenderer.setColor(0.8f, 0.8f, 0.8f, 0.7f);
		// game.bgShapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// game.bgShapeRenderer.end();
		// Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private void renderForeground() {
		if (showDebugPanel) {
			// TODO : Use a Scene2D Window instead of game batch
			game.batch.begin();
			// getDefaultFont().setScale(1f);
			debugHeight = Gdx.graphics.getHeight() - 40;
			debug("camera position: " + camera.position);
			debug("mouse position: " + Gdx.input.getX() + "/" + Gdx.input.getY());
			debug("map coords: " + getMapCoordinates(Gdx.input.getX(), Gdx.input.getY()));
			game.batch.end();
		}
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
	}

	private void renderEvents(SpriteBatch batch) {
		for (Location location : game.world.area.locations) {
			Texture t = getTexture(Textures.SPRITE_LOCATION);
			if (location.getCoords().equals(getMapCoordinates(Gdx.input.getX(), Gdx.input.getY()))) {
				t = getTexture(Textures.SPRITE_LOCATION_DONE);
			}
			batch.draw(t, location.pos.x, location.pos.y);
		}
	}

	private void renderEventsLinks() {
		Set<Location> doneLocations = new HashSet<>();
		for (Location location : game.world.area.locations) {
			// if (location.done) {
			for (Target target : location.targets) {
				if (doneLocations.contains(target.destination)) {
					continue;
				}
				float x1 = location.pos.x + 8;
				float y1 = location.pos.y + 8;
				float x2 = target.destination.pos.x + 8;
				float y2 = target.destination.pos.y + 8;

				// Compute distance between mouse cursor and link
				// Link equation
				float a = y2 - y1;
				float b = x1 - x2;
				float c = -(b * y1 + a * x1);

				// Mouse coordinates in Map projection
				Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
				float xm = mousePos.x;
				float ym = mousePos.y;

				// Distance Mouse - Link
				double distance = Math.abs(a * xm + b * ym + c) / Math.sqrt(a * a + b * b);

				// Link lenght - we need to ensure mouse is "between" the 2 locations of the link and not along the line but far away from
				// the link
				double length = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
				double d1 = Math.sqrt((x1 - xm) * (x1 - xm) + (y1 - ym) * (y1 - ym));
				double d2 = Math.sqrt((x2 - xm) * (x2 - xm) + (y2 - ym) * (y2 - ym));

				// We need to ensure mouse is not hovering one of the locations
				XY mouseCoords = getMapCoordinates(Gdx.input.getX(), Gdx.input.getY());

				if (distance < 3 && (d1 < length) && (d2 < length) && !mouseCoords.equals(location.getCoords())
						&& !mouseCoords.equals(target.destination.getCoords())) {
					game.bgShapeRenderer.setColor(0, 0.5f, 0, 0);
				}
				game.bgShapeRenderer.rectLine(x1, y1, x2, y2, 5);
				game.bgShapeRenderer.setColor(Color.WHITE);
			}
			doneLocations.add(location);
		}
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
		menu.setHeight(240);
		menu.clearChildren();
		if (location.pos.equals(uiPlayer.getPos())) {
			// Current location contextual menu
			for (Action a : location.actions) {
				ActionMenu l = new ActionMenu(a);
				menu.add(l).width(150).padBottom(10);
				menu.row();
			}
		} else {
			// Distant location
			MenuLabel info = new MenuLabel("Information");
			menu.add(info).width(150).padBottom(10);
			menu.row();

			for (Target t : location.targets) {
				if (t.destination.pos.equals(uiPlayer.getPos())) {
					MenuLabel move = new TravelMenu(location);
					menu.add(move).width(150).padBottom(10);
					menu.row();
				}
			}

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

	public class MenuLabel extends Label {

		public MenuLabel(CharSequence text) {
			super(text, getDefaultSkin());
			addListener(new ClickListener() {
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
			setWrap(true);
		}
	}

	public class ActionMenu extends MenuLabel {
		public Action action;

		public ActionMenu(Action a) {
			super(a.description);
			action = a;
			addListener(new ClickListener(Input.Buttons.LEFT) {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					contextMenu.remove();
					event.setBubbles(false);
					game.launchAction(action);
				}
			});
		}
	}

	public class TravelMenu extends MenuLabel {
		public Location destination;

		public TravelMenu(Location d) {
			super("Move here");
			destination = d;
			addListener(new ClickListener(Input.Buttons.LEFT) {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					game.player.location = destination;
					game.player.pos = destination.pos;
					contextMenu.remove();
					event.setBubbles(false);
				}
			});
		}
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
		if (keycode == Keys.F1) {
			showDebugPanel = !showDebugPanel;
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
				if (contextMenu != null) {
					contextMenu.remove();
				}
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
