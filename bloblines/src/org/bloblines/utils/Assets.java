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
		BATTLE_SCREEN("img/battle.jpg"), 
		
		SPRITE_LOCATION("characters/spot.png"), 
		SPRITE_LOCATION_DONE("characters/spot_done.png"), 
		
		ICON_BOOK("icons/book.png"), 
		ICON_LOCATION("icons/map.png"), 
		ICON_PARAMS("icons/cog.png"), 
		ICON_BLOB("icons/drop.png"), 
		ICON_HEART("icons/heart.png"),
		
		ICON_STATUS("icons/icons8/48/Food/protein-48.png"),
		ICON_QUEST_LOG("icons/icons8/48/Business/rules-48.png"),
		ICON_TRAVEL("icons/icons8/48/Maps/wind_rose-48.png"),
		ICON_CURRENT_ACTION("icons/icons8/48/Maps/map_marker-48.png"),
		
		ICON_0("icons/icons8/48/Alphabet/0-48.png"),
		ICON_1("icons/icons8/48/Alphabet/1-48.png"),
		ICON_2("icons/icons8/48/Alphabet/2-48.png"),
		ICON_3("icons/icons8/48/Alphabet/3-48.png"),
		ICON_4("icons/icons8/48/Alphabet/4-48.png"),
		ICON_5("icons/icons8/48/Alphabet/5-48.png"), 
		ICON_6("icons/icons8/48/Alphabet/6-48.png"), 
		ICON_7("icons/icons8/48/Alphabet/7-48.png"), 
		ICON_8("icons/icons8/48/Alphabet/8-48.png"), 
		ICON_9("icons/icons8/48/Alphabet/9-48.png"); 
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
