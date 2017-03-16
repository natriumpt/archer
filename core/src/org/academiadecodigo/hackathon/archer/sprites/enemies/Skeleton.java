package org.academiadecodigo.hackathon.archer.sprites.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import org.academiadecodigo.hackathon.archer.ArcherGame;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;
import org.academiadecodigo.hackathon.archer.sprites.projectile.Projectile;

import java.util.ArrayList;

public class Skeleton extends Enemy {

    public static final int MAX_ACTIVE_PROJECTILES = 10;

    private static final float SPEED = 10;
    private ArrayList<Projectile> projectiles;

    public Skeleton(GameScreen screen, float initial_x, float initial_y) {
        super(screen, initial_x, initial_y);
    }

    @Override
    protected void defineEnemy() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(200, 200);

        enemyBody = getWorld().createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(400 / ArcherGame.PPM);

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

    }

    @Override
    public void hitByEnemy(Enemy enemy) {

    }


    public void moveToArcher(){

        Vector2 enemyPos = new Vector2(enemyBody.getPosition());
        Vector2 archerPos = new Vector2(getGameScreen().getArcher().body.getPosition());

        //nor : Normaliza o vector
        Vector2 delta = enemyPos.sub(archerPos).nor();

        // Take alien current position and add the delta times velocity times delta-time
        Vector2 newPos = new Vector2(enemyBody.getPosition());
        newPos.add(delta.scl(SPEED * Gdx.graphics.getDeltaTime()));

    }

    public void fire() {

    }
}
