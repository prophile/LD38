package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Snowglobe extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private GameMode gameMode = new LoaderGameMode();

	public void setGameMode(GameMode newGameMode) {
		if (newGameMode == gameMode) {
			return;
		}
		GameMode oldGameMode = gameMode;
		newGameMode.preActivate();
		oldGameMode.preDeactivate();
		gameMode = newGameMode;
		gameMode.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		newGameMode.postActivate();
		oldGameMode.postDeactivate();
	}

	@Override
	public void create () {
		Overlord.init();

		// set up initial game mode
		gameMode.preActivate();
        gameMode.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render () {
		double dt = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0.0f, 0, 0.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		GameMode newGameMode = gameMode.tick(dt);
		if (newGameMode != gameMode) {
			setGameMode(newGameMode);
		}
        Viewport viewport = gameMode.getViewport();
        viewport.getCamera().update();
        viewport.apply();
		gameMode.render();
	}

	@Override
	public void resize(int width, int height) {
		gameMode.getViewport().update(width, height, true);
	}
}
