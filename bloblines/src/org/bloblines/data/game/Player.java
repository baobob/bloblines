package org.bloblines.data.game;

import java.util.ArrayList;
import java.util.List;

import org.bloblines.data.map.Area;
import org.bloblines.data.map.Location;
import org.bloblines.utils.XY;

public class Player {

	public String name;
	public List<Blob> blobs = new ArrayList<Blob>();
	public Location location;
	public XY pos;
	public Area area;
	public List<Atom> atoms = new ArrayList<>();

	public Player(String playerName, Area area, Location startPos) {
		this.name = playerName;
		this.area = area;
		this.location = startPos;
		this.location.discovered = true;
		this.pos = new XY(location.pos);
		Blob b1 = new Blob();
		b1.name = "Bobby";
		b1.age = 1;
		b1.initializeAttributes(57, 5, 5, 5, 5);

		Blob b2 = new Blob();
		b2.name = "Jean-Blob";
		b2.age = 1;
		b2.initializeAttributes(52, 5, 5, 5, 5);
		this.blobs.add(b1);
		this.blobs.add(b2);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
