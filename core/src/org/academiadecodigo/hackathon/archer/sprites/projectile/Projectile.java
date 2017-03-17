package org.academiadecodigo.hackathon.archer.sprites.projectile;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.academiadecodigo.hackathon.archer.ArcherGame;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;
import org.academiadecodigo.hackathon.archer.sprites.Animatable;

public class Projectile extends Animatable {

    public static final String ATLAS = "projectileset.atlas";
    public static final String ARROW_N = "arrow_n";
    public static final String ARROW_S = "arrow_s";
    public static final String ARROW_E = "arrow_e";

    public static final float WIDTH = 32;
    public static final float HEIGTH = 32;

    private GameScreen gameScreen;
    private World world;
    public Body body;
    private boolean fireRight;


    public Projectile(GameScreen gameScreen, Vector2 vector2, Vector2 velocityVector, boolean fireRight, Orientation orientation) {

        this.gameScreen = gameScreen;
        this.world = gameScreen.getWorld();
        this.fireRight = fireRight;
        this.atlas = new TextureAtlas(ATLAS);

        setTextureRegions();

        setBounds(0, 0, WIDTH / ArcherGame.PPM, HEIGTH / ArcherGame.PPM);

        defineProjectile(vector2.x, vector2.y, velocityVector);

        currentOrientation = orientation;
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public State getState() {
        return currentState;
    }

    @Override
    public void setTextureRegions() {

        standingNorth = new TextureRegion(atlas.findRegion(ARROW_N));
        standingEast = new TextureRegion(atlas.findRegion(ARROW_E));
        standingSouth = new TextureRegion(atlas.findRegion(ARROW_S));
    }

    @Override
    public TextureRegion getFrame(float dt) {

        TextureRegion region = standingSouth;

        if (currentOrientation == Orientation.EAST || currentOrientation == Orientation.WEST) {
            region = standingEast;
        }
        if (currentOrientation == Orientation.NORTH) {
            region = standingNorth;
        }
        if (currentOrientation == Orientation.SOUTH) {
            region = standingSouth;
        }

        return region;
    }

    private void defineProjectile(float x, float y, Vector2 velocityVector) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(fireRight ? x : x - 1 / ArcherGame.PPM, y);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(9 / ArcherGame.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        body.setLinearVelocity(velocityVector);

        shape.dispose();

    }

    public void update(float dt) {

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

}



