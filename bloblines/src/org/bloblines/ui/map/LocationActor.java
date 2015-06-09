package org.bloblines.ui.map;

import org.bloblines.Game;
import org.bloblines.data.map.Location;
import org.bloblines.ui.blob.BlobActor;
import org.bloblines.utils.XY;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LocationActor extends BlobActor {

	public Location location;

	public LocationActor(Location l, BlobWorld w, Game g, ShapeRenderer r) {
		super(w, r);
		this.location = l;

		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Clicked " + location.name);
				super.clicked(event, x, y);
			}
		});
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Vector2 screenCoords = getStage().toScreenCoordinates(new Vector2(location.pos.x, location.pos.y), batch.getTransformMatrix());
		XY pos = new XY(screenCoords.x, screenCoords.y);

		// define hit box
		this.setSize(80, 40);
		this.setPosition(pos.x, pos.y);

		// System.out.println("location : " + location.pos.x + "/" + location.pos.y);
		// System.out.println("transformed : " + pos.x + "/" + pos.y);
		renderer.setColor(Color.BLACK);
		renderer.ellipse(pos.x - 4, pos.y - 2, 88, 44);
		renderer.setColor(Color.YELLOW);
		renderer.ellipse(pos.x - 2, pos.y - 1, 84, 42);
		renderer.setColor(Color.BLACK);
		renderer.ellipse(pos.x + 4, pos.y + 2, 72, 36);
		renderer.setColor(Color.RED);
		renderer.ellipse(pos.x + 6, pos.y + 3, 68, 34);
	}
}
