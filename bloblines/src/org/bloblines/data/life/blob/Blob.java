package org.bloblines.data.life.blob;

import org.bloblines.data.life.LivingThing;
import org.bloblines.data.world.Area;

public class Blob extends LivingThing {

	public String name;
	public int age = 0;
	public int life = 100;
	public int lifeMax = 100;

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
		if (random(5)) {
			say("I'm happy to be " + age);
		} else if (random(5)) {
			say("To Blob or not to Blob...");
		}
		age++;
		if (random(age - 20)) {
			die();
		}
	}

	@Override
	public void die() {
		say("Arrrgh. ");
		say("-- dead at the age of " + age);
		super.die();
	}

}
