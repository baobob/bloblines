package org.bloblines.mapgen;

import java.io.IOException;

import org.bloblines.data.map.Area;

public class MapGeneratorTest {

	private static final long SEED = 42;

	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final int EVENTS = 400;

	public static void main(String[] args) throws IOException {
		MapGenerator generator = new MapGenerator(SEED, WIDTH, HEIGHT, EVENTS);
		Area a = generator.generate();
		generator.show(a);
	}
}
