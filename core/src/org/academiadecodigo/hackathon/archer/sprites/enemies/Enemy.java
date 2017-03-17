package org.academiadecodigo.hackathon.archer.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;
import org.academiadecodigo.hackathon.archer.sprites.Animatable;

public abstract class Enemy extends Animatable {


    protected World world;
    protected GameScreen gameScreen;
    public Body enemyBody;

    private int health;


    public Enemy(GameScreen screen){

        this.gameScreen = screen;
        this.world = screen.getWorld();
    }

    protected abstract void defineEnemy(float initial_x, float initial_y);

    public abstract void update(float dt);

    public World getWorld(){
        return world;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }


}
