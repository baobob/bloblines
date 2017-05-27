package org.bloblines.ui.map;

import java.util.ArrayList;
import java.util.List;

import org.bloblines.data.map.Area;
import org.bloblines.data.map.Location;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UiArea {

	public Area area;
	public int width;
	public int height;

	public List<UiLocation> uiLocations;

	public UiArea(Area area) {
		this.area = area;
		this.uiLocations = new ArrayList<>();
		this.width = (int) (area.width * UiLocation.TILE_WIDTH);
		this.height = (int) (area.height * UiLocation.TILE_HEIGHT);
		for (Location l : area.locations) {
			uiLocations.add(new UiLocation(l));
		}
	}

	public void render(SpriteBatch batch) {
		for (UiLocation l : uiLocations) {
			l.render(batch);
		}
	}

}
