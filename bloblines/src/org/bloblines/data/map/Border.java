package org.bloblines.data.map;

/**
 * A Link object represents the border between 2 locations.
 * 
 * The border may be "passable" or not dependings on border type, player's level, currents quests, etc.
 */
public class Border {

	private boolean passable = true;

	public Location left;
	public Location right;

	public String type;

	public Border(Location left, Location right) {
		this.left = left;
		this.right = right;
	}

	public Location other(Location l) {
		if (l.equals(left)) {
			return right;
		} else if (l.equals(right)) {
			return left;
		}
		return null;
	}

	public void notPassable() {
		if (passable) {
			passable = false;
			left.passablePaths--;
			right.passablePaths--;
		}
	}

	public boolean isPassable() {
		return passable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
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
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}

}
