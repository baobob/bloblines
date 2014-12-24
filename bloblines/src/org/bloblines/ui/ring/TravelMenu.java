package org.bloblines.ui.ring;

import org.bloblines.Game;
import org.bloblines.data.map.Target;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Input.Keys;

public class TravelMenu extends MenuElement {

	public Target target;

	public TravelMenu(Target target, Textures t) {
		super(target.destination.name, t);
		this.target = target;
	}

	@Override
	public boolean keyDown(int keycode, Game game) {
		if (keycode == Keys.ENTER) {
			game.player.location = target.destination;
			game.player.pos = target.destination.pos; // TODO player pos should be based on location ?
			getMenu().openMenu(MenuHelper.getMapMenu());
			return true;
		}
		return false;
	}

}
