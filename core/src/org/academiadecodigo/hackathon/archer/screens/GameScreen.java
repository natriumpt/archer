package org.academiadecodigo.hackathon.archer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.academiadecodigo.hackathon.archer.ArcherGame;

public class GameScreen implements Screen {

    private ArcherGame archerGame;
    //  Texture texture;

    private OrthographicCamera gamecam;
    private Viewport viewPort;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public GameScreen(ArcherGame archerGame) {

        this.archerGame = archerGame;
        // texture = new Texture("archer.png");

        gamecam = new OrthographicCamera();
        viewPort = new FitViewport(800, 480, gamecam);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map/testmap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        System.out.println(viewPort.getScreenWidth());
        System.out.println(viewPort.getScreenHeight());
        gamecam.position.set(viewPort.getWorldWidth()/2, viewPort.getWorldHeight()/2, 0);

    }

    @Override
    public void show() {

    }

    public void update(float dt) {
        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height);
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
