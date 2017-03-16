package org.academiadecodigo.hackathon.archer.sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.academiadecodigo.hackathon.archer.ArcherGame;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;

public class Archer extends Sprite {

    public World world;
    public Body body;
    private GameScreen gameScreen;

    public Archer(GameScreen gameScreen){

        this.gameScreen = gameScreen;
        this.world = gameScreen.getWorld();

        defineArcher();
    }



    private void init(){

    }

    private void defineArcher(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(32 / ArcherGame.PPM, 32 / ArcherGame.PPM);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(5 / ArcherGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
//        fixtureDef.density = 1f;
//        fixtureDef.friction = 0.4f;
//        fixtureDef.restitution = 0.6f;
        body.createFixture(fixtureDef);

//        shape.dispose();


    }

}
