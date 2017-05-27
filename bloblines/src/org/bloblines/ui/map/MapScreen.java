package org.bloblines.ui.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bloblines.Game;
import org.bloblines.data.action.Action;
import org.bloblines.data.battle.Character.Attributes;
import org.bloblines.data.game.Blob;
import org.bloblines.data.map.Area;
import org.bloblines.data.map.Border;
import org.bloblines.data.map.Location;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MapScreen extends BlobScreen implements InputProcessor {

	private OrthographicCamera camera;

	// private MenuGroup menuGroup;

	public UiPlayer uiPlayer;
	public UiArea uiArea;

	private int debugHeight;
	private boolean showDebugPanel = false;

	private float prevMouseX = 0;
	private float prevMouseY = 0;
	private boolean moveMap = false;

	/** Current area */
	private Area area;

	private Location mouseClosestLocation;
	private boolean mouseOverLocation;

	/** Current context menu */
	private Window contextMenu = null;

	/** Status window */
	private Window statusWindow = null;

	// public State currentState;

	public MapScreen(Game game) {
		super(game);
		uiPlayer = new UiPlayer(game.player);
		area = game.player.area;
		uiArea = new UiArea(area);
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.zoom = 1f;
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
			addStat("Name", b.name, blobTable);
			addStat("Age", String.valueOf(b.age), blobTable);
			addStat("Life", b.getAttributeCurrent(Attributes.HP) + "/" + b.getAttributeBase(Attributes.HP), blobTable);
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

	private void addStat(String name, String value, Table t) {
		t.add(name).width(120);
		t.add(value).width(220);
		t.row();
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

		mouseOverLocation = false;
		mouseClosestLocation = closestLocation();
		if (getMousePos().distance(mouseClosestLocation.pos) < 32) {
			mouseOverLocation = true;
		}

		game.spriteBatch.setProjectionMatrix(camera.combined);
		game.spriteBatch.begin();
		uiArea.render(game.spriteBatch);
		// renderPaths(game.spriteBatch);
		uiPlayer.render(game.spriteBatch);

		if (showDebugPanel) {
			// TODO : Use a Scene2D Window instead of game batch
			// getDefaultFont().setScale(1f);
			debugHeight = Gdx.graphics.getHeight() - 40;
			debug("camera position: " + camera.position.x + "/" + camera.position.y);
			debug("camera zoom: " + camera.zoom);
			debug("mouse position: " + Gdx.input.getX() + "/" + Gdx.input.getY());
			debug((int) (1.0f / delta) + " FPS");
		}

		game.spriteBatch.end();

		// Render UI (dialogs / buttons / etc)
		stage.act(delta);
		stage.draw();

		// System.out.println("camera position: " + camera.position);
	}

	private XY getMousePos() {
		Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		float xm = mousePos.x;
		float ym = mousePos.y;
		return new XY(xm, ym);
	}

	private Location closestLocation() {
		XY mouse = getMousePos();
		Location closestLocation = area.locations.get(0);
		double d = mouse.distance(closestLocation.pos);
		boolean changed = true;
		while (changed) {
			changed = false;
			for (Location l : closestLocation.neighbors.values()) {
				double distance = mouse.distance(l.pos);
				if (distance < d) {
					d = distance;
					closestLocation = l;
					changed = true;
					break;
				}
			}
		}
		return closestLocation;
	}

	private void debug(String message) {
		Vector3 debugPos = camera.unproject(new Vector3(50, debugHeight, 0));
		getDefaultFont().draw(game.spriteBatch, message, debugPos.x, debugPos.y);
		debugHeight -= 30;
	}

	private void updatePlayer(float delta) {
		// update player - Move and stuff
		uiPlayer.update(delta);
	}

	private void updateCamera() {
		int w2 = Gdx.graphics.getWidth() / 2;
		int h2 = Gdx.graphics.getHeight() / 2;

		camera.position.x = uiPlayer.getCenter().x;
		camera.position.y = uiPlayer.getCenter().y;
		if (camera.position.x - w2 <= 0) {
			camera.position.x = w2;
		} else if (camera.position.x + w2 >= uiArea.width) {
			camera.position.x = (float) (uiArea.width - w2);
		}
		if (camera.position.y - h2 <= 0) {
			camera.position.y = h2;
		} else if (camera.position.y + h2 >= uiArea.height) {
			camera.position.y = (float) (uiArea.height - h2);
		}
		camera.update();
	}

	private void renderPaths(SpriteBatch batch) {
		Texture texture = getTexture(Textures.SPRITE_PATH);
		Texture textureSelected = getTexture(Textures.SPRITE_PATH_SELECTED);
		Set<Location> accessibleNeighbors = game.player.location.getAccessibleNeighbors();

		Map<Location, Set<Location>> doneLocations = new HashMap<>();
		for (Location location : area.locations) {
			for (Entry<Border, Location> e : location.neighbors.entrySet()) {
				if (!e.getKey().isPassable()) {
					continue;
				}
				if (doneLocations.get(location) != null && doneLocations.get(location).contains((e.getValue()))) {
					continue;
				}
				Location left = e.getKey().left;
				Location right = e.getKey().right;
				if (!(left.discovered || accessibleNeighbors.contains(left))
						|| !(right.discovered || accessibleNeighbors.contains(right))) {
					continue;
				}

				float x1 = location.pos.x;
				float y1 = location.pos.y;
				float x2 = e.getValue().pos.x;
				float y2 = e.getValue().pos.y;

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

				Texture t = texture;
				if (distance < 10 && (d1 < length) && (d2 < length) && !mouseOverLocation) {
					t = textureSelected;
				}

				float m = (y2 - y1) / (x2 - x1);
				float angle = (float) Math.toDegrees(Math.atan(m));

				Vector2 centerVector = new Vector2(0, -10);
				centerVector = centerVector.rotate(angle);
				float x = x1;
				float y = y1;
				if (x2 < x1) {
					x = x2;
					y = y2;
				}
				x += centerVector.x;
				y += centerVector.y;

				// if (x1 <= x2) {
				batch.draw(t, x, y, 0, 0, (float) length, 20, 1, 1, angle, 0, 0, t.getWidth(), t.getHeight(), false, false);
				// } else {
				// batch.draw(t, x2, y2, 0, 0, (float) length, 20, 1, 1, angle, 0, 0, t.getWidth(), t.getHeight(), false, false);
				// }

				// game.shapeRenderer.rectLine(x1, y1, x2, y2, 5);
				// game.shapeRenderer.setColor(Color.WHITE);
				if (doneLocations.get(location) == null) {
					doneLocations.put(location, new HashSet<Location>());
				}
				if (doneLocations.get(e.getValue()) == null) {
					doneLocations.put(e.getValue(), new HashSet<Location>());
				}
				doneLocations.get(location).add(e.getValue());
				doneLocations.get(e.getValue()).add(location);
			}
		}
	}

	private void renderMenu() {
		game.spriteBatch.begin();
		// getDefaultFont().setScale(1);
		getDefaultFont().draw(game.spriteBatch,
				"Menu Screen - Use LEFT and RIGHT to select and ENTER to validate (press TAB to return to game)", 50,
				Gdx.graphics.getHeight() - 50);
		game.spriteBatch.end();
		// menuGroup.render(game.fgShapeRenderer);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT) {
			if (contextMenu != null) {
				contextMenu.remove();
			}
			if (mouseOverLocation) {
				contextMenu = getContextMenu(mouseClosestLocation, screenX, screenY);
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
		if (uiPlayer.isAtLocation(location)) {
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

			if (location.neighbors.values().contains(game.player.location) && game.player.location.borders.get(location).isPassable()) {
				MenuLabel move = new TravelMenu(location);
				menu.add(move).width(150).padBottom(10);
				menu.row();
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
					destination.discovered = true;
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
}
