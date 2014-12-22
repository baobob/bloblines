package org.bloblines.ui.ring;

import org.bloblines.Game;
import org.bloblines.data.map.Action;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Input.Keys;

public class ActionMenu extends MenuElement {

	public Action action;
	public String description;

	public ActionMenu(Action action, Textures t, String description) {
		super(action.type.name().substring(0, 1).toUpperCase() + action.type.name().substring(1).toLowerCase(), t);
		this.action = action;
		this.description = description;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public boolean keyDown(int keycode, Game game) {
		if (keycode == Keys.ENTER) {
			game.launchAction(action);
			return true;
		}
		return false;
	}
}
