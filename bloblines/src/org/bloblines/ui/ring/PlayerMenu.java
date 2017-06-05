/**
 * 
 */
package org.bloblines.ui.ring;

import java.util.ArrayList;
import java.util.List;

import org.bloblines.Game;
import org.bloblines.data.game.Player;
import org.bloblines.data.map.Location;
import org.bloblines.utils.Assets.Textures;

/**
 * SubMenu for current location actions
 */
public class PlayerMenu extends SubMenu {

	public List<MenuElement> items = new ArrayList<>();

	public PlayerMenu(Player p, Location location) {
		super("Player menu", Textures.ICON_BOOK);

		// Status menu
		items.add(new MenuElement("Status", Textures.ICON_HEART));

		// Quest Log menu
		items.add(new MenuElement("Quest Log", Textures.ICON_QUEST_LOG));

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
