package org.academiadecodigo.hackathon.archer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import org.academiadecodigo.hackathon.archer.ArcherGame;
import org.academiadecodigo.hackathon.archer.tools.ArcherInputProcessor;

public class StartScreen implements Screen {

    private ArcherGame game;
    private OrthographicCamera camera;
    private Texture backgroundPicture;
    private Rectangle background;
    private ArcherInputProcessor inputProcessor;

    public StartScreen(ArcherGame archerGame) {
        this.game = archerGame;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        backgroundPicture = new Texture(Gdx.files.internal("titlescreen.png"));
        background = new Rectangle();
        background.x = 0;
        background.y = 0;
        background.width = 800;
        background.height = 480;

        inputProcessor = new ArcherInputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backgroundPicture, background.x, background.y, background.width, background.height);
        game.batch.end();

        handleInput();
    }

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
