package org.academiadecodigo.hackathon.archer.sprites.projectile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.academiadecodigo.hackathon.archer.ArcherGame;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;
import org.academiadecodigo.hackathon.archer.sprites.archer.Archer;

public class Projectile extends Sprite{

    GameScreen gameScreen;
    World world;
    public Body body;
    boolean fireRight;

    public Projectile(GameScreen gameScreen, Vector2 vector2, Vector2 velocityVector, boolean fireRight){

        this.gameScreen = gameScreen;
        this.world = gameScreen.getWorld();
        this.fireRight = fireRight;

        defineProjectile(vector2.x, vector2.y, velocityVector);
    }

    private void defineProjectile(float x, float y, Vector2 velocityVector){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(fireRight? x : x - 1 / ArcherGame.PPM, y);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(3 / ArcherGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        body.setLinearVelocity(velocityVector);

        shape.dispose();

    }

    public void update(){



    }



}
