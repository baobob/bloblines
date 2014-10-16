package org.bloblines.ui.map;

import java.util.ArrayList;
import java.util.List;

import org.bloblines.utils.XY;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;

public class BlobOverworld {

	public OrthogonalTiledMapRenderer renderer;

	public BlobEvent start;

	public List<BlobEvent> events = new ArrayList<BlobEvent>();

	public BlobOverworld(TiledMap map) {
		renderer = new OrthogonalTiledMapRenderer(map);
		for (MapObject event : map.getLayers().get("Events").getObjects()) {
			Ellipse e = ((EllipseMapObject) event).getEllipse();
			// TODO faire �a mieux.
			// On fait -8 sur les 2 coordonn�es pour corriger le d�calage entre
			// le centre de l'ellipse et le coin inf�rieur gauche qui correspond
			// aux coordonn�es de la ou l'on veut dessiner les sprites.
			XY pos = new XY(e.x - 8, e.y - 8);

			if ("Start".equals(event.getName())) {
				start = new BlobEvent(pos, event.getName(), true);
				start.text = "Bienvue jeune Blob, que la force soit avec toi !";
				events.add(start);
			} else {
				events.add(new BlobEvent(pos, event.getName(), true));
			}
		}

	}
}
