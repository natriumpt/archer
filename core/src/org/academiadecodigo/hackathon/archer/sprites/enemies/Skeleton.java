package org.academiadecodigo.hackathon.archer.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.academiadecodigo.hackathon.archer.ArcherGame;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;
import org.academiadecodigo.hackathon.archer.sprites.Animatable;

public class Skeleton extends Enemy {

    private static final float SPEED = 1;
    private boolean dead;
    private final int points = 100;


    public Skeleton(GameScreen gameScreen, float initialX, float initialY) {
        super(gameScreen);

        atlas = new TextureAtlas("skeletonset.atlas");

        setTextureRegions();
        setAnimations(0.3f);

        setBounds(0, 0, 48 / ArcherGame.PPM, 48 / ArcherGame.PPM);
        setRegion(standingSouth);

        defineEnemy(initialX, initialY);
        init();
    }


    private void init() {
        previousState = Animatable.State.STANDING;
        currentState = Animatable.State.STANDING;
        currentOrientation = Animatable.Orientation.SOUTH;
        stateTimer = 0;
    }


    @Override
    protected void defineEnemy(float initialX, float initialY) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(initialX, initialY);

        enemyBody = getWorld().createBody(bodyDef);
        enemyBody.setActive(false);

        CircleShape shape = new CircleShape();
        shape.setRadius(15 / ArcherGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        enemyBody.createFixture(fixtureDef);
//        shape.dispose();

    }

    @Override
    public void update(float dt) {

        moveToArcher();
        setPosition(enemyBody.getPosition().x - getWidth() / 2, enemyBody.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    @Override
    public Body getBody() {
        return enemyBody;
    }

    @Override
    public State getState() {

        if (isDead()) {
            return State.DEAD;
        }
        if (enemyBody.getLinearVelocity().x != 0 || enemyBody.getLinearVelocity().y != 0) {
            return State.WALKING;
        }
        return State.STANDING;
    }


    public void moveToArcher() {

        Vector2 enemyPos = new Vector2(enemyBody.getPosition());
        Vector2 archerPos = new Vector2(getGameScreen().getArcher().body.getPosition());

        //nor : Normaliza o vector
        Vector2 delta = archerPos.sub(enemyPos).nor();
        enemyBody.setLinearVelocity(delta);
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
