package org.bloblines.ui.ring;

import java.util.ArrayList;
import java.util.List;

import org.bloblines.utils.Assets;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MenuGroup extends Group {

	private static final int ELEMENTS_ANGLE = 30;

	private static final int ELEMENTS_DISTANCE = 80;

	private static final float ROTATION_DURATION = 0.3f;

	private Assets assets;

	/**
	 * Vector to position the next menu elements we'll add. We rotate this vector each time we add a new menuElement
	 */
	private Vector3 nextElementVector = new Vector3(0, ELEMENTS_DISTANCE, 0);

	public int rotationIndex = 0;

	public List<MenuElement> elements = new ArrayList<>();

	public MenuGroup(Assets assets) {
		this.assets = assets;
	}

	public void addElement(String label, Textures t) {
		MenuElement element = new MenuElement(label);
		Image menuIcon = new Image(assets.getTexture(t));
		menuIcon.setBounds(getOriginX() + nextElementVector.x - 16, getOriginY() + nextElementVector.y - 16, 32, 32);
		addActor(menuIcon);
		elements.add(element);
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
		if (rotationIndex == elements.size() - 1) {
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
