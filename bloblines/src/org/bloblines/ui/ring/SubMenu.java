package org.bloblines.ui.ring;

import java.util.List;

import org.bloblines.Game;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Input.Keys;

public abstract class SubMenu extends MenuElement {

	public SubMenu(String label, Textures t) {
		super(label, t);
	}

	@Override
	public boolean keyDown(int keycode, Game game) {
		if (keycode == Keys.ENTER) {
			List<MenuElement> subMenu = getSubMenu(game);
			if (subMenu.size() > 0) {
				getMenu().openMenu(subMenu, true);
			}
			return true;
		}
		return false;
	}

	public abstract List<MenuElement> getSubMenu(Game game);
}
