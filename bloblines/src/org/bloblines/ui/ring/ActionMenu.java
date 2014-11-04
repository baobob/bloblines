package org.bloblines.ui.ring;

import org.bloblines.Game;
import org.bloblines.utils.Assets.Textures;

public class ActionMenu extends MenuElement {

	public String description;

	public ActionMenu(String label, Textures t, String description) {
		super(label, t);
		this.description = description;
	}

	@Override
	public boolean keyDown(int keycode, Game game) {
		// TODO Auto-generated method stub
		return super.keyDown(keycode, game);
	}

}
