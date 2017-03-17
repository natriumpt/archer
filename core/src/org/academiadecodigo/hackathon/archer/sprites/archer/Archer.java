package org.academiadecodigo.hackathon.archer.sprites.archer;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.academiadecodigo.hackathon.archer.ArcherGame;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;
import org.academiadecodigo.hackathon.archer.sprites.Animatable;
import org.academiadecodigo.hackathon.archer.sprites.projectile.Projectile;

public class Archer extends Animatable {

    public static final int NUMBER_PROJECTILES = 5;


    public Vector2 velocityVector;
    private GameScreen gameScreen;

    public Array<Projectile> projectiles;
    public float speed;

    public Archer(GameScreen gameScreen) {

        this.world = gameScreen.getWorld();
        this.gameScreen = gameScreen;
        this.atlas = new TextureAtlas("archerset.atlas");

        setTextureRegions();
        setAnimations(0.1f);

        setBounds(0, 0, 48 / ArcherGame.PPM, 48 / ArcherGame.PPM);
        setRegion(standingNorth);

        defineArcher();

        init();


    }

    private void init() {
        projectiles = new Array<Projectile>();
        previousState = State.STANDING;
        currentState = State.STANDING;
        currentOrientation = Orientation.NORTH;
        stateTimer = 0;
        speed = 3f;

    }



    public void update(float dt) {

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        checkCollisionWithObstacles();

        for (Projectile p: projectiles) {
            p.update(dt);
        }
    }

    private void checkCollisionWithObstacles() {
        for (Projectile projectile: projectiles) {
            if ((projectile.body.getLinearVelocity().x < 2 && projectile.body.getLinearVelocity().y < 2)
                    && (projectile.body.getLinearVelocity().x > -2 && projectile.body.getLinearVelocity().y > -2)){
                ArcherGame.manager.get("audio/sounds/arrow-hit.wav", Sound.class).play();
                projectile.body.setTransform(1000000f,1000000f, projectile.body.getAngle());
                projectiles.removeValue(projectile, true);
            }
        }
    }

    private void defineArcher() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(300 / ArcherGame.PPM, 300 / ArcherGame.PPM);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(15 / ArcherGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        velocityVector = new Vector2(0, 0);

    }


    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public State getState() {

        if (body.getLinearVelocity().x != 0 || body.getLinearVelocity().y != 0) {
            return State.WALKING;
        }

        return State.STANDING;
    }

    public void fire(Vector2 velocityVector, boolean fireRight) {

        ArcherGame.manager.get("audio/sounds/arrow-shot.wav", Sound.class).play();

        if (projectiles.size < NUMBER_PROJECTILES) {
            projectiles.add(new Projectile(gameScreen, body.getPosition(), velocityVector, fireRight));
        }
    }


}
