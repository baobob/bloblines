package org.bloblines.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets extends AssetManager {

	private BitmapFont font = null;

	public static final String TEXTURE_SPLASH_SCREEN = "img/hourglass.png";

	public Assets() {
		// Use LibGDX's default Arial font.
		font = new BitmapFont();

		// load("data/mytexture.png", Texture.class);
		load(TEXTURE_SPLASH_SCREEN, Texture.class);

		load("skins/ui.json", Skin.class);
		// skin = new Skin(Gdx.files.internal("skins/ui.json"));

	}

	public BitmapFont getFont() {
		return font;
	}

	public Skin getSkin() {
		return get("skins/ui.json", Skin.class);
	}

	public Texture getTexture(String t) {
		return get(t, Texture.class);
	}

	@Override
	public synchronized void dispose() {
		font.dispose();
		super.dispose();
	}

}
