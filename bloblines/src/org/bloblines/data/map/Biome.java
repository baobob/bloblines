package org.bloblines.data.map;

import com.badlogic.gdx.graphics.Color;

public enum Biome {

	OCEAN(Color.BLUE), BEACH(new Color(240f * 1 / 255, 200f * 1 / 255, 60f * 1 / 255, 1f)), GRASSLAND(new Color(0, 128f * 1 / 255, 0, 1f)), HILL(
			new Color(130f * 1 / 255, 70f * 1 / 255, 60f * 1 / 255, 1f)), MOUNTAIN(Color.DARK_GRAY);

	private Color color;

	private Biome(Color c) {
		this.color = c;
	}

	public Color getColor() {
		return color;
	}
}
