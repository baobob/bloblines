package org.bloblines.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets extends AssetManager {

	private BitmapFont font = null;

	public enum Textures {
		// @formatter:off
		SPLASH_SCREEN("img/hourglass.png"), 
		
		SPRITE_LOCATION("characters/spot.png"), 
		SPRITE_LOCATION_DONE("characters/spot_done.png"), 
		
		ICON_BOOK("icons/book.png"), 
		ICON_LOCATION("icons/map.png"), 
		ICON_PARAMS("icons/cog.png"), 
		ICON_BLOB("icons/drop.png"), 
		ICON_HEART("icons/heart.png"); 
		// @formatter:on

		Textures(String path) {
			this.path = path;
		}

		private final String path;

		public String getPath() {
			return path;
		}
	}

	public Assets() {
		// Use LibGDX's default Arial font.
		font = new BitmapFont();

		for (Textures t : Textures.values()) {
			load(t.path, Texture.class);
		}

		load("skins/ui.json", Skin.class);
		// skin = new Skin(Gdx.files.internal("skins/ui.json"));

	}

	public BitmapFont getFont() {
		return font;
	}

	public Skin getSkin() {
		return get("skins/ui.json", Skin.class);
	}

	public Texture getTexture(Textures t) {
		return get(t.getPath(), Texture.class);
	}

	@Override
	public synchronized void dispose() {
		font.dispose();
		super.dispose();
	}

}
