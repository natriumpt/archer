package org.academiadecodigo.hackathon.archer.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;

public abstract class Enemy extends Sprite {


    private World world;
    private Body body;
    private GameScreen gameScreen;
    private Vector2 velocity;
    private int health;



    public Enemy(GameScreen screen, float initial_x, float initial_y){

        this.world = screen.getWorld();
        this.gameScreen = screen;
        setPosition(initial_x, initial_y);
        defineEnemy();
        velocity = new Vector2(-1f, -2f);
        body.setActive(false);
    }

    protected abstract void defineEnemy();

    public abstract void update(float dt);

    public abstract void hitByEnemy(Enemy enemy);

    void setBody(Body body){
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    World getWorld(){
        return world;
    }


//    public void reverseVelocity(boolean x, boolean y){
//        if(x)
//            velocity.x = -velocity.x;
//        if(y)
//            velocity.y = -velocity.y;
//    }

}
