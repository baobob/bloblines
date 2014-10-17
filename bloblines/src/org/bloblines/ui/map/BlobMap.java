package org.bloblines.ui.map;

import org.bloblines.Game;
import org.bloblines.data.map.Location;
import org.bloblines.data.map.Target;
import org.bloblines.ui.BlobScreen;
import org.bloblines.utils.Assets.Textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class BlobMap extends BlobScreen implements InputProcessor {

	private OrthographicCamera camera;
	private Stage stage;

	private Skin skin = getDefaultSkin();

	public UiPlayer uiPlayer;

	public BlobMap(Game game) {
		super(game);
		uiPlayer = new UiPlayer(game.player);

		stage = new Stage();

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		int wpx = 600;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, (w / h) * wpx, wpx);
		camera.zoom = 0.4f;
		camera.update();

		InputMultiplexer inputs = new InputMultiplexer(stage, this);
		Gdx.input.setInputProcessor(inputs);

		initUserInterface();
	}

	private void initUserInterface() {
		initStartDialog();
		initMenu();
	}

	private void initStartDialog() {
		// *************************** Start dialog *********************
		float w = 400;
		float h = 300;
		Dialog dialog = new Dialog("Bienvenue dans Bloblines", skin, "default");
		String message = "Bienvenue jeune Blob,\n\n"
				+ "Dans cette incroyable quête, vous devrez faire plein de choses épiques et géniales pour réussir à survivre. Essayez de trouver des compagnons et de l'équipement de meilleure qualité que ce que vous possédez actuellement.";
		Label dialogTxt = new Label(message, skin);
		dialogTxt.setWrap(true);
		dialog.getContentTable().add(dialogTxt).prefWidth(w);
		dialog.setModal(true);
		dialog.setMovable(true);
		dialog.setColor(0.9f, 0.9f, 0.9f, 0.8f);

		dialog.setBounds((Gdx.graphics.getWidth() - w) / 2, (Gdx.graphics.getHeight() - h) / 2, w, h);
		dialog.button("C'est parti !");
		stage.addActor(dialog);
	}

	private void initMenu() {
		// Add menu Icon + Dialog
		final Table menuParamsTable = new Table(skin);
		menuParamsTable.add("Parametres");
		menuParamsTable.add("Vous pouvez changer des trucs ici...");
		menuParamsTable.setBounds(30, 30, 200, 200);
		menuParamsTable.setVisible(false);
		stage.addActor(menuParamsTable);

		Image menuParamsIcon = new Image(getTexture(Textures.ICON_PARAMS));
		menuParamsIcon.setBounds(20, Gdx.graphics.getHeight() - (20 + 32), 32, 32);
		menuParamsIcon.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (((InputEvent) event).getType().equals(Type.touchDown)) {
					menuParamsTable.setVisible(!menuParamsTable.isVisible());
					return true;
				}
				return false;
			}
		});
		stage.addActor(menuParamsIcon);

		// Add menu Icon + Dialog
		final Table menuQuestsTable = new Table(skin);
		menuQuestsTable.add("Quêtes");
		menuQuestsTable.add("Liste des quêtes à effectuer");
		menuQuestsTable.setBounds(30, 30, 200, 200);
		menuQuestsTable.setVisible(false);
		stage.addActor(menuQuestsTable);

		Image menuQuestsIcon = new Image(getTexture(Textures.ICON_BOOK));
		menuQuestsIcon.setBounds(20, Gdx.graphics.getHeight() - (60 + 32), 32, 32);
		menuQuestsIcon.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (((InputEvent) event).getType().equals(Type.touchDown)) {
					menuQuestsTable.setVisible(!menuQuestsTable.isVisible());
					return true;
				}
				return false;
			}
		});
		stage.addActor(menuQuestsIcon);

		// Add menu Icon + Dialog
		final Table menuEventsTable = new Table(skin);
		menuEventsTable.add("Evenements");
		menuEventsTable.add("Trucs faisables là ou vous êtes actuellement...");
		menuEventsTable.setBounds(30, 30, 200, 200);
		menuEventsTable.setVisible(false);
		stage.addActor(menuEventsTable);

		Image menuEventsIcon = new Image(getTexture(Textures.ICON_LOCATION));
		menuEventsIcon.setBounds(20, Gdx.graphics.getHeight() - (100 + 32), 32, 32);
		menuEventsIcon.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (((InputEvent) event).getType().equals(Type.touchDown)) {
					menuEventsTable.setVisible(!menuEventsTable.isVisible());
					return true;
				}
				return false;
			}
		});
		stage.addActor(menuEventsIcon);

		// Add menu Icon + Dialog
		final Table menuBlobsTable = new Table(skin);
		menuBlobsTable.add("Blobs");
		menuBlobsTable.add("Blobs de l'équipe");
		menuBlobsTable.setBounds(30, 30, 200, 200);
		menuBlobsTable.setVisible(false);
		stage.addActor(menuBlobsTable);

		Image menuBlobsIcon = new Image(getTexture(Textures.ICON_BLOB));
		menuBlobsIcon.setBounds(20, Gdx.graphics.getHeight() - (140 + 32), 32, 32);
		menuBlobsIcon.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (((InputEvent) event).getType().equals(Type.touchDown)) {
					menuBlobsTable.setVisible(!menuBlobsTable.isVisible());
					return true;
				}
				return false;
			}
		});
		stage.addActor(menuBlobsIcon);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(100f / 255f, 100f / 255f, 250f / 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		updatePlayer(delta);
		updateCamera();

		// Render base Map
		game.world.renderer.setView(camera);
		game.world.renderer.render();

		game.shapeRenderer.setProjectionMatrix(camera.combined);
		game.shapeRenderer.begin(ShapeType.Filled);
		renderEventsLinks();
		game.shapeRenderer.end();

		// Render player / events / moving stuff
		SpriteBatch batch = (SpriteBatch) game.world.renderer.getSpriteBatch();
		batch.begin();
		renderEvents(batch);
		renderPlayer(batch);
		batch.end();

		// Render UI (dialogs / buttons / etc)
		stage.act(delta);
		stage.draw();

	}

	private void updatePlayer(float delta) {
		// update player - Move and stuff
		uiPlayer.update(delta);
	}

	private void updateCamera() {
		// Keep player as centered as possible
		camera.position.x = uiPlayer.getPos().x;
		// if (camera.position.x < Gdx.graphics.getWidth() / 2)
		// camera.position.x = Gdx.graphics.getWidth() / 2;
		camera.position.y = uiPlayer.getPos().y;

		camera.update();
	}

	private void renderEvents(SpriteBatch batch) {
		for (Location location : game.world.area.locations.values()) {
			Texture t = getTexture(Textures.SPRITE_LOCATION);
			if (location.done) {
				t = getTexture(Textures.SPRITE_LOCATION_DONE);
			}
			batch.draw(t, location.pos.x, location.pos.y);
		}
	}

	private void renderEventsLinks() {
		for (Location location : game.world.area.locations.values()) {
			for (Target target : location.targets) {
				game.shapeRenderer.rectLine(location.pos.x + 8, location.pos.y + 8, target.destination.pos.x + 8,
						target.destination.pos.y + 8, 8);
			}
		}
	}

	private void renderPlayer(SpriteBatch batch) {
		TextureRegion frame = uiPlayer.getAnimation();
		batch.draw(frame, uiPlayer.getPos().x, uiPlayer.getPos().y, 16, 16);

	}

	private void startEvent() {
		Dialog dialog = new Dialog("TODO", skin, "default");
		Label dialogTxt = new Label("DESCRIPTION TODO", skin);
		dialogTxt.setWrap(true);
		dialog.getContentTable().add(dialogTxt).prefWidth(300);

		dialog.setMovable(true);
		dialog.setColor(0.9f, 0.9f, 0.9f, 0.7f);

		dialog.setBounds(100, 400, 300, 200);
		dialog.validate();
		stage.addActor(dialog);

		// MoveToAction a = new MoveToAction();
		// a.setPosition(300, 500);
		// a.setDuration(10);
		// dialog.addAction(a);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.ENTER) {
			startEvent();
		}
		if (keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT || keycode == Input.Keys.UP || keycode == Input.Keys.DOWN)
			uiPlayer.updateAnimation();
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT || keycode == Input.Keys.UP || keycode == Input.Keys.DOWN)
			uiPlayer.updateAnimation();
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if (amount < 0) {
			camera.zoom /= 1.1f;
		} else {
			camera.zoom *= 1.1f;
		}
		System.out.println(camera.zoom);
		return true;
	}
}
