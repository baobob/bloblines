package org.bloblines.ui.ring;

import org.bloblines.Game;
import org.bloblines.data.map.ActionType;
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

	public Textures getTexture(ActionType type) {
		switch (type) {
		case FIGHT:
			return Textures.ICON_SWORD;
		case STATUS:
			return Textures.ICON_HEART;
		case SPEAK_TO_NPC:
			return Textures.ICON_SPEECH;
		case SHOP:
			return Textures.ICON_SHOP;
		}
		return null;
	}

	public Textures getTextureNumber(int i) {
		switch (i) {
		case 0:
			return Textures.ICON_0;
		case 1:
			return Textures.ICON_1;
		case 2:
			return Textures.ICON_2;
		case 3:
			return Textures.ICON_3;
		case 4:
			return Textures.ICON_4;
		case 5:
			return Textures.ICON_5;
		case 6:
			return Textures.ICON_6;
		case 7:
			return Textures.ICON_7;
		case 8:
			return Textures.ICON_8;
		case 9:
			return Textures.ICON_9;
		}
		return null;
	}

}
