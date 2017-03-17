package org.academiadecodigo.hackathon.archer.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Created by codecadet on 17/03/17.
 */
public abstract class Animatable extends Sprite {

    public static final String STANDING_N = "standing_n";
    public static final String STANDING_S = "standing_s";
    public static final String STANDING_E = "standing_e";
    public static final String WALKING_N = "walking_n";
    public static final String WALKING_S = "walking_s";
    public static final String WALKING_E = "walking_e";

    public static final float PIXEL_WIDTH = 48;
    public static final float PIXEL_HEIGHT = 48;


    public enum State {STANDING, WALKING, FIRING, DEAD}
    public enum Orientation {NORTH, SOUTH, EAST, WEST}

    protected State currentState;
    protected State previousState;
    protected Orientation currentOrientation;
    protected Orientation previousOrientation;
    protected float stateTimer;

    public World world;
    public Body body;

    protected TextureAtlas atlas;
    protected TextureRegion standingNorth;
    protected TextureRegion standingEast;
    protected TextureRegion standingSouth;
    protected Animation walkingNorth;
    protected Animation walkingEast;
    protected Animation walkingSouth;
    protected Animation deathAnimation;


    public abstract Body getBody();

    public abstract State getState();


    protected void setTextureRegions() {

        standingNorth = new TextureRegion(atlas.findRegion(STANDING_N));
        standingSouth = new TextureRegion(atlas.findRegion(STANDING_S));
        standingEast = new TextureRegion(atlas.findRegion(STANDING_E));
    }

    protected void setAnimations(float delay) {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(atlas.findRegion(WALKING_E, i)));
        }
        walkingEast = new Animation(delay, frames);
        frames.clear();

        for (int i = 1; i < 5; i++) {
            frames.add(new TextureRegion(atlas.findRegion(WALKING_N, i)));
        }
        walkingNorth = new Animation(delay, frames);
        frames.clear();

        for (int i = 1; i < 5; i++) {
            frames.add(new TextureRegion(atlas.findRegion(WALKING_S, i)));
        }
        walkingSouth = new Animation(delay, frames);
        frames.clear();

        /*for (int i = 1; i < 3; i++) {
            frames.add(new TextureRegion(atlas.findRegion("death", i)));
        }
        deathAnimation = new Animation(delay, frames);
        frames.clear();*/
    }


    protected TextureRegion getFrame(float dt) {

        TextureRegion region;
        currentState = getState();
        updateOrientation();

        switch (currentState) {
            case WALKING:
                if (currentOrientation == Orientation.EAST || currentOrientation == Orientation.WEST) {
                    region = (TextureRegion) walkingEast.getKeyFrame(stateTimer, true);
                    break;
                }
                if (currentOrientation == Orientation.NORTH) {
                    region = (TextureRegion) walkingNorth.getKeyFrame(stateTimer, true);
                    break;
                }
                if (currentOrientation == Orientation.SOUTH) {
                    region = (TextureRegion) walkingSouth.getKeyFrame(stateTimer, true);
                    break;
                }
            case STANDING:
                if (currentOrientation == Orientation.EAST || currentOrientation == Orientation.WEST) {
                    region = standingEast;
                    break;
                }
                if (currentOrientation == Orientation.NORTH) {
                    region = standingNorth;
                    break;
                }
                if (currentOrientation == Orientation.SOUTH) {
                    region = standingSouth;
                    break;
                }
            case DEAD:
                region = (TextureRegion) deathAnimation.getKeyFrame(stateTimer,true);
                break;
            default:
                region = standingSouth;
                break;
        }

        flipRegionIfNeeded(region);
        updateState(dt);
        return region;
    }


    private void updateState(float dt) {

        if (currentState == previousState) {
            stateTimer += dt;
        } else {
            stateTimer = 0;
        }
        previousState = currentState;
    }


    public void flipRegionIfNeeded(TextureRegion region) {

        if (currentOrientation == Orientation.WEST && !region.isFlipX()) {
            region.flip(true, false);
        }
        if (currentOrientation == Orientation.EAST && region.isFlipX()) {
            region.flip(true, false);
        }
    }


    private void updateOrientation() {

        previousOrientation = currentOrientation;

        if (getBody().getLinearVelocity().x > 0) {
            currentOrientation = Orientation.EAST;
            return;
        }
        if (getBody().getLinearVelocity().x < 0) {
            currentOrientation = Orientation.WEST;
            return;
        }
        if (getBody().getLinearVelocity().y > 0) {
            currentOrientation = Orientation.NORTH;
            return;
        }
        if (getBody().getLinearVelocity().y < 0) {
            currentOrientation = Orientation.SOUTH;
        }
    }
}
