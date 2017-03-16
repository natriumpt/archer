package org.academiadecodigo.hackathon.archer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import org.academiadecodigo.hackathon.archer.ArcherGame;
import org.academiadecodigo.hackathon.archer.sprites.Archer;

public class GameScreen implements Screen {

    private World world;
    private Archer player;
    private ArcherGame game;
    private Box2DDebugRenderer debugRenderer;


    private OrthographicCamera camera;
    public static final float VIEWPORT_WIDTH = 10;
    public static final float VIEWPORT_HEIGHT = 7.5f;

    public GameScreen(ArcherGame archerGame) {

        // Start the physics engine
        Box2D.init();

        this.game = archerGame;
        world = new World(new Vector2(0, 0), true);
        player = new Archer(this);

        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();

        debugRenderer = new Box2DDebugRenderer();

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        world.step(1/60f, 6, 2);

        // This clears the screen
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // This is so that the world bodies outline are showned
        // (just for testing purposes)
        debugRenderer.render(world, camera.combined);

        // tell the camera to update its matrices.
        camera.update();

        game.getBatch().setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        game.getBatch().begin();
        game.getBatch().end();


        handleInput();

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

    public void handleInput() {
        //control our player using immediate impulses
//        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
//            player.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x <= 2) {
            player.body.applyLinearImpulse(new Vector2(0.1f, 0), player.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x >= -2) {
            player.body.applyLinearImpulse(new Vector2(-0.1f, 0), player.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.body.getLinearVelocity().y >= -2) {
            player.body.applyLinearImpulse(new Vector2(0, -0.1f), player.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.body.getLinearVelocity().y <= 2) {
            player.body.applyLinearImpulse(new Vector2(0, 0.1f), player.body.getWorldCenter(), true);
        }

    }

    public World getWorld() {
        return world;
    }
}
