package org.academiadecodigo.hackathon.archer.sprites.archer;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.academiadecodigo.hackathon.archer.ArcherGame;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;
import org.academiadecodigo.hackathon.archer.sprites.projectile.Projectile;

public class Archer extends Sprite {

    public static final int NUMBER_PROJECTILES = 5;
    public enum State {STANDING, WALKING, FIRING, DEAD}
    public enum Orientation {NORTH, SOUTH, EAST, WEST}

    public State currentState;
    public State previousState;
    public Orientation currentOrientation;
    public Orientation previousOrientation;
    private float stateTimer;

    public World world;
    public Body body;
    public Vector2 velocityVector;
    private GameScreen gameScreen;

    private TextureRegion archerStandingNorth;
    private TextureRegion archerStandingEast;
    private TextureRegion archerStandingSouth;
    private Animation walkingNorth;
    private Animation walkingEast;
    private Animation walkingSouth;

    public Array<Projectile> projectiles;
    public float speed;

    public Archer(GameScreen gameScreen) {

        super(gameScreen.getAtlas().findRegion("standing_n"));

        this.world = gameScreen.getWorld();
        this.gameScreen = gameScreen;

        setTextureRegions(gameScreen);
        setAnimations(gameScreen);

        setBounds(0, 0, 48 / ArcherGame.PPM, 48 / ArcherGame.PPM);
        setRegion(archerStandingNorth);

        defineArcher();

        init();


    }

    private void init() {
        projectiles = new Array<Projectile>();
        previousState = State.STANDING;
        currentState = State.STANDING;
        currentOrientation = Orientation.NORTH;
        stateTimer = 0;
        speed = 3f;

    }

    private void setAnimations(GameScreen gameScreen) {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(gameScreen.getAtlas().findRegion("walking_e", i)));
        }
        walkingEast = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 1; i < 5; i++) {
            frames.add(new TextureRegion(gameScreen.getAtlas().findRegion("walking_n", i)));
        }
        walkingNorth = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 1; i < 5; i++) {
            frames.add(new TextureRegion(gameScreen.getAtlas().findRegion("walking_s", i)));
        }
        walkingSouth = new Animation(0.1f, frames);
        frames.clear();
    }

    private void setTextureRegions(GameScreen gameScreen) {
        archerStandingNorth = new TextureRegion(gameScreen.getAtlas().findRegion("standing_n"));
        archerStandingSouth = new TextureRegion(gameScreen.getAtlas().findRegion("standing_s"));
        archerStandingEast = new TextureRegion(gameScreen.getAtlas().findRegion("standing_e"));
    }

    public void update(float dt) {

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        checkCollisionWithObstacles();
    }

    private void checkCollisionWithObstacles() {
        for (Projectile projectile: projectiles) {
            if ((projectile.body.getLinearVelocity().x < 2 && projectile.body.getLinearVelocity().y < 2)
                    && (projectile.body.getLinearVelocity().x > -2 && projectile.body.getLinearVelocity().y > -2)){
                ArcherGame.manager.get("audio/sounds/arrow-hit.wav", Sound.class).play();
                projectile.body.setTransform(1000000f,1000000f, projectile.body.getAngle());
                projectiles.removeValue(projectile, true);
            }
        }
    }

    private void defineArcher() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(300 / ArcherGame.PPM, 300 / ArcherGame.PPM);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(5 / ArcherGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        velocityVector = new Vector2(0, 0);

    }

    public TextureRegion getFrame(float dt) {

        TextureRegion region;

        currentState = getState();
        updateOrientation();


        switch (currentState) {
            case WALKING:
                if (currentOrientation == Orientation.EAST || currentOrientation == Orientation.WEST) {
                    region = (TextureRegion) walkingEast.getKeyFrame(stateTimer, true);
                    break;
                }
                if (currentOrientation == Orientation.NORTH) {
                    region = (TextureRegion) walkingNorth.getKeyFrame(stateTimer, true);
                    break;
                }
                if (currentOrientation == Orientation.SOUTH) {
                    region = (TextureRegion) walkingSouth.getKeyFrame(stateTimer, true);
                    break;
                }
            case STANDING:
                if (currentOrientation == Orientation.EAST || currentOrientation == Orientation.WEST) {
                    region = archerStandingEast;
                    break;
                }
                if (currentOrientation == Orientation.NORTH) {
                    region = archerStandingNorth;
                    break;
                }
                if (currentOrientation == Orientation.SOUTH) {
                    region = archerStandingSouth;
                    break;
                }
            default:
                region = archerStandingNorth;
                break;
        }

        flipRegionIfNeeded(region);
        updateState(dt);


        return region;
    }

    private void updateState(float dt) {
        if (currentState == previousState) {
            stateTimer += dt;
        } else {
            stateTimer = 0;
        }
        previousState = currentState;
    }

    private void flipRegionIfNeeded(TextureRegion region) {
        if (currentOrientation == Orientation.WEST && !region.isFlipX()) {
            region.flip(true, false);
        }
        if(currentOrientation == Orientation.EAST && region.isFlipX()){
            region.flip(true, false);
        }
    }

    private void updateOrientation() {

        previousOrientation = currentOrientation;

        if (body.getLinearVelocity().x > 0) {
            currentOrientation = Orientation.EAST;
            return;
        }
        if (body.getLinearVelocity().x < 0) {
            currentOrientation = Orientation.WEST;
            return;
        }
        if (body.getLinearVelocity().y > 0) {
            currentOrientation = Orientation.NORTH;
            return;
        }
        if (body.getLinearVelocity().y < 0) {
            currentOrientation = Orientation.SOUTH;
        }
    }


    //METODO PARA SABER SE ESTA A AND
    private State getState() {

        if (body.getLinearVelocity().x != 0 || body.getLinearVelocity().y != 0) {
            return State.WALKING;
        }

        return State.STANDING;
    }

    public void fire(Vector2 velocityVector, boolean fireRight) {

        ArcherGame.manager.get("audio/sounds/arrow-shot.wav", Sound.class).play();

        if (projectiles.size < NUMBER_PROJECTILES) {
            projectiles.add(new Projectile(gameScreen, body.getPosition(), velocityVector, fireRight));
        }
    }


}
