package org.bloblines.ui.ring;

import org.bloblines.Game;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MenuElement extends Image {

	public String label;

	public MenuElement(String label, Textures t) {
		super(Game.assets.getTexture(t));
		this.label = label;
	}

	public String getDescription() {
		return this.label;
	}

	public boolean keyDown(int keycode, Game game) {
		return false;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " - " + label;
	}

	public MenuGroup getMenu() {
		return (MenuGroup) getParent();
	}
}
