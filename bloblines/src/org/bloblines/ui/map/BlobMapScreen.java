package org.bloblines.ui.map;

import org.bloblines.Bloblines;
import org.bloblines.ui.BlobScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

// Unused class ?
public class BlobMapScreen extends BlobScreen {

	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;

	public BlobPlayer player;

	public BlobMapScreen(Bloblines b) {
		super(b);
		renderer = new OrthogonalTiledMapRenderer(new BlobTiledMap());
		player = new BlobPlayer(b);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		int wpx = 600;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, (w / h) * wpx, wpx);
		camera.update();
		Gdx.input.setInputProcessor(new BlobMapInput(this));

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(100f / 255f, 100f / 255f, 250f / 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		updatePlayer(delta);
		updateCamera();

		// set the tile map rendere view based on what the
		// camera sees and render the map
		renderer.setView(camera);
		renderer.render();

		SpriteBatch batch = (SpriteBatch) renderer.getSpriteBatch();
		batch.begin();
		renderPlayer(batch);
		batch.end();
	}

	private void updatePlayer(float delta) {
		// update player - Move and stuff
		player.update(delta);
	}

	private void updateCamera() {
		// Keep player as centered as possible
		camera.position.x = player.pos.x;
		// if (camera.position.x < Gdx.graphics.getWidth() / 2)
		// camera.position.x = Gdx.graphics.getWidth() / 2;
		camera.position.y = player.pos.y;

		camera.update();
	}

	private void renderPlayer(SpriteBatch batch) {
		TextureRegion frame = player.getAnimation();
		batch.draw(frame, player.pos.x, player.pos.y); // , 40, 40);
	}

}
