package org.bloblines.ui.ring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bloblines.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class MenuGroup extends Group {

	private static final int ELEMENTS_DISTANCE = 80;

	private static final float ROTATION_DURATION = 0.08f;

	private static final int ICON_SIZE = 32;

	private int elementsAngle = 35;

	private Game game;
	/**
	 * Vector to position the next menu elements we'll add. We rotate this vector each time we add a new menuElement
	 */
	private Vector3 nextElementVector = new Vector3(0, ELEMENTS_DISTANCE, 0);

	public int rotationIndex = 0;

	/** Stack to save parent menus when going into submenus and coming back */
	private List<MenuState> menuStack = new ArrayList<MenuState>();

	/** Description window. This not a a menu children cause it shouldn't rotate */
	private Window descWindow;

	public MenuGroup(Game game, List<MenuElement> items, Stage stage) {
		this.game = game;
		initMenuComponents(stage);
		openMenu(items, false);
		stage.addActor(this);
	}

	private void initMenuComponents(Stage stage) {
		descWindow = new Window("", Game.assets.getSkin());
		int w = 320;
		descWindow.setWidth(w);
		descWindow.setHeight(100);
		descWindow.setPosition((Gdx.graphics.getWidth() - w) / 2, 50);
		descWindow.setMovable(false);
		descWindow.setVisible(false);
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
		setOrigin(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		for (MenuElement item : items) {
			addElement(item);
		}
		updateDescWindow();
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
		// item.setCenterPosition(getOriginX() + nextElementVector.x + tx, getOriginY() + nextElementVector.y + ty);
		addActor(item);
		nextElementVector.rotate(-elementsAngle, 0, 0, 1);
	}

	private void updateRotation() {
		RotateToAction rotation = new RotateToAction();
		rotation.setDuration(ROTATION_DURATION);
		rotation.setRotation(rotationIndex * elementsAngle);
		addAction(rotation);
		updateDescWindow();
	}

	private void updateDescWindow() {
		descWindow.setName(getCurrentItem().label);
		descWindow.clearChildren();
		Label text = new Label(getCurrentItem().getDescription(), Game.assets.getSkin());
		descWindow.add(text);
	}

	public MenuElement getCurrentItem() {
		return (MenuElement) getChildren().get(rotationIndex);
	}

	public void selectMenuItem(MenuElement item) {
		item.keyDown(Keys.ENTER, game);
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

	@Override
	public void setVisible(boolean visible) {
		for (Actor child : getChildren()) {
			child.setVisible(visible);
		}
		// label.setVisible(visible);
		descWindow.setVisible(visible);
		super.setVisible(visible);
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

	public void render(ShapeRenderer fgShapeRenderer) {

		// Draw a black square to show selected menu
		fgShapeRenderer.begin(ShapeType.Line);
		fgShapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1);
		fgShapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 20, Gdx.graphics.getHeight() / 2 + 61, 40, 40);
		fgShapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 21, Gdx.graphics.getHeight() / 2 + 60, 42, 42);

		// draw a line with menu title to show selected menu
		// int lineX = Gdx.graphics.getWidth() / 2 - 220;
		// int lineY = Gdx.graphics.getHeight() / 2 + 150;
		// fgShapeRenderer.line(lineX, lineY, lineX + 140, lineY);
		// fgShapeRenderer.line(lineX + 140, lineY, lineX + 225, lineY - 48);
		fgShapeRenderer.end();
	}

	private class MenuState {
		public List<MenuElement> elements;
		public int selectedElementIndex;

		public MenuState(List<MenuElement> elements, int selectedElementIndex) {
			this.elements = elements;
			this.selectedElementIndex = selectedElementIndex;
		}
	}
}
