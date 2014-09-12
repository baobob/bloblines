package org.bloblines.ui.map;

import org.bloblines.utils.XY;

public class BlobEvent {

	public XY pos;

	public boolean visible;
	public boolean done;

	public String text;
	public String name;

	public BlobEvent(XY pos, String name, boolean visible) {
		this.pos = pos;
		this.visible = visible;
		this.name = name;
	}

}
