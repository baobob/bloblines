package org.bloblines.ui.map;

import java.util.HashMap;
import java.util.Map;

import org.bloblines.data.game.Player;
import org.bloblines.data.map.Location;
import org.bloblines.utils.XY;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class UiPlayer {

	private static final int MOVE_UP = 1;
	private static final int MOVE_DOWN = 2;
	private static final int MOVE_LEFT = 3;
	private static final int MOVE_RIGHT = 4;

	private Player player;

	private float stateTime = 0;

	Map<Integer, Animation> anims = new HashMap<Integer, Animation>();

	private int moving = 0x0000;
	private int currentAnimation = MOVE_DOWN;
	private XY currentUiPos;

	public static int HEIGHT = 64;
	public static int WIDTH = 64;

	public UiPlayer(Player p) {
		player = p;
		currentUiPos = UiLocation.getUiXY(player.location);
		Texture slimeTexture = new Texture(Gdx.files.internal("characters/slime.png"));

		TextureRegion[][] slimeRegion = new TextureRegion(slimeTexture).split(32, 32);
		TextureRegion[][] slimeRegionMirror = new TextureRegion(slimeTexture).split(32, 32);

		for (TextureRegion[] rx : slimeRegionMirror)
			for (TextureRegion ry : rx)
				ry.flip(true, false);

		anims.put(MOVE_UP, getColumnAnimation(slimeRegion, 0, 6));
		anims.put(MOVE_DOWN, getColumnAnimation(slimeRegion, 2, 6));
		anims.put(MOVE_RIGHT, getColumnAnimation(slimeRegion, 1, 6));
		anims.put(MOVE_LEFT, getColumnAnimation(slimeRegionMirror, 1, 6));

	}

	public void update(float delta) {
		stateTime += delta;
		XY dest = UiLocation.getUiXY(player.location);
		if (dest.equals(currentUiPos)) {
			currentAnimation = MOVE_DOWN;
			return;
		}

		float xDest = dest.x;
		float yDest = dest.y;
		float xCurr = currentUiPos.x;
		float yCurr = currentUiPos.y;

		float dx = xDest - xCurr;
		float dy = yDest - yCurr;

		if (Math.abs(dx) > Math.abs(dy)) {
			if (dx > 0) {
				currentAnimation = MOVE_RIGHT;
			} else {
				currentAnimation = MOVE_LEFT;
			}
		} else {
			if (dy > 0) {
				currentAnimation = MOVE_UP;
			} else {
				currentAnimation = MOVE_DOWN;
			}
		}

		double dz = Math.sqrt(dx * dx + dy * dy);
		if (dz < 3) {
			currentUiPos = new XY(player.pos);
			return;
		}
		double ax = 3 * dx / dz;
		double ay = 3 * dy / dz;

		currentUiPos.x += ax;
		currentUiPos.y += ay;
	}

	public boolean isAtLocation(Location l) {
		if (player.location.equals(l)) {
			return true;
		}
		return false;
	}

	public TextureRegion getAnimation() {
		return anims.get(currentAnimation).getKeyFrame(stateTime, true);
	}

	private Animation getColumnAnimation(TextureRegion[][] r, int col, int frames) {
		Array<TextureRegion> keyFrames = new Array<TextureRegion>();
		for (int i = 0; i < frames; i++) {
			keyFrames.add(r[i][col]);
		}
		return new Animation(0.1f, keyFrames);
	}

	public XY getCenter() {
		return new XY(currentUiPos.x - WIDTH / 2, currentUiPos.y - HEIGHT / 2);
	}

	public void render(SpriteBatch batch) {
		TextureRegion frame = getAnimation();
		batch.draw(frame, currentUiPos.x - WIDTH / 2, currentUiPos.y - HEIGHT / 4, UiPlayer.WIDTH, UiPlayer.HEIGHT);
	}
}
