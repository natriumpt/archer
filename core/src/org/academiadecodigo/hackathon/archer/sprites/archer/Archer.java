package org.academiadecodigo.hackathon.archer.sprites.archer;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.academiadecodigo.hackathon.archer.ArcherGame;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;
import org.academiadecodigo.hackathon.archer.sprites.projectile.Projectile;

public class Archer extends Sprite {

    public World world;
    public Body body;
    public Vector2 velocityVector;
    private GameScreen gameScreen;
    private TextureRegion archerStanding;

    public Array<Projectile> projectiles;
    public static final int NUMBER_PROJECTILES = 50;


    public enum State {STANDING, WALKING, FIRING, DEAD}

    public State currentState;
    public State previousState;
    private float stateTimer;

    public enum Orientation {NORTH, SOUTH, EAST, WEST}

    public Orientation currentOrientation;
    public Orientation previousOrientation;

    private Animation walkingNorth;
    private Animation walkingSouth;
    private Animation walkingEast;
    private Animation walkingWest;

    public Archer(GameScreen gameScreen) {

        super(gameScreen.getAtlas().findRegion("standing_n"));
        this.world = gameScreen.getWorld();
        this.gameScreen = gameScreen;

        archerStanding = new TextureRegion(getTexture(), 2, 2, 48, 48);
        setBounds(0, 0, 48 / ArcherGame.PPM, 48 / ArcherGame.PPM);
        setRegion(archerStanding);

        defineArcher();

        projectiles = new Array<Projectile>();
        previousState = State.STANDING;
        currentState = State.STANDING;
        currentOrientation = Orientation.NORTH;
        stateTimer = 0;

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

//        for (int i = 1; i < 5; i++) {
//            frames.add(new TextureRegion(gameScreen.getAtlas().findRegion("walking_w", i)));
//        }
//        walkingWest = new Animation(0.1f, frames);
//        frames.clear();

        for (int i = 1; i < 5; i++) {
            frames.add(new TextureRegion(gameScreen.getAtlas().findRegion("walking_s", i)));
        }
        walkingSouth = new Animation(0.1f, frames);
        frames.clear();
    }

    private void init() {

    }

    public void update(float dt) {

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
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

        currentState = getState();

        TextureRegion region;

        currentOrientation = updateOrientation();

        switch (currentState) {
            case WALKING:
                if (currentOrientation.equals(Orientation.EAST)) {
                    region = (TextureRegion) walkingEast.getKeyFrame(stateTimer, true);
                    break;
                }
//                if (currentOrientation.equals(Orientation.WEST)) {
//                    region = (TextureRegion) walkingWest.getKeyFrame(stateTimer, true);
//                    break;
//                }
                if (currentOrientation.equals(Orientation.NORTH)) {
                    region = (TextureRegion) walkingNorth.getKeyFrame(stateTimer, true);
                    break;
                }
                if (currentOrientation.equals(Orientation.SOUTH)) {
                    region = (TextureRegion) walkingSouth.getKeyFrame(stateTimer, true);
                    break;
                }
            default:
                region = archerStanding;
                break;
        }


        if (currentState == previousState) {
            stateTimer += dt;
        } else {
            stateTimer = 0;
        }
        previousState = currentState;
        return region;
    }

    private Orientation updateOrientation() {

        if (body.getLinearVelocity().x > 0) {
            return Orientation.EAST;
        }
        if (body.getLinearVelocity().x < 0) {
            return Orientation.WEST;
        }
        if (body.getLinearVelocity().y > 0) {
            return Orientation.NORTH;
        }
        if (body.getLinearVelocity().y < 0) {
            return Orientation.SOUTH;
        }
        return currentOrientation;
    }


    //METODO PARA SABER SE ESTA A AND
    private State getState() {

        if (body.getLinearVelocity().x != 0 || body.getLinearVelocity().y != 0) {
            return State.WALKING;
        }

        return State.STANDING;
    }

    public void fire(Vector2 velocityVector, boolean fireRight) {
        if (projectiles.size < NUMBER_PROJECTILES) {
            projectiles.add(new Projectile(gameScreen, body.getPosition(), velocityVector, fireRight));
        }
    }


}
