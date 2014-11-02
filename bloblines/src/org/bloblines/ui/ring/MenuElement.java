package org.bloblines.ui.ring;

import org.bloblines.Game;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MenuElement extends Image {

	public String label;

	public MenuElement(String label, Textures t) {
		super(Game.assets.getTexture(t));
		this.label = label;
	}

}
