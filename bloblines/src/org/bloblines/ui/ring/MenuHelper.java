package org.bloblines.ui.ring;

import java.util.ArrayList;
import java.util.List;

import org.bloblines.Game;
import org.bloblines.data.map.Action;
import org.bloblines.data.map.Location;
import org.bloblines.data.map.Target;
import org.bloblines.utils.Assets.Textures;

public class MenuHelper {

	public static List<MenuElement> getMapMenu() {
		List<MenuElement> items = new ArrayList<MenuElement>();
		// Current location Actions (access to sub menu based on location actions)
		items.add(new SubMenu("Current Location", Textures.ICON_CURRENT_ACTION) {
			@Override
			public List<MenuElement> getSubMenu(Game game) {
				return MenuHelper.getLocationActions(game.player.location);
			}
		});
		// Travel to ... (access to sub menu based on location targets)
		items.add(new SubMenu("Travel", Textures.ICON_TRAVEL) {
			@Override
			public List<MenuElement> getSubMenu(Game game) {
				return MenuHelper.getLocationTargets(game.player.location);
			}
		});
		// Blobs status
		items.add(new MenuElement("Blobs Status", Textures.ICON_STATUS));
		// Quest log
		items.add(new MenuElement("Quest Log", Textures.ICON_QUEST_LOG));
		return items;
	}

	public static List<MenuElement> getLocationTargets(Location location) {
		List<MenuElement> items = new ArrayList<MenuElement>();
		for (Target t : location.targets) {
			items.add(new MenuElement(t.destination.name, getTextureNumber(items.size() + 1)));
		}
		return items;
	}

	public static List<MenuElement> getLocationActions(Location location) {
		List<MenuElement> items = new ArrayList<MenuElement>();
		for (Action a : location.actions) {
			items.add(new MenuElement(a.description, getTextureNumber(items.size() + 1)));
		}
		return items;
	}

	private static Textures getTextureNumber(int i) {
		switch (i) {
		case 0:
			return Textures.ICON_0;
		case 1:
			return Textures.ICON_1;
		case 2:
			return Textures.ICON_2;
		case 3:
			return Textures.ICON_3;
		case 4:
			return Textures.ICON_4;
		case 5:
			return Textures.ICON_5;
		case 6:
			return Textures.ICON_6;
		case 7:
			return Textures.ICON_7;
		case 8:
			return Textures.ICON_8;
		case 9:
			return Textures.ICON_9;
		}
		return null;
	}

}
