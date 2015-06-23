package org.bloblines.ui.map;

import java.util.HashMap;
import java.util.Map;

import org.bloblines.data.game.Player;
import org.bloblines.utils.XY;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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

	public static int HEIGHT = 64;
	public static int WIDTH = 64;

	public UiPlayer(Player p) {
		player = p;
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
		if ((moving & 0xF000) > 0)
			player.pos.y += 2;
		if ((moving & 0x0F00) > 0)
			player.pos.x += 2;
		if ((moving & 0x00F0) > 0)
			player.pos.y -= 2;
		if ((moving & 0x000F) > 0)
			player.pos.x -= 2;
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

	public void updateAnimation() {
		int up = Gdx.input.isKeyPressed(Keys.UP) ? 0xF000 : 0x0000;
		int right = Gdx.input.isKeyPressed(Keys.RIGHT) ? 0x0F00 : 0x0000;
		int down = Gdx.input.isKeyPressed(Keys.DOWN) ? 0x00F0 : 0x0000;
		int left = Gdx.input.isKeyPressed(Keys.LEFT) ? 0x000F : 0x0000;

		moving = (up | left | down | right);

		if (moving > 0) {
			if (currentAnimation == MOVE_UP && (moving & up) > 0)
				return;
			if (currentAnimation == MOVE_RIGHT && (moving & right) > 0)
				return;
			if (currentAnimation == MOVE_DOWN && (moving & down) > 0)
				return;
			if (currentAnimation == MOVE_LEFT && (moving & left) > 0)
				return;
		}
		if (up > 0)
			currentAnimation = MOVE_UP;
		else if (right > 0)
			currentAnimation = MOVE_RIGHT;
		else if (down > 0)
			currentAnimation = MOVE_DOWN;
		else if (left > 0)
			currentAnimation = MOVE_LEFT;
		// moving = (up || left || down || right);
	}

	public XY getPos() {
		return player.pos;
	}

	public XY getCenter() {
		return new XY(player.pos.x - WIDTH / 2, player.pos.y - HEIGHT / 2);
	}

	public void render(SpriteBatch batch) {
		TextureRegion frame = getAnimation();
		batch.draw(frame, player.pos.x - WIDTH / 2, player.pos.y - HEIGHT / 4, UiPlayer.WIDTH, UiPlayer.HEIGHT);
	}
}
