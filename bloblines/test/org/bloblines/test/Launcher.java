package org.bloblines.test;

import org.bloblines.data.life.blob.Blob;
import org.bloblines.data.life.plant.Cucumber;
import org.bloblines.data.life.plant.Potato;
import org.bloblines.data.world.Area;

public class Launcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create a world with a few area
		Area world = new Area(100, 100);

		// Create some living things
		new Potato(world);
		new Potato(world);
		new Cucumber(world);
		new Blob(null, null, world);
		new Blob(null, null, world);
		new Blob(null, null, world);

		int i = 0;
		while (i++ < 300) {
			world.live();
			if (i % 30 == 0) {
				System.out.println("There are currently "
						+ world.livingThings.size()
						+ " living things in this world");
			}
		}

	}

}
