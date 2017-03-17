package org.academiadecodigo.hackathon.archer.sprites.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
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

    private TextureAtlas atlas;

    private TextureRegion archerStandingNorth;
    private TextureRegion archerStandingEast;
    private TextureRegion archerStandingSouth;
    private Animation walkingNorth;
    private Animation walkingEast;
    private Animation walkingSouth;

    public Skeleton(GameScreen gameScreen, float initialX, float initialY) {
        super(gameScreen, initialX, initialY);

        atlas = new TextureAtlas("skeletonset.atlas");

//        setTextureRegions(gameScreen);
//        setAnimations(gameScreen);
//
//        setTextureRegions(gameScreen);
//        setAnimations(gameScreen);
//
//        setBounds(0, 0, 48 / ArcherGame.PPM, 48 / ArcherGame.PPM);
//        setRegion(archerStandingNorth);
//
//        defineArcher();
//
//        init();
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

    }

    public void moveToArcher(){

        Vector2 enemyPos = new Vector2(enemyBody.getPosition());
        Vector2 archerPos = new Vector2(getGameScreen().getArcher().body.getPosition());

        //nor : Normaliza o vector
        Vector2 delta = archerPos.sub(enemyPos).nor();
        enemyBody.setLinearVelocity(delta);
        setPosition((enemyBody.getPosition().x - getWidth() / 2)/ArcherGame.PPM, (enemyBody.getPosition().y - getHeight() / 2)/ArcherGame.PPM);


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
