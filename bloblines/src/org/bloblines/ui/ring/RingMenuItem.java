package org.bloblines.ui.ring;

import org.bloblines.utils.Assets.Textures;

/**
 * This is a ring menu item. to be subclassed as an Enum.
 */
public interface RingMenuItem {
	String label();

	Textures texture();
}
