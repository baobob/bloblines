package org.bloblines.ui.ring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bloblines.data.map.Location;
import org.bloblines.data.map.Target;
import org.bloblines.utils.Assets;
import org.bloblines.utils.Assets.Textures;

public class MenuHelper {

	public static List<MenuElement> getMapMenu() {
		// @formatter:off
		return Arrays.asList(new MenuElement[] { 
			new MenuElement("Parameters", Textures.ICON_PARAMS),
			new MenuElement("Journal", Textures.ICON_BOOK),
			new MenuElement("Travel", Textures.ICON_LOCATION),
			new MenuElement("Actions", Textures.ICON_BLOB),
			new MenuElement("Status", Textures.ICON_HEART),
		});
		// @formatter:on
	}

	public static List<MenuElement> getLocationMenu(Assets assets, Location location) {
		List<MenuElement> items = new ArrayList<MenuElement>();
		for (Target t : location.targets) {
			items.add(new MenuElement(t.destination.name, Textures.ICON_LOCATION));
		}
		return items;
	}

}
