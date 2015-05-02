package org.bloblines.ui.map;

import org.bloblines.Game;
import org.bloblines.data.map.Location;
import org.bloblines.ui.blob.BlobActor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LocationActor extends BlobActor {

	public Location location;

	public LocationActor(Location l, BlobWorld w, Game g, ShapeRenderer r) {
		super(w, r);
		this.location = l;

		this.setSize(80, 40);
		this.setPosition(location.pos.x, location.pos.y);
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
		renderer.setColor(Color.RED);
		renderer.ellipse(location.pos.x, location.pos.y, 80, 40);
	}
}
