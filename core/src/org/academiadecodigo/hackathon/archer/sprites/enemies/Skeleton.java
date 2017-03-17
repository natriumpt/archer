package org.academiadecodigo.hackathon.archer.sprites.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.academiadecodigo.hackathon.archer.ArcherGame;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;
import org.academiadecodigo.hackathon.archer.sprites.archer.Archer;
import org.academiadecodigo.hackathon.archer.sprites.projectile.Projectile;

import java.util.ArrayList;

public class Skeleton extends Enemy {

    public static final int MAX_ACTIVE_PROJECTILES = 10;
    private static final float SPEED = 1;
    private ArrayList<Projectile> projectiles;
    private boolean dead;
    private final int points = 100;

    public enum State {STANDING, WALKING, FIRING, DEAD}
    public enum Orientation {NORTH, SOUTH, EAST, WEST}

    public State currentState;
    public State previousState;
    public Orientation currentOrientation;
    public Orientation previousOrientation;
    private float stateTimer;

    public World world;
    public Body body;

    private TextureAtlas atlas;

    private TextureRegion standingNorth;
    private TextureRegion standingEast;
    private TextureRegion standingSouth;
    private Animation walkingNorth;
    private Animation walkingEast;
    private Animation walkingSouth;

    public Skeleton(GameScreen gameScreen, float initialX, float initialY) {
        super(gameScreen);

        atlas = new TextureAtlas("skeletonset.atlas");

        setTextureRegions();
        setAnimations();
//
        setBounds(0, 0, 48 / ArcherGame.PPM, 48 / ArcherGame.PPM);
        setRegion(standingSouth);

        defineEnemy(initialX, initialY);

        init();
    }

    private void init() {
        previousState = State.STANDING;
        currentState = State.STANDING;
        currentOrientation = Orientation.SOUTH;
        stateTimer = 0;
    }


    private void setTextureRegions() {
        standingNorth = new TextureRegion(atlas.findRegion("standing_n"));
        standingSouth = new TextureRegion(atlas.findRegion("standing_s"));
        standingEast = new TextureRegion(atlas.findRegion("standing_e"));
    }

    private void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 1; i < 5; i++) {
            frames.add(new TextureRegion(atlas.findRegion("walking_e", i)));
        }
        walkingEast = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 1; i < 5; i++) {
            frames.add(new TextureRegion(atlas.findRegion("walking_n", i)));
        }
        walkingNorth = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 1; i < 5; i++) {
            frames.add(new TextureRegion(atlas.findRegion("walking_s", i)));
        }
        walkingSouth = new Animation(0.1f, frames);
        frames.clear();
    }

    @Override
    protected void defineEnemy(float initialX, float initialY) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(initialX , initialY);

        enemyBody = getWorld().createBody(bodyDef);
        enemyBody.setActive(false);

        CircleShape shape = new CircleShape();
        shape.setRadius( 5 / ArcherGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
//        fixtureDef.density = 1f;
//        fixtureDef.friction = 0.4f;
//        fixtureDef.restitution = 0.6f;
        enemyBody.createFixture(fixtureDef);
//        shape.dispose();

    }

    @Override
    public void update(float dt) {

        moveToArcher();
        setPosition(enemyBody.getPosition().x - getWidth() / 2 , enemyBody.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

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
                    region = standingEast;
                    break;
                }
                if (currentOrientation == Orientation.NORTH) {
                    region = standingNorth;
                    break;
                }
                if (currentOrientation == Orientation.SOUTH) {
                    region = standingSouth;
                    break;
                }
            default:
                region = standingSouth;
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

        if (enemyBody.getLinearVelocity().x > 0) {
            currentOrientation = Orientation.EAST;
            return;
        }
        if (enemyBody.getLinearVelocity().x < 0) {
            currentOrientation = Orientation.WEST;
            return;
        }
        if (enemyBody.getLinearVelocity().y > 0) {
            currentOrientation = Orientation.NORTH;
            return;
        }
        if (enemyBody.getLinearVelocity().y < 0) {
            currentOrientation = Orientation.SOUTH;
        }
    }


    //METODO PARA SABER SE ESTA A AND
    private State getState() {

        if (enemyBody.getLinearVelocity().x != 0 || enemyBody.getLinearVelocity().y != 0) {
            return State.WALKING;
        }

        return State.STANDING;
    }


    public void moveToArcher(){

        Vector2 enemyPos = new Vector2(enemyBody.getPosition());
        Vector2 archerPos = new Vector2(getGameScreen().getArcher().body.getPosition());

        //nor : Normaliza o vector
        Vector2 delta = archerPos.sub(enemyPos).nor();
        enemyBody.setLinearVelocity(delta);


    }

    public void fire() {

    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getPoints() {
        return points;
    }
}
