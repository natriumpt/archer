package org.academiadecodigo.hackathon.archer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;

public class ArcherGame extends Game {
	public SpriteBatch batch;
//	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new GameScreen((this)));
	}

	@Override
	public void render () {

		super.render();
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
