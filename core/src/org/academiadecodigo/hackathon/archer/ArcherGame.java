package org.academiadecodigo.hackathon.archer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;

public class ArcherGame extends Game {

	private SpriteBatch getBatch;
	Texture img;
	
	@Override
	public void create () {
		getBatch = new SpriteBatch();
		setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		getBatch.dispose();
	}

	public SpriteBatch getBatch() {
		return getBatch;
	}
}
