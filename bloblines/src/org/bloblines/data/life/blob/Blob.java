package org.bloblines.data.life.blob;

import org.bloblines.data.life.LivingThing;
import org.bloblines.data.world.Area;

public class Blob extends LivingThing {

	public String name;
	public int age = 0;

	/**
	 * Creates a Blob from a dominantParent and a recessiveParent. The newly
	 * born Blob will get most of its characteristics from its dominant parent,
	 * and a few from the recessive one.
	 * 
	 * @param dominantParent Dominant parent of the newly created LivingThing
	 * @param recessiveParent Recessive parent of the newly created LivingThing
	 * @param startingArea Starting area of the Blob
	 */
	public Blob(Blob dominantParent, Blob recessiveParent, Area startingArea) {
		super(startingArea);
		name = "BobTheBlob" + r.nextInt(100);
	}

	@Override
	public void live() {
		if (random(20)) {
			System.out.println(name + "> \"I'm happy ! \"");
		} else if (random(20)) {
			System.out.println(name + "> \"To Blob or not to Blob\"");
		}
		age++;
		if (random(age - 20)) {
			die();
		}
	}

	@Override
	public void die() {
		System.out.println(name + "> \"Arrrgh. \"");
	}

}
