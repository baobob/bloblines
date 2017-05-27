package org.bloblines.ui.map;

import org.bloblines.Game;
import org.bloblines.data.map.Biome;
import org.bloblines.data.map.Location;
import org.bloblines.utils.Assets.Textures;
import org.bloblines.utils.XY;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UiLocation {

	public static int TILE_WIDTH = 65 * 2;
	public static int TILE_HEIGHT = 89 * 2;
	public static int TILE_HEIGHT_DIFF = 49 * 2;
	public static int TILE_ELEVATION_DIFF = 23 * 2;

	public Location location;

	public UiLocation(Location l) {
		this.location = l;
	}

	private Textures getTileTexture(Biome biome) {
		if (biome == Biome.OCEAN)
			return Textures.TILE_WATER;
		if (biome == Biome.BEACH)
			return Textures.TILE_BEACH;
		if (biome == Biome.HILL)
			return Textures.TILE_HILL;
		if (biome == Biome.MOUNTAIN)
			return Textures.TILE_MOUNTAIN;
		return Textures.TILE_GRASS;
	}

	public static XY getUiXY(Location l) {
		int x = (int) l.pos.x * TILE_WIDTH;
		if (l.pos.y % 2 == 0)
			x -= TILE_WIDTH / 2;
		int y = (int) l.pos.y * TILE_HEIGHT_DIFF + TILE_ELEVATION_DIFF * Math.max(l.elevation - 4, 0);
		return new XY(x, y);
	}

	public void render(SpriteBatch batch) {
		Texture texture = Game.assets.getTexture(getTileTexture(location.biome));
		XY uiPos = UiLocation.getUiXY(location);

		batch.draw(texture, uiPos.x, uiPos.y, TILE_WIDTH, TILE_HEIGHT);

		// Texture texture = Game.assets.getTexture(Textures.SPRITE_LOCATION);
		// Texture textureSelected = Game.assets.getTexture(Textures.SPRITE_LOCATION_SELECTED);
		//
		// Set<Location> currentLocations = game.player.location.getAccessibleNeighbors();
		//
		// if (!location.reachable) {
		// continue;
		// }
		// if (!location.discovered && !currentLocations.contains(location)) {
		// continue;
		// }
		// int width = 64;
		// int height = width * texture.getHeight() / texture.getWidth();
		//
		// if (location.equals(mouseClosestLocation) && mouseOverLocation) {
		// batch.draw(texture, location.pos.x - width / 2, location.pos.y - height / 2, width, height);
		// } else {
		// batch.draw(textureSelected, location.pos.x - width / 2, location.pos.y - height / 2, width, height);
		// }

	}
}
