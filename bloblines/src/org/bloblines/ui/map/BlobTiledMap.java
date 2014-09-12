package org.bloblines.ui.map;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class BlobTiledMap extends TiledMap {

	private Random r = new Random();
	private Texture tiles;

	public BlobTiledMap() {
		tiles = new Texture(Gdx.files.internal("world/land.png"));
		getLayers().add(getBackground());

	}

	private TiledMapTileLayer getBackground() {
		TextureRegion[][] splitTiles = TextureRegion.split(tiles, 128, 128);
		TiledMapTileLayer layer = new TiledMapTileLayer(64, 64, 128, 128);
		for (int x = 0; x < 64; x++) {
			for (int y = 0; y < 64; y++) {
				int tx = 0;
				int ty = 0;
				// M tiles : 7 9 / 4 13 / 3 14
				int i = r.nextInt(3);
				switch (i) {
				case 0:
					tx = 7;
					ty = 9;
					break;
				case 1:
					tx = 4;
					ty = 13;
					break;
				case 2:
					tx = 3;
					ty = 14;
					break;
				}
				Cell cell = new Cell();
				cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
				layer.setCell(x, y, cell);
			}
		}
		return layer;
	}

}
