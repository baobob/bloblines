package org.bloblines.data.game;

import java.util.ArrayList;
import java.util.List;

import org.bloblines.data.life.blob.Blob;
import org.bloblines.data.world.Area;

public class Player {

	public String name;

	public List<Blob> blobs = new ArrayList<>();

	public Player(String name, Area area) {
		this.name = name;
		Blob firstBorn = new Blob(null, null, area);
		this.blobs.add(firstBorn);
	}

}
