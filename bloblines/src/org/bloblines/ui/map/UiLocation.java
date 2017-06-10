package org.bloblines.ui.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bloblines.Game;
import org.bloblines.data.map.Biome;
import org.bloblines.data.map.Location;
import org.bloblines.data.map.World;
import org.bloblines.utils.Assets.Textures;
import org.bloblines.utils.XY;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UiLocation {

	public static int ZOOM_FACTOR = 3;
	public static int TILE_WIDTH = 65 * ZOOM_FACTOR;
	public static int TILE_HEIGHT = 89 * ZOOM_FACTOR;
	public static int TILE_HEIGHT_DIFF = 48 * ZOOM_FACTOR;
	public static int TILE_ELEVATION_DIFF = 23 * ZOOM_FACTOR;
	public static Texture LOCATION_SPRITE = Game.assets.getTexture(Textures.SPRITE_LOCATION);
	public static Texture LOCATION_SELECTED_SPRITE = Game.assets.getTexture(Textures.SPRITE_LOCATION_SELECTED);

	public static int LOCATION_WIDTH = 65;
	public static int LOCATION_HEIGHT = LOCATION_WIDTH * LOCATION_SPRITE.getHeight() / LOCATION_SPRITE.getWidth();;

	public Location location;
	public boolean selected;

	public Map<XY, Textures> randomElements;

	public UiLocation(Location l) {
		this.location = l;
		this.randomElements = new HashMap<>();
		initRandomVisualElements();
	}

	private void initRandomVisualElements() {
		switch (location.biome) {
		case OCEAN:
			if (World.RANDOM.nextInt(10) < 3) {
				randomElements.put(new XY(World.RANDOM.nextInt(50) * ZOOM_FACTOR, (35 + World.RANDOM.nextInt(40)) * ZOOM_FACTOR),
						Textures.TILE_ELT_WAVE);
			}
			if (World.RANDOM.nextInt(10) < 2) {
				randomElements.put(new XY(World.RANDOM.nextInt(50) * ZOOM_FACTOR, (35 + World.RANDOM.nextInt(40)) * ZOOM_FACTOR),
						Textures.TILE_ELT_WAVE);
			}
			break;
		case BEACH:
			if (World.RANDOM.nextInt(10) < 3) {
				randomElements.put(new XY(World.RANDOM.nextInt(50) * ZOOM_FACTOR, (35 + World.RANDOM.nextInt(40)) * ZOOM_FACTOR),
						Textures.TILE_ELT_SAND_HILL);
			}
			if (World.RANDOM.nextInt(10) < 3) {
				Textures cactusTexture = Textures.TILE_ELT_SAND_CACTUS1;
				if (World.RANDOM.nextBoolean() && World.RANDOM.nextBoolean()) {
					cactusTexture = Textures.TILE_ELT_SAND_CACTUS2;
				} else if (World.RANDOM.nextBoolean()) {
					cactusTexture = Textures.TILE_ELT_SAND_CACTUS3;
				}
				randomElements.put(new XY(World.RANDOM.nextInt(50) * ZOOM_FACTOR, (35 + World.RANDOM.nextInt(40)) * ZOOM_FACTOR),
						cactusTexture);
			}
			break;
		case GRASSLAND:
			break;
		case HILL:
			break;
		case MOUNTAIN:
			break;

		}
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
		if (location.discovered || !MapScreen.FOG_OF_WAR) {
			renderLocation(batch);
		} else {
			renderUndiscoveredLocation(batch);
		}
	}

	private void renderLocation(SpriteBatch batch) {
		// Render tile
		Texture texture = Game.assets.getTexture(getTileTexture(location.biome));
		XY tilePos = UiLocation.getUiTileXY(location);

		batch.draw(texture, tilePos.x, tilePos.y, TILE_WIDTH, TILE_HEIGHT);

		for (Entry<XY, Textures> e : randomElements.entrySet()) {
			Texture eltTexture = Game.assets.getTexture(e.getValue());
			batch.draw(eltTexture, tilePos.x + e.getKey().x, tilePos.y + e.getKey().y, eltTexture.getWidth(), eltTexture.getHeight());
		}

		// Render location spot
		if (location.reachable) {
			XY uiLocationPos = getUiLocationXY(location);
			// batch.draw(spotTexture, uiPos.x - TILE_WIDTH / 2 - width/2, uiPos.y - TILE_HEIGHT / 2, width, height);
			batch.draw(selected ? LOCATION_SELECTED_SPRITE : LOCATION_SPRITE, uiLocationPos.x, uiLocationPos.y, LOCATION_WIDTH,
					LOCATION_HEIGHT);
			if (MapScreen.DEBUG) {
				Game.assets.getFontSmall().draw(batch, "" + location.pos.x + "/" + location.pos.y, uiLocationPos.x, uiLocationPos.y);
			}
		}
	}

	private void renderUndiscoveredLocation(SpriteBatch batch) {
		Texture texture = Game.assets.getTexture(Textures.TILE_FOG);
		XY tilePos = UiLocation.getUiTileXY(location);
		batch.draw(texture, tilePos.x, tilePos.y, TILE_WIDTH, TILE_HEIGHT);
	}
}
