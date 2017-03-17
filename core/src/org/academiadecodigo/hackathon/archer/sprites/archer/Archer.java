package org.academiadecodigo.hackathon.archer.sprites.archer;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.academiadecodigo.hackathon.archer.ArcherGame;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;
import org.academiadecodigo.hackathon.archer.sprites.projectile.Projectile;

public class Archer extends Sprite {

    public World world;
    public Body body;
    public Vector2 velocityVector;
    private GameScreen gameScreen;
    private TextureRegion archerStand;

    public Array<Projectile> projectiles;

    public enum State {STANDING, WALKING, FIRING, DEAD}

    public State currentState;
    public State previousState;
    private float stateTimer;

    public enum Orientation {NORTH, SOUTH, EAST, WEST}

    public Orientation currentOrientation;
    public Orientation previousOrientation;

    private TextureAtlas textureAtlas;

    private Animation walkingEast;

    public Archer(GameScreen gameScreen) {

        super(gameScreen.getAtlas().findRegion("standing_n"));
        this.gameScreen = gameScreen;
        this.world = gameScreen.getWorld();

        archerStand = new TextureRegion(getTexture(), 0, 0, 48, 48);
        setBounds(0, 0, 48 / ArcherGame.PPM, 48 / ArcherGame.PPM);
        setRegion(archerStand);

        defineArcher();

        projectiles = new Array<Projectile>();

        currentState = State.WALKING;
        currentOrientation = Orientation.EAST;

        textureAtlas = new TextureAtlas("archerset.atlas");

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(textureAtlas.findRegion("walking_n")));
            System.out.println(frames);
        }
        walkingEast = new Animation(0.1f, frames);
        setRegion((TextureRegion) walgit add .kingEast.getKeyFrame(stateTimer));

        frames.clear();

    }

    private void init() {

    }

    public void update(float dt) {

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y- getHeight()/2);

    }

    private void defineArcher() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(600 / ArcherGame.PPM, 600 / ArcherGame.PPM);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(5 / ArcherGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        velocityVector = new Vector2(0, 0);

    }

    public TextureRegion getFrame(float dt) {

        TextureRegion region = null;

        switch (currentState) {
            case WALKING:
                region = (TextureRegion) walkingEast.getKeyFrame(stateTimer, true);
                break;
            default:
                break;
        }

        return region;

    }

    public void fire(Vector2 velocityVector, boolean fireRight) {
        projectiles.add(new Projectile(gameScreen, body.getPosition(), velocityVector, fireRight));
    }


}
