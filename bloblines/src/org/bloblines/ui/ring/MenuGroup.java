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
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class MenuGroup extends Group {

	private static final int ELEMENTS_DISTANCE = 80;

	private static final float ROTATION_DURATION = 0.08f;

	private static final int ICON_SIZE = 32;

	/** Menu Label. This not a a menu children cause it shouldn't rotate */
	private Label lbl;

	private int elementsAngle = 35;

	private Game game;
	/**
	 * Vector to position the next menu elements we'll add. We rotate this vector each time we add a new menuElement
	 */
	private Vector3 nextElementVector = new Vector3(0, ELEMENTS_DISTANCE, 0);

	public int rotationIndex = 0;

	private List<List<MenuElement>> menuStack = new ArrayList<List<MenuElement>>();

	public MenuGroup(Game game, List<MenuElement> items) {
		this.game = game;
		openMenu(items);
	}

	public void openMenu(List<MenuElement> items) {
		if (items == null) {
			System.err.println("openMenu called with null parameter");
			return;
		}
		if (getChildren().size > 0) {
			menuStack.add(Arrays.asList((MenuElement[]) getChildren().toArray(MenuElement.class)));
			getChildren().clear();
		}
		setMenuElements(items);
	}

	public void closeMenu() {
		// Remove last
		getChildren().clear();
		setMenuElements(menuStack.get(menuStack.size() - 1));
		menuStack.remove(menuStack.size() - 1);
	}

	private void setMenuElements(List<MenuElement> items) {
		elementsAngle = 360 / items.size();
		setOrigin(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		for (MenuElement item : items) {
			addElement(item);
		}
	}

	public void addElement(MenuElement item) {
		// Rotation moves object center because we rotate it aroud the bottom left corner, we need to translate it to a bit
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
		item.setCenterPosition(getOriginX() + nextElementVector.x + tx, getOriginY() + nextElementVector.y + ty);
		addActor(item);
		nextElementVector.rotate(-elementsAngle, 0, 0, 1);
	}

	private void updateRotation() {
		RotateToAction rotation = new RotateToAction();
		rotation.setDuration(ROTATION_DURATION);
		rotation.setRotation(rotationIndex * elementsAngle);
		addAction(rotation);
		lbl.setText(getCurrentItem().label);
	}

	private MenuElement getCurrentItem() {
		return (MenuElement) getChildren().get(rotationIndex);
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
		lbl.setVisible(visible);
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
		int lineX = Gdx.graphics.getWidth() / 2 - 220;
		int lineY = Gdx.graphics.getHeight() / 2 + 150;
		fgShapeRenderer.line(lineX, lineY, lineX + 140, lineY);
		fgShapeRenderer.line(lineX + 140, lineY, lineX + 225, lineY - 48);
		fgShapeRenderer.end();
	}

	public Label getLabel() {
		int lineX = Gdx.graphics.getWidth() / 2 - 220;
		int lineY = Gdx.graphics.getHeight() / 2 + 150;

		lbl = new Label(((MenuElement) getChildren().get(0)).label, Game.assets.getSkin());
		lbl.setBounds(lineX + 10, lineY - 10, 300, 50);
		lbl.setVisible(false);
		return lbl;
	}
}
