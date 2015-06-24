package org.bloblines.utils;

import com.hoten.delaunay.geom.Point;

public class XY {

	public float x;
	public float y;

	public XY(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public XY(XY xy) {
		this.x = xy.x;
		this.y = xy.y;
	}

	public XY(Point p) {
		this.x = (float) p.x;
		this.y = (float) p.y;
	}

	public double distance(XY other) {
		return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
	}

	@Override
	public String toString() {
		return x + "/" + y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
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
		XY other = (XY) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

}
