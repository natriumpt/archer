package org.academiadecodigo.hackathon.archer;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;
import org.academiadecodigo.hackathon.archer.sprites.enemies.Skeleton;

import java.util.ArrayList;

public class BodyWorldCreator {

    public BodyWorldCreator(GameScreen gameScreen){

        TiledMap map = gameScreen.getMap();
        World world = gameScreen.getWorld();

        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //Create tree boundaries body/fixtures
        for (MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/ArcherGame.PPM , (rect.getY() + rect.getHeight()/2)/ArcherGame.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2/ArcherGame.PPM, rect.getHeight()/2/ArcherGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //Spawn zombies based on the map object
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {

            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            gameScreen.skeletons.add(new Skeleton(gameScreen, rect.getX()/ArcherGame.PPM, rect.getY()/ArcherGame.PPM));

        }
    }


}
