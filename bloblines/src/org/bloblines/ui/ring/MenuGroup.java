package org.bloblines.ui.ring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bloblines.Game;
import org.bloblines.data.game.Player;
import org.bloblines.data.map.Location;
import org.bloblines.ui.map.UiLocation;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class MenuGroup extends Group {

	private static final int ELEMENTS_DISTANCE = 80;

	private static final float ROTATION_DURATION = 0.08f;

	private static final int ICON_SIZE = 48;

	private int elementsAngle = 55;

	public Game game;
	public UiLocation uiLocation;
	public MenuElement selectedItem;

	/**
	 * Vector to position the next menu elements we'll add. We rotate this vector each time we add a new menuElement
	 */
	private Vector3 nextElementVector = new Vector3(0, ELEMENTS_DISTANCE, 0);

	public int rotationIndex = 0;

	/** Stack to save parent menus when going into submenus and coming back */
	private List<MenuState> menuStack = new ArrayList<MenuState>();

	/** Description window. This not a a menu children cause it shouldn't rotate */
	private Window descWindow;

	public MenuGroup(Game game, UiLocation uiLocation, Vector3 menuPosition, Stage stage) {
		this.game = game;
		this.uiLocation = uiLocation;
		setPosition(menuPosition.x, menuPosition.y);
		initMenuComponents(stage);
		openMenu(getLocationActions(game.player, uiLocation.location), false);
		stage.addActor(this);
	}

	private void initMenuComponents(Stage stage) {
		descWindow = new Window("", Game.assets.getSkin());
		int w = 320;
		descWindow.setWidth(w);
		descWindow.setHeight(100);
		descWindow.setPosition((Gdx.graphics.getWidth() - w) / 2, 50);
		descWindow.setMovable(false);
		stage.addActor(descWindow);
	}

	public void openMenu(List<MenuElement> items, boolean saveCurrentState) {
		if (items == null) {
			System.err.println("openMenu called with null parameter");
			return;
		}
		if (saveCurrentState) {
			MenuState saveState = new MenuState(Arrays.asList((MenuElement[]) getChildren().toArray(MenuElement.class)), rotationIndex);
			menuStack.add(saveState);
		} else {
			menuStack.clear();
		}
		getChildren().clear();
		rotationIndex = 0;
		setMenuElements(items);
		updateRotation();
	}

	public void closeMenu() {
		// Remove last
		getChildren().clear();
		MenuState loadState = menuStack.get(menuStack.size() - 1);
		rotationIndex = loadState.selectedElementIndex;
		setMenuElements(loadState.elements);
		menuStack.remove(menuStack.size() - 1);
		updateRotation();
	}

	private void setMenuElements(List<MenuElement> items) {
		nextElementVector = new Vector3(0, ELEMENTS_DISTANCE, 0);
		// setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		// setOrigin(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		for (MenuElement item : items) {
			addElement(item);
		}
		updateDescWindow(null);
	}

	public void addElement(MenuElement item) {
		// Rotation moves object center because we rotate it around the bottom left corner, we need to translate it to a bit
		float tetaDegrees = getChildren().size * -elementsAngle;
		double teta = Math.toRadians(-tetaDegrees);
		item.setRotation(tetaDegrees);
		float cx = ICON_SIZE / 2;
		float cy = ICON_SIZE / 2;
		float cx2 = (float) (cx * Math.cos(teta) + cy * Math.sin(teta));
		float cy2 = (float) (-cx * Math.sin(teta) + cy * Math.cos(teta));
		float tx = cx - cx2;
		float ty = cy - cy2;

		item.setWidth(ICON_SIZE);
		item.setHeight(ICON_SIZE);
		item.setPosition(getOriginX() + nextElementVector.x + tx, getOriginY() + nextElementVector.y + ty);
		addActor(item);
		nextElementVector.rotate(-elementsAngle, 0, 0, 1);
	}

	private void updateRotation() {
		RotateToAction rotation = new RotateToAction();
		rotation.setDuration(ROTATION_DURATION);
		rotation.setRotation(rotationIndex * elementsAngle);
		addAction(rotation);
		updateDescWindow(null);
	}

	public void updateDescWindow(String description) {
		descWindow.setName(getCurrentItem().label);
		descWindow.clearChildren();
		Label text = new Label(uiLocation.location.name, Game.assets.getSkin());
		descWindow.add(text);
		if (description != null) {
			Label desc = new Label(description, Game.assets.getSkin());
			descWindow.add(desc);
		}
	}

	public MenuElement getCurrentItem() {
		return (MenuElement) getChildren().get(rotationIndex);
	}

	public void selectMenuItem(MenuElement item) {
		if (selectedItem != null) {
			selectedItem.selected = false;
		}
		selectedItem = item;
		selectedItem.selected = true;
		selectedItem.select(game);
	}

	public void rotateTo(MenuElement element) {
		for (int i = 0; i < getChildren().size; i++) {
			MenuElement child = (MenuElement) getChildren().get(i);
			if (child.equals(element)) {
				rotationIndex = i;
				updateRotation();
			} else {
				child.selected = false;
			}
		}
	}

	/**
	 * Rotates menu to the left
	 */
	public void left() {
		if (rotationIndex == 0) {
			rotationIndex = getChildren().size - 1;
		} else {
			rotationIndex--;
		}
		updateRotation();
	}

	/**
	 * Rotates menu to the right
	 */
	public void right() {
		if (rotationIndex == getChildren().size - 1) {
			rotationIndex = 0;
		} else {
			rotationIndex++;
		}
		updateRotation();
	}

	public boolean keyDown(int keycode) {
		if (keycode == Keys.RIGHT) {
			right();
			return true;
		}
		if (keycode == Keys.LEFT) {
			left();
			return true;
		}
		if (keycode == Keys.ESCAPE) {
			if (menuStack.size() > 0) {
				closeMenu();
				return true;
			}
			return false;
		}
		return getCurrentItem().keyDown(keycode, game);
	}

	public static List<MenuElement> getLocationActions(Player p, Location location) {
		List<MenuElement> items = new ArrayList<MenuElement>();
		items.add(new MenuElement("Location description", Textures.ICON_LOCATION));
		if (location.equals(p.location)) {
			// Menu for current location
			items.add(new CurrentLocationMenu(p, location));
			items.add(new PlayerMenu(p, location));
		} else if (p.location.neighbors.values().contains(location) && location.reachable) {
			items.add(new TravelMenu(location));
		}
		return items;
	}

	private class MenuState {
		public List<MenuElement> elements;
		public int selectedElementIndex;

		public MenuState(List<MenuElement> elements, int selectedElementIndex) {
			this.elements = elements;
			this.selectedElementIndex = selectedElementIndex;
		}
	}

	@Override
	public boolean remove() {
		descWindow.remove();
		return super.remove();
	}
}
