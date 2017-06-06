package org.bloblines.ui.ring;

import org.bloblines.Game;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Input.Keys;

public class BackMenu extends MenuElement {

	public BackMenu() {
		super("Back", Textures.ICON_BACK);
	}

	@Override
	public boolean keyDown(int keycode, Game game) {
		if (keycode == Keys.ENTER) {
			getMenu().closeMenu();
			return true;
		}
		return false;
	}

}
