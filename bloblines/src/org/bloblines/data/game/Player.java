package org.bloblines.data.game;

import java.util.ArrayList;
import java.util.List;

import org.bloblines.data.life.blob.Blob;
import org.bloblines.data.world.Area;
import org.bloblines.data.world.Area.Dir;
import org.bloblines.data.world.Cell;
import org.bloblines.data.world.Pos;

public class Player {

	public String name;

	public Pos pos;

	public Area area;

	public List<Blob> blobs = new ArrayList<>();

	public Player(String name, Area area) {
		this.name = name;
		Blob firstBorn = new Blob(null, null, area, area.spawnPoint);
		this.blobs.add(firstBorn);
		this.blobs.add(new Blob(null, null, area, area.spawnPoint));
		this.blobs.add(new Blob(null, null, area, area.spawnPoint));
		this.pos = area.spawnPoint;
		this.area = area;
	}

	public boolean move(Dir d) {
		Cell c = area.getCell(pos, d);
		if (c != null && c.isPassable()) {
			pos = c.p;
			for (Blob b : blobs) {
				b.p = c.p;
			}
			return true;
		}
		return false;
	}
}
