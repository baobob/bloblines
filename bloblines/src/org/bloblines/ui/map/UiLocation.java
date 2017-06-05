package org.bloblines.ui.map;

import org.bloblines.Game;
import org.bloblines.data.map.Biome;
import org.bloblines.data.map.Location;
import org.bloblines.utils.Assets.Textures;
import org.bloblines.utils.XY;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UiLocation {

	public static int ZOOM_FACTOR = 2;
	public static int TILE_WIDTH = 65 * ZOOM_FACTOR;
	public static int TILE_HEIGHT = 89 * ZOOM_FACTOR;
	public static int TILE_HEIGHT_DIFF = 49 * ZOOM_FACTOR;
	public static int TILE_ELEVATION_DIFF = 23 * ZOOM_FACTOR;
	public static Texture LOCATION_SPRITE = Game.assets.getTexture(Textures.SPRITE_LOCATION);
	public static int LOCATION_WIDTH = 65;
	public static int LOCATION_HEIGHT = LOCATION_WIDTH * LOCATION_SPRITE.getHeight() / LOCATION_SPRITE.getWidth();;

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

	public static XY getUiTileXY(Location l) {
		int x = (int) l.pos.x * TILE_WIDTH;
		if (l.pos.y % 2 == 0)
			x -= TILE_WIDTH / 2;
		int y = (int) l.pos.y * TILE_HEIGHT_DIFF + TILE_ELEVATION_DIFF * Math.max(l.elevation - 4, 0);
		return new XY(x, y);
	}

	public static XY getUiLocationXY(Location l) {
		XY base = getUiTileXY(l);
		float x = base.x + TILE_WIDTH / 2 - LOCATION_WIDTH / 2;
		float y = base.y + TILE_HEIGHT / 2 + LOCATION_HEIGHT / 2;
		return new XY(x, y);
	}

	public static XY getUiLocationCenterXY(Location l) {
		XY base = getUiLocationXY(l);
		return new XY(base.x + LOCATION_WIDTH / 2, base.y + LOCATION_HEIGHT / 2);
	}

	public void render(SpriteBatch batch) {
		// Render tile
		Texture texture = Game.assets.getTexture(getTileTexture(location.biome));
		XY tilePos = UiLocation.getUiTileXY(location);

		batch.draw(texture, tilePos.x, tilePos.y, TILE_WIDTH, TILE_HEIGHT);

		// Render location spot
		if (location.reachable || location.discovered) {
			XY uiLocationPos = getUiLocationXY(location);
			// batch.draw(spotTexture, uiPos.x - TILE_WIDTH / 2 - width/2, uiPos.y - TILE_HEIGHT / 2, width, height);
			batch.draw(LOCATION_SPRITE, uiLocationPos.x, uiLocationPos.y, LOCATION_WIDTH, LOCATION_HEIGHT);
			Game.assets.getFontSmall().draw(batch, "" + location.pos.x + "/" + location.pos.y, uiLocationPos.x, uiLocationPos.y);

		}

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
		//
		// if (location.equals(mouseClosestLocation) && mouseOverLocation) {
		// batch.draw(texture, location.pos.x - width / 2, location.pos.y - height / 2, width, height);
		// } else {
		// batch.draw(textureSelected, location.pos.x - width / 2, location.pos.y - height / 2, width, height);
		// }

	}
}
