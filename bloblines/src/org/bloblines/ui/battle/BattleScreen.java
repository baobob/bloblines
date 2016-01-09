package org.bloblines.ui.battle;

import org.bloblines.Game;
import org.bloblines.data.battle.Battle;
import org.bloblines.data.battle.Log;
import org.bloblines.data.battle.Log.Type;
import org.bloblines.ui.BlobScreen;
import org.bloblines.ui.map.MapScreen;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class BattleScreen extends BlobScreen {

	private TextButton btnBack;

	private Battle battle;

	public BattleScreen(Game b, Battle battle) {
		super(b);

		this.battle = battle;

		/** Process a 1 round battle */
		battle.process(1);

		initUI();

		InputMultiplexer inputs = new InputMultiplexer(stage, this);
		Gdx.input.setInputProcessor(inputs);

	}

	private void initUI() {
		Table logTable = new Table();
		logTable.align(Align.left);
		for (Log log : battle.logs) {
			Label messageLabel = new Label(log.message, getDefaultSkin());
			if (log.type == Type.BATTLE_INFO) {
				messageLabel.setColor(Color.ORANGE);
				messageLabel.setAlignment(Align.left);
			}
			logTable.add(messageLabel);
			logTable.row();
		}

		ScrollPane scrollPane = new ScrollPane(logTable, getDefaultSkin());
		Table scrollContainer = new Table();
		scrollContainer.setWidth(800);
		scrollContainer.setHeight(400);
		scrollContainer.setX((Gdx.graphics.getWidth() - scrollContainer.getWidth()) / 2);
		scrollContainer.add(scrollPane).fill().expand();
		stage.addActor(scrollContainer);

		btnBack = new TextButton("Back to Map", getDefaultSkin(), "default");
		btnBack.setBounds(600, 550, 200, 25);
		stage.addActor(btnBack);
		btnBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				backToMap();
			}
		});
		stage.setScrollFocus(scrollPane);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// camera.update();

		game.spriteBatch.begin();
		game.spriteBatch.draw(getTexture(Textures.BATTLE_SCREEN), 200, 50);
		// getDefaultFont().setScale(3);
		getBiggerFont().draw(game.spriteBatch, "Fight !", 50, Gdx.graphics.getHeight() - 50);
		// getDefaultFont().setScale(1);
		getDefaultFont().draw(game.spriteBatch, "You fight for your life. Or fortune. Or glory. Or something else, probably worthless. ",
				80, Gdx.graphics.getHeight() - 120);
		game.spriteBatch.end();

		stage.act(delta);
		stage.draw();
	}

	private void backToMap() {
		game.setScreen(new MapScreen(game));
		dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ENTER || keycode == Keys.ESCAPE) {
			backToMap();
			return true;
		}
		return super.keyDown(keycode);
	}

}
