package org.bloblines.ui.map;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class BlobMapInput extends InputAdapter {

	private BlobMapScreen s;

	public BlobMapInput(BlobMapScreen s) {
		this.s = s;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT
				|| keycode == Input.Keys.UP || keycode == Input.Keys.DOWN)
			s.player.updateAnimation();
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT
				|| keycode == Input.Keys.UP || keycode == Input.Keys.DOWN)
			s.player.updateAnimation();
		return false;
	}

	// @Override
	// public boolean scrolled(int amount) {
	// camera.zoom += amount / 10f;
	// if (camera.zoom < 0.1f)
	// camera.zoom = 0.1f;
	//
	// return false;
	// }
}
