package org.academiadecodigo.hackathon.archer;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import org.academiadecodigo.hackathon.archer.screens.GameScreen;

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
            bdef.position.set(rect.getX() + rect.getWidth()/2, rect.getY() + rect.getHeight()/2);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }


}
