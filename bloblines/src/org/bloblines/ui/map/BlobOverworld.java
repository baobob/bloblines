package org.bloblines.ui.map;

import org.bloblines.data.map.Action;
import org.bloblines.data.map.ActionType;
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

	public Area area;

	public static final int TILE_WIDTH = 16;

	public BlobOverworld(TiledMap map) {
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

			location.actions.add(new Action(ActionType.SPEAK_TO_NPC, "Speak to the Elder"));
			location.actions.add(new Action(ActionType.FIGHT, "Find an evil radish to fight"));
			location.actions.add(new Action(ActionType.SHOP, "Talk to the traveling merchant"));

			area.addLocation(location);
		}

		initLocationLinks(map);
	}

	private void initLocationLinks(TiledMap map) {
		for (MapObject event : map.getLayers().get("Locations").getObjects()) {
			Location location = area.locationsByName.get(event.getName());
			String neighbours = event.getProperties().get("neighbours", String.class);
			if (neighbours == null)
				continue;

			for (String neighbour : neighbours.split(",")) {
				Location targetLocation = area.locationsByName.get(neighbour.trim());
				if (targetLocation == null) {
					System.err.printf("Cannot find the target location %s in location %s", neighbour.trim(), location.name);
				} else {
					location.targets.add(new Target(targetLocation));
				}
			}
		}
	}
}
