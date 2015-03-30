package org.bloblines.ui.ring;

import org.bloblines.Game;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuElement extends Image {

	public String label;

	public MenuElement(String label, Textures t) {
		super(Game.assets.getTexture(t));
		this.label = label;
		addListener(new ClickListener(Buttons.LEFT) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				leftClick();
			}
		});
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

	/** Left click on menu item. */
	public void leftClick() {
		// We need to go through MenuGroup to get game reference and pass it to clicked menu
		getMenu().selectMenuItem(this);
	}

	// disabled method for submenus ?
}
