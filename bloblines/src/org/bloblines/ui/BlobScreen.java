package org.bloblines.ui;

import org.bloblines.Game;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class BlobScreen implements Screen {

	protected Game game;

	public BlobScreen(Game game) {
		this.game = game;
	}

	protected Texture getTexture(Textures t) {
		return game.assets.getTexture(t);
	}

	protected BitmapFont getDefaultFont() {
		return game.assets.getFont();
	}

	protected Skin getDefaultSkin() {
		return game.assets.getSkin();
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}
