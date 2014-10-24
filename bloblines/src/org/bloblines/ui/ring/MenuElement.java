package org.bloblines.ui.ring;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MenuElement extends Image {

	public String label;

	public MenuElement(String label, Texture t) {
		super(t);
		this.label = label;
	}

}
