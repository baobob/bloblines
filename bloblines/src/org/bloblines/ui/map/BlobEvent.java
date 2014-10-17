package org.bloblines.ui.map;

import org.bloblines.data.map.Location;

public class BlobEvent {
	public Location location;

	public boolean visible;
	public boolean done;

	public BlobEvent(Location location, boolean visible) {
		this.location = location;
		this.visible = visible;
	}

}
