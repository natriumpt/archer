package org.academiadecodigo.hackathon.archer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import org.academiadecodigo.hackathon.archer.sprites.archer.Archer;
import org.academiadecodigo.hackathon.archer.sprites.enemies.Skeleton;
import org.academiadecodigo.hackathon.archer.sprites.projectile.Projectile;
import org.academiadecodigo.hackathon.archer.tools.ArcherInputProcessor;

public class GameScreen implements Screen {

    //  Texture texture;

    private OrthographicCamera gamecam;
    private Viewport viewPort;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private ArcherGame game;
    private World world;
    private Box2DDebugRenderer debugRenderer;

    private Archer archer;
    private ArcherInputProcessor inputProcessor;
    private Skeleton skeleton;

    public static final float VIEWPORT_WIDTH = 10f;
    public static final float VIEWPORT_HEIGHT = 7.5f;
    public static final float PROJECTILE_VELOCITY = 3f;

    public GameScreen(ArcherGame archerGame) {

        Box2D.init();

        this.game = archerGame;
        world = new World(new Vector2(0, 0), true);
        archer = new Archer(this);
        skeleton = new Skeleton(this, 40/ArcherGame.PPM, 40/ ArcherGame.PPM);

        gamecam = new OrthographicCamera();
        viewPort = new FitViewport(archerGame.V_WIDTH / archerGame.PPM, archerGame.V_HEIGHT / archerGame.PPM, gamecam);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map/forest.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1 / archerGame.PPM);
        gamecam.position.set(viewPort.getWorldWidth() / 2, viewPort.getWorldHeight() / 2, 0);
        debugRenderer = new Box2DDebugRenderer();

        inputProcessor = new ArcherInputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);


        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //Create tree bodies/fixtures
        for (MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / ArcherGame.PPM, (rect.getY() + rect.getHeight() / 2) / ArcherGame.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / ArcherGame.PPM, rect.getHeight() / 2 / ArcherGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
            body.setActive(true);
        }
    }


    @Override
    public void show() {

    }

    public void update(float dt) {

        handleInput();

        world.step(1 / 60f, 6, 2);

        gamecam.position.x = archer.body.getPosition().x;
        gamecam.position.y = archer.body.getPosition().y;
        gamecam.update();
        renderer.setView(gamecam);

        for (Projectile p: archer.projectiles) {

            CircleShape projectileShape = (CircleShape) p.body.getFixtureList().get(0).getShape();
            CircleShape circleShape = (CircleShape) skeleton.enemyBody.getFixtureList().get(0).getShape();

            float xD = p.body.getPosition().x - skeleton.enemyBody.getPosition().x;      // delta x
            float yD = p.body.getPosition().y - skeleton.enemyBody.getPosition().y;      // delta y
            float sqDist = xD * xD + yD * yD;  // square distance
            boolean collision = sqDist <= (projectileShape.getRadius()+circleShape.getRadius()) * (projectileShape.getRadius()+circleShape.getRadius());

            if (collision){
                world.destroyBody(p.body);
                world.destroyBody(skeleton.enemyBody);
            }
        }


    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        world.step(1 / 60f, 6, 2);

        // This is so that the world bodies outline are showned
        // (just for testing purposes)
        debugRenderer.render(world, gamecam.combined);

        // only draw what the projection sees
        game.getBatch().setProjectionMatrix(gamecam.combined);

        // begin a new batch and draw the bucket and
        game.getBatch().begin();
        game.getBatch().end();


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
            archer.velocityVector.x = -1f;
        } else if (inputProcessor.dKey && !inputProcessor.aKey) {
            archer.velocityVector.x = 1f;
        } else {
            archer.velocityVector.x = 0;
        }

        if (inputProcessor.wKey) {
            archer.velocityVector.y = 1f;
        } else if (inputProcessor.sKey) {
            archer.velocityVector.y = -1f;
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
