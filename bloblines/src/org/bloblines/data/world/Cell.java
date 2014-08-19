package org.bloblines.data.world;

import java.util.Random;

public class Cell {

	public Pos p;

	public Type type;

	public Cell(Pos p) {
		this.p = p;
		switch (new Random().nextInt(8)) {
		case 0:
			type = Type.MOUNTAIN;
			break;
		case 1:
			type = Type.WATER;
			break;
		default:
			type = Type.FOREST;
		}
	}

	public enum Type {
		WATER, FOREST, MOUNTAIN
	}
}
