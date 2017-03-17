package org.academiadecodigo.hackathon.archer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.academiadecodigo.hackathon.archer.ArcherGame;
import org.academiadecodigo.hackathon.archer.BodyWorldCreator;
import org.academiadecodigo.hackathon.archer.scenes.Hud;
import org.academiadecodigo.hackathon.archer.sprites.archer.Archer;
import org.academiadecodigo.hackathon.archer.sprites.enemies.Skeleton;
import org.academiadecodigo.hackathon.archer.sprites.projectile.Projectile;
import org.academiadecodigo.hackathon.archer.tools.ArcherInputProcessor;

import java.util.ArrayList;

import static sun.audio.AudioPlayer.player;

public class GameScreen implements Screen {

    private BodyWorldCreator creator;
    private TextureAtlas atlas;

    private OrthographicCamera gamecam;

    private Viewport viewPort;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private ArcherGame game;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Hud hud;

    private Archer archer;
    private ArcherInputProcessor inputProcessor;

    public static final float VIEWPORT_WIDTH = 10f;
    public static final float VIEWPORT_HEIGHT = 7.5f;
    public static final float PROJECTILE_VELOCITY = 6f;


    public ArrayList<Skeleton> skeletons = new ArrayList<Skeleton>();

    public GameScreen(ArcherGame archerGame) {

        Box2D.init();

        atlas = new TextureAtlas("archerset.atlas");

        this.game = archerGame;
        world = new World(new Vector2(0, 0), true);
        archer = new Archer(this);

//        skeleton = new Skeleton(this, 40 / ArcherGame.PPM, 40 / ArcherGame.PPM);
        hud = new Hud(game.batch);

        gamecam = new OrthographicCamera();
        viewPort = new FitViewport(archerGame.V_WIDTH / archerGame.PPM, archerGame.V_HEIGHT / archerGame.PPM, gamecam);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map/level1.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1 / archerGame.PPM);
        gamecam.position.set(viewPort.getWorldWidth() / 2, viewPort.getWorldHeight() / 2, 0);
        debugRenderer = new Box2DDebugRenderer();

        creator = new BodyWorldCreator(this);

        inputProcessor = new ArcherInputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void show() {
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void update(float dt) {

        handleInput();

        world.step(1 / 60f, 6, 2);

        archer.update(dt);
        setActiveEnemies();

        hud.update(dt);

        gamecam.position.x = archer.body.getPosition().x;
        gamecam.position.y = archer.body.getPosition().y;
        gamecam.update();
        renderer.setView(gamecam);

        checkCollisions(dt);

    }

    private void checkCollisions(float dt) {

        for (Skeleton skeleton : skeletons) {

            if (!skeleton.isDead()) {

                skeleton.update(dt);

                for (Projectile projectile : archer.projectiles) {


                    CircleShape projectileShape = (CircleShape) projectile.body.getFixtureList().get(0).getShape();
                    CircleShape skeletonShape = (CircleShape) skeleton.enemyBody.getFixtureList().get(0).getShape();

                    float xD = projectile.body.getPosition().x - skeleton.enemyBody.getPosition().x;      // delta x
                    float yD = projectile.body.getPosition().y - skeleton.enemyBody.getPosition().y;      // delta y
                    float sqDist = xD * xD + yD * yD;  // square distance
                    boolean collision = sqDist <= (projectileShape.getRadius() + skeletonShape.getRadius())
                            * (projectileShape.getRadius() + skeletonShape.getRadius());

                    if (collision) {
                        archer.projectiles.removeValue(projectile, true);
                        skeleton.setDead(true);
                        projectile.body.setTransform(1000000f,1000000f, projectile.body.getAngle());
                        skeleton.enemyBody.setTransform(1000000f,1000000f, projectile.body.getAngle());
                        Hud.addScore(skeleton.getPoints());
                        break;
                    }
                }
            }

        }
    }

    private void setActiveEnemies() {

        for (Skeleton skeleton : skeletons) {
            float enemyPosX = skeleton.getEnemyBody().getPosition().x;
            float enemyPosY = skeleton.getEnemyBody().getPosition().y;
            float archerPosX = archer.body.getPosition().x;
            float archerPosY = archer.body.getPosition().y;
            double distanceDiff = ((Math.pow(archerPosX - enemyPosX, 2) + Math.pow(archerPosY - enemyPosY, 2)));

            if (distanceDiff < 35) {
                skeleton.getEnemyBody().setActive(true);
            }
        }
    }

    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();


        // This is so that the world bodies outline are showned
        // (just for testing purposes)
        debugRenderer.render(world, gamecam.combined);

        // only draw what the projection sees
        game.getBatch().setProjectionMatrix(gamecam.combined);

        // begin a new batch and draw the bucket and

        game.batch.begin();
        archer.draw(game.batch);
        game.batch.end();

        game.getBatch().setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    public TiledMap getMap() {
        return map;
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

    public void handleInput() {


        // TODO: Provide the archer class with a speed value;
        // TODO: Update/merge with the firing method;

        // Accounts for the archer holding opposite keys.
        // Movement code starts here
        if (inputProcessor.aKey && !inputProcessor.dKey) {
            archer.velocityVector.x = -archer.speed;
        } else if (inputProcessor.dKey && !inputProcessor.aKey) {
            archer.velocityVector.x = archer.speed;
        } else {
            archer.velocityVector.x = 0;
        }

        if (inputProcessor.wKey) {
            archer.velocityVector.y = archer.speed;
        } else if (inputProcessor.sKey) {
            archer.velocityVector.y = -archer.speed;
        } else {
            archer.velocityVector.y = 0;
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            archer.fire(new Vector2(PROJECTILE_VELOCITY, 0), true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            archer.fire(new Vector2(-PROJECTILE_VELOCITY, 0), false);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            archer.fire(new Vector2(0, PROJECTILE_VELOCITY), true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            archer.fire(new Vector2(0, -PROJECTILE_VELOCITY), true);
        }

        archer.body.setLinearVelocity(archer.velocityVector);
        // Movement code ends here.
    }

    public Archer getArcher() {
        return archer;
    }

    public World getWorld() {
        return world;
    }


}