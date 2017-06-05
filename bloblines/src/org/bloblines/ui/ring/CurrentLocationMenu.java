/**
 * 
 */
package org.bloblines.ui.ring;

import java.util.ArrayList;
import java.util.List;

import org.bloblines.Game;
import org.bloblines.data.action.Action;
import org.bloblines.data.game.Player;
import org.bloblines.data.map.Location;
import org.bloblines.utils.Assets.Textures;

/**
 * SubMenu for current location actions
 */
public class CurrentLocationMenu extends SubMenu {

	public List<MenuElement> items = new ArrayList<>();

	public CurrentLocationMenu(Player p, Location location) {
		super(location.name + " - " + location.biome.toString(), Textures.ICON_CURRENT_ACTION);
		for (Action action : location.actions) {
			items.add(new ActionMenu(action, getTexture(action.type), action.description));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bloblines.ui.ring.SubMenu#getSubMenu(org.bloblines.Game)
	 */
	@Override
	public List<MenuElement> getSubMenu(Game game) {
		return items;
	}

}
