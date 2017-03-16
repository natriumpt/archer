package org.academiadecodigo.hackathon.archer.sprites.projectile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.academiadecodigo.hackathon.archer.ArcherGame;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;
import org.academiadecodigo.hackathon.archer.sprites.Archer;

public class Projectile extends Sprite{

    GameScreen gameScreen;
    World world;
    Body body;
    Archer archer;

    public Projectile(GameScreen gameScreen, Archer archer){

        this.gameScreen = gameScreen;
        this.world = gameScreen.getWorld();
        this.archer = archer;
        defineProjectile();
    }

    private void defineProjectile(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(archer.getOriginX(), archer.getOriginY());

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(250 / ArcherGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        body.setLinearVelocity(new Vector2(10f, 0));

        shape.dispose();

    }



}
