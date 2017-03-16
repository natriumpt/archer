package org.academiadecodigo.hackathon.archer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;

public class ArcherGame extends Game {

	public SpriteBatch batch;
    public static final float PPM = 32;

//	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new GameScreen((this)));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}
