package org.bloblines.data.game;

import java.util.ArrayList;
import java.util.List;

import org.bloblines.data.life.blob.Blob;
import org.bloblines.data.world.Area;
import org.bloblines.data.world.Pos;

public class Player {

	public String name;

	public Pos pos; 
	
	public List<Blob> blobs = new ArrayList<>();

	public Player(String name, Area area) {
		this.name = name;
		Blob firstBorn = new Blob(null, null, area);
		this.blobs.add(firstBorn);
		this.blobs.add(new Blob(null, null, area));
		this.blobs.add(new Blob(null, null, area));
		this.pos = area.spawnPoint; 
	}

}
