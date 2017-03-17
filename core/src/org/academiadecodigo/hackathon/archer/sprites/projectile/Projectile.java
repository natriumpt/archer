package org.academiadecodigo.hackathon.archer.sprites.projectile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.academiadecodigo.hackathon.archer.ArcherGame;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;
import org.academiadecodigo.hackathon.archer.sprites.Animatable;
import org.academiadecodigo.hackathon.archer.sprites.archer.Archer;

public class Projectile extends Animatable {


    GameScreen gameScreen;
    World world;
    public Body body;
    boolean fireRight;


    public Projectile(GameScreen gameScreen, Vector2 vector2, Vector2 velocityVector, boolean fireRight) {

        this.gameScreen = gameScreen;
        this.world = gameScreen.getWorld();
        this.fireRight = fireRight;
        this.atlas = new TextureAtlas("projectileset.atlas");

        setTextureRegions();

        setBounds(0, 0, 32 / ArcherGame.PPM, 32 / ArcherGame.PPM);

        defineProjectile(vector2.x, vector2.y, velocityVector);
    }

    @Override
    public Body getBody() {
        return null;
    }

    @Override
    public State getState() {
        return null;
    }

    public void setTextureRegions() {

        standingNorth = new TextureRegion(atlas.findRegion("arrow_n"));
        standingEast = new TextureRegion(atlas.findRegion("arrow_e"));
        standingSouth = new TextureRegion(atlas.findRegion("arrow_s"));
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

    public TextureRegion getFrame(float dt) {

        TextureRegion region = standingSouth;

        currentOrientation = gameScreen.getArcher().getCurrentOrientation();

        if (currentOrientation == Orientation.EAST || currentOrientation == Orientation.WEST) {
            region = standingEast;
        }
        if (currentOrientation == Orientation.NORTH) {
            region = standingNorth;
        }
        if (currentOrientation == Orientation.SOUTH) {
            region = standingSouth;
        }

        flipRegionIfNeeded(region);

        return region;
    }

    private void flipRegionIfNeeded(TextureRegion region) {

        if (currentOrientation == Orientation.WEST && !region.isFlipX()) {
            region.flip(true, false);
        }

        if (currentOrientation == Orientation.EAST && region.isFlipX()) {
            region.flip(true, false);
        }
    }
}



