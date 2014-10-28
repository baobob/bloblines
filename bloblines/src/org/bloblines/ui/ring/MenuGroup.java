package org.bloblines.ui.ring;

import org.bloblines.Game;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class MenuGroup extends Group {

	private static final int ELEMENTS_ANGLE = 35;

	private static final int ELEMENTS_DISTANCE = 80;

	private static final float ROTATION_DURATION = 0.08f;

	private static final int ICON_SIZE = 32;

	private Game game;

	/** Menu Label. This not a a menu children cause it shouldn't rotate */
	private Label lbl;

	/**
	 * Vector to position the next menu elements we'll add. We rotate this vector each time we add a new menuElement
	 */
	private Vector3 nextElementVector = new Vector3(0, ELEMENTS_DISTANCE, 0);

	public int rotationIndex = 0;

	public MenuGroup(Game game) {
		this.game = game;
	}

	public void addElement(String label, Textures t) {
		MenuElement menuElement = new MenuElement(label, game.assets.getTexture(t));
		// Rotation moves object center because we rotate it aroud the bottom left corner, we need to translate it to a bit
		float tetaDegrees = getChildren().size * -ELEMENTS_ANGLE;
		double teta = Math.toRadians(-tetaDegrees);
		menuElement.setRotation(tetaDegrees);
		float cx = ICON_SIZE / 2;
		float cy = ICON_SIZE / 2;
		float cx2 = (float) (cx * Math.cos(teta) + cy * Math.sin(teta));
		float cy2 = (float) (-cx * Math.sin(teta) + cy * Math.cos(teta));
		float tx = cx - cx2;
		float ty = cy - cy2;

		menuElement.setWidth(ICON_SIZE);
		menuElement.setHeight(ICON_SIZE);
		menuElement.setCenterPosition(getOriginX() + nextElementVector.x + tx, getOriginY() + nextElementVector.y + ty);
		addActor(menuElement);
		nextElementVector.rotate(-ELEMENTS_ANGLE, 0, 0, 1);
	}

	private void updateRotation() {
		RotateToAction rotation = new RotateToAction();
		rotation.setDuration(ROTATION_DURATION);
		rotation.setRotation(rotationIndex * ELEMENTS_ANGLE);
		addAction(rotation);
		lbl.setText(((MenuElement) getChildren().get(rotationIndex)).label);
	}

	/**
	 * Rotates menu to the left
	 */
	public void left() {
		if (rotationIndex == 0) {
			// We can't rotate left of the first element
			return;
		}
		rotationIndex--;
		updateRotation();
	}

	/**
	 * Rotates menu to the right
	 */
	public void right() {
		if (rotationIndex == getChildren().size - 1) {
			// We can't rotate right of the last element
			return;
		}
		rotationIndex++;
		updateRotation();
	}

	@Override
	public void setVisible(boolean visible) {
		for (Actor child : getChildren()) {
			child.setVisible(visible);
		}
		lbl.setVisible(visible);
		super.setVisible(visible);
	}

	public void keyDown(int keycode) {
		if (keycode == Keys.RIGHT) {
			right();
		}
		if (keycode == Keys.LEFT) {
			left();
		}
	}

	public void render() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		game.bgShapeRenderer.begin(ShapeType.Filled);
		game.bgShapeRenderer.setColor(0.8f, 0.8f, 0.8f, 0.5f);
		game.bgShapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		game.bgShapeRenderer.end();

		// Draw a black square to show selected menu
		game.fgShapeRenderer.begin(ShapeType.Line);
		game.fgShapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1);
		game.fgShapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 20, Gdx.graphics.getHeight() / 2 + 52, 40, 40);
		game.fgShapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 21, Gdx.graphics.getHeight() / 2 + 51, 42, 42);

		// draw a line with menu title to show selected menu
		int lineX = Gdx.graphics.getWidth() / 2 - 220;
		int lineY = Gdx.graphics.getHeight() / 2 + 140;
		game.fgShapeRenderer.line(lineX, lineY, lineX + 140, lineY);
		game.fgShapeRenderer.line(lineX + 140, lineY, lineX + 225, lineY - 48);
		game.fgShapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	public Label getLabel() {
		int lineX = Gdx.graphics.getWidth() / 2 - 220;
		int lineY = Gdx.graphics.getHeight() / 2 + 140;

		lbl = new Label(((MenuElement) getChildren().get(0)).label, game.assets.getSkin());
		lbl.setBounds(lineX + 10, lineY - 10, 300, 50);
		lbl.setVisible(false);
		return lbl;
	}
}
