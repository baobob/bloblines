package org.bloblines.ui.blob;

import org.bloblines.ui.map.BlobWorld;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Abstract class for all Bloblines elements that extend Actor
 *
 */
public abstract class BlobActor extends Actor {

	public BlobWorld world;
	public ShapeRenderer renderer;

	public BlobActor(BlobWorld w, ShapeRenderer r) {
		this.world = w;
		this.renderer = r;
	}
}
