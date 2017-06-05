package org.bloblines.ui.ring;

import org.bloblines.Game;
import org.bloblines.data.map.Location;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Input.Keys;

public class TravelMenu extends MenuElement {

	public Location target;

	public TravelMenu(Location target) {
		super(target.name + " - " + target.biome, Textures.ICON_TRAVEL);
		this.target = target;
	}

	@Override
	public boolean keyDown(int keycode, Game game) {
		if (keycode == Keys.ENTER) {
			game.player.location = target;
			game.player.pos = target.pos;
			getMenu().remove();
			return true;
		}
		return false;
	}

}
