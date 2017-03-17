package org.academiadecodigo.hackathon.archer.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;

public abstract class Enemy extends Sprite {


    protected World world;
    protected GameScreen gameScreen;
    public Body enemyBody;
    Vector2 velocity;

    private int health;

    public Enemy(GameScreen screen){

        this.gameScreen = screen;
        this.world = screen.getWorld();

        velocity = new Vector2(-1f, -2f);
//        enemyBody.setActive(false);
    }

    protected abstract void defineEnemy(float initial_x, float initial_y);

    public abstract void update(float dt);

//    public abstract void hitByEnemy(Enemy enemy);


    public World getWorld(){
        return world;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public Body getEnemyBody() {
        return enemyBody;
    }

    //    public void reverseVelocity(boolean x, boolean y){
//        if(x)
//            velocity.x = -velocity.x;
//        if(y)
//            velocity.y = -velocity.y;
//    }

}
