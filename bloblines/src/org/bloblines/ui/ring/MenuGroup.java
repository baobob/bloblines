package org.bloblines.ui.ring;

import org.bloblines.Game;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;

public class MenuGroup extends Group {

	private static final int ELEMENTS_ANGLE = 45;

	private static final int ELEMENTS_DISTANCE = 80;

	private static final float ROTATION_DURATION = 0.1f;

	private static final int ICON_SIZE = 32;

	private Game game;

	/**
	 * Vector to position the next menu elements we'll add. We rotate this vector each time we add a new menuElement
	 */
	private Vector3 nextElementVector = new Vector3(0, ELEMENTS_DISTANCE, 0);

	public int rotationIndex = 0;

	public MenuGroup(Game game) {
		this.game = game;
	}

	public void addElement(String label, Textures t) {
		MenuElement menuElement = new MenuElement(label, game.assets.getTexture(t));
		// Rotation moves object center because we rotate it aroud the bottom left corner, we need to translate it to a bit
		float tetaDegrees = getChildren().size * -ELEMENTS_ANGLE;
		double teta = Math.toRadians(-tetaDegrees);
		menuElement.setRotation(tetaDegrees);
		float cx = ICON_SIZE / 2;
		float cy = ICON_SIZE / 2;
		float cx2 = (float) (cx * Math.cos(teta) + cy * Math.sin(teta));
		float cy2 = (float) (-cx * Math.sin(teta) + cy * Math.cos(teta));
		float tx = cx - cx2;
		float ty = cy - cy2;

		menuElement.setWidth(ICON_SIZE);
		menuElement.setHeight(ICON_SIZE);
		menuElement.setCenterPosition(getOriginX() + nextElementVector.x + tx, getOriginY() + nextElementVector.y + ty);
		addActor(menuElement);
		nextElementVector.rotate(-ELEMENTS_ANGLE, 0, 0, 1);
	}

	private void updateRotation() {
		RotateToAction rotation = new RotateToAction();
		rotation.setDuration(ROTATION_DURATION);
		rotation.setRotation(rotationIndex * ELEMENTS_ANGLE);
		addAction(rotation);
	}

	/**
	 * Rotates menu to the left
	 */
	public void left() {
		if (rotationIndex == 0) {
			// We can't rotate left of the first element
			return;
		}
		rotationIndex--;
		updateRotation();
	}

	/**
	 * Rotates menu to the right
	 */
	public void right() {
		if (rotationIndex == getChildren().size - 1) {
			// We can't rotate right of the last element
			return;
		}
		rotationIndex++;
		updateRotation();
	}

	@Override
	public void setVisible(boolean visible) {
		for (Actor child : getChildren()) {
			child.setVisible(visible);
		}
		super.setVisible(visible);
	}

	public void keyDown(int keycode) {
		if (keycode == Keys.RIGHT) {
			right();
		}
		if (keycode == Keys.LEFT) {
			left();
		}
	}

}
