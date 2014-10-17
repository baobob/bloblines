package org.bloblines.ui.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bloblines.data.map.Area;
import org.bloblines.data.map.Location;
import org.bloblines.data.map.Target;
import org.bloblines.utils.XY;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;

public class BlobOverworld {

	public OrthogonalTiledMapRenderer renderer;

	public BlobEvent start;
	public Area area;
	public List<BlobEvent> events = new ArrayList<BlobEvent>();

	public BlobOverworld(TiledMap map) {
		BlobEvent locationEvent;
		Map<String, Location> locations = new HashMap<String, Location>();
		renderer = new OrthogonalTiledMapRenderer(map);
		area = new Area();

		for (MapObject event : map.getLayers().get("Locations").getObjects()) {
			Ellipse e = ((EllipseMapObject) event).getEllipse();
			Location location = new Location();

			// TODO faire ça mieux.
			// On fait -8 sur les 2 coordonnées pour corriger le décalage entre
			// le centre de l'ellipse et le coin inférieur gauche qui correspond
			// aux coordonnées de la ou l'on veut dessiner les sprites.
			XY pos = new XY(e.x - 8, e.y - 8);

			location.name = event.getName();
			location.description = event.getProperties().get("description", String.class);
			location.pos = pos;

			locationEvent = new BlobEvent(pos, location.name, true);
			locationEvent.text = location.description;
			events.add(locationEvent);

			locations.put(location.name, location);
			area.locations.add(location);

			if ("Start".equals(location.name)) {
				start = locationEvent;
			}
		}

		for (MapObject event : map.getLayers().get("Locations").getObjects()) {
			Location location = locations.get(event.getName());
			String neighbours = event.getProperties().get("neighbours", String.class);
			if (neighbours == null)
				continue;

			for (String neighbour : neighbours.split(",")) {
				Location targetLocation = locations.get(neighbour.trim());
				if (targetLocation == null) {
					System.err.printf("Cannot find the target location %s in location %s", neighbour.trim(), location.name);
				} else {
					location.targets.add(new Target(targetLocation));
				}
			}
		}
	}
}
