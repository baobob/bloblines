package org.bloblines.data.life.blob;

import org.bloblines.data.life.LivingThing;
import org.bloblines.data.world.Area;

public class Blob extends LivingThing {

	/**
	 * Creates a Blob from a dominantParent and a recessiveParent. The newly
	 * born Blob will get most of its characteristics from its dominant parent,
	 * and a few from the recessive one.
	 * 
	 * @param dominantParent
	 *            Dominant parent of the newly created LivingThing
	 * @param recessiveParent
	 *            Recessive parent of the newly created LivingThing
	 * @param startingArea
	 *            Starting area of the Blob
	 */
	public Blob(Blob dominantParent, Blob recessiveParent, Area startingArea) {
		super(startingArea);
	}

	@Override
	public void live() {
		
	}

	@Override
	public void die() {
			
	}

}
