package org.bloblines.ui.map;

import org.bloblines.ui.ring.RingMenuItem;
import org.bloblines.utils.Assets.Textures;

public enum MapMenu implements RingMenuItem {

	// @formatter:off
	PARAMETERS("Parameters", Textures.ICON_PARAMS),
	JOURNAL("Journal", Textures.ICON_BOOK),
	TRAVEL("Travel", Textures.ICON_LOCATION),
	ACTIONS("Actions", Textures.ICON_BLOB),
	STATUS("Status", Textures.ICON_HEART);
	// @formatter:on

	public String label;
	public Textures texture;

	MapMenu(String label, Textures texture) {
		this.label = label;
		this.texture = texture;
	}

	@Override
	public Textures texture() {
		return this.texture;
	}

	@Override
	public String label() {
		return this.label;
	}
}
