package org.bloblines.data.map;

import org.bloblines.utils.XY;

/**
 * A Link object represents the border between 2 locations.
 * 
 * The border may be "passable" or not dependings on border type, player's level, currents quests, etc.
 */
public class Border {

	public boolean passable = true;

	public Location left;
	public Location right;

	public XY leftCorner;
	public XY rightCorner;

	public String type;

	public Border(Location left, Location right, XY leftCorner, XY rightCorner) {
		this.left = left;
		this.right = right;
		this.leftCorner = leftCorner;
		this.rightCorner = rightCorner;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leftCorner == null) ? 0 : leftCorner.hashCode());
		result = prime * result + ((rightCorner == null) ? 0 : rightCorner.hashCode());
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
		Border other = (Border) obj;
		if (leftCorner == null) {
			if (other.leftCorner != null)
				return false;
		} else if (!leftCorner.equals(other.leftCorner))
			return false;
		if (rightCorner == null) {
			if (other.rightCorner != null)
				return false;
		} else if (!rightCorner.equals(other.rightCorner))
			return false;
		return true;
	}

}
