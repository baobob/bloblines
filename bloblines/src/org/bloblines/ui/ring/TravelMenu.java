package org.bloblines.ui.ring;

import org.bloblines.Game;
import org.bloblines.data.map.Location;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Input.Keys;

public class TravelMenu extends MenuElement {

	public Location destination;

	public TravelMenu(Location destination) {
		super(destination.name + " - " + destination.biome, Textures.ICON_TRAVEL);
		this.destination = destination;
	}

	@Override
	public boolean keyDown(int keycode, Game game) {
		if (keycode == Keys.ENTER) {
			game.player.travel(destination);
			getMenu().remove();
			return true;
		}
		return false;
	}

}
