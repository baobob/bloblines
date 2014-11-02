package org.bloblines.ui.ring;

import java.util.List;

import org.bloblines.Game;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Input.Keys;

public abstract class ParentMenu extends MenuElement {

	public ParentMenu(String label, Textures t) {
		super(label, t);
	}

	@Override
	public boolean keyDown(int keycode, Game game) {
		if (keycode == Keys.ENTER) {
			MenuGroup menu = (MenuGroup) getParent();
			menu.openMenu(getSubMenu(game));
			return true;
		}
		return false;
	}

	public abstract List<MenuElement> getSubMenu(Game game);
}
