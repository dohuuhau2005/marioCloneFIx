package Com.pack.Mario.Tools;

import Com.pack.Mario.Main;
import Com.pack.Mario.Screens.PlayScreen;
import Com.pack.Mario.Sprites.Enemies.Enemy;
import Com.pack.Mario.Sprites.Enemies.Goomba;
import Com.pack.Mario.Sprites.Enemies.Turtle;
import Com.pack.Mario.Sprites.TileObjects.Brick;
import Com.pack.Mario.Sprites.TileObjects.Coin;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class B2WorldCreator {
    private final Array<Goomba> goombas;
    private final Array<Turtle> turtles;

    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get("Ground").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Main.PPM, (rect.getY() + rect.getHeight() / 2) / Main.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / Main.PPM, rect.getHeight() / 2 / Main.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        for (MapObject object : map.getLayers().get("Pipes").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Main.PPM, (rect.getY() + rect.getHeight() / 2) / Main.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / Main.PPM, rect.getHeight() / 2 / Main.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = Main.OBJECT_BIT;
            body.createFixture(fdef);
        }

        MapLayer endLayer = map.getLayers().get("end");
        if (endLayer != null) {
            for (MapObject object : endLayer.getObjects().getByType(RectangleMapObject.class)) {

                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / Main.PPM, (rect.getY() + rect.getHeight() / 2) / Main.PPM);
                body = world.createBody(bdef);
                shape.setAsBox(rect.getWidth() / 2 / Main.PPM, rect.getHeight() / 2 / Main.PPM);
                fdef.shape = shape;
                fdef.isSensor = true;
                body.createFixture(fdef).setUserData("end");
            }

            for (MapObject object : map.getLayers().get("Bricks").getObjects().getByType(RectangleMapObject.class)) {
                new Brick(screen, object);
            }

            for (MapObject object : map.getLayers().get("Coins").getObjects().getByType(RectangleMapObject.class)) {
                new Coin(screen, object);
            }
        }

        goombas = new Array<Goomba>();
        MapLayer goombasLayer = map.getLayers().get("Goombas");
        if (goombasLayer != null) {
            for (MapObject object : goombasLayer.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                goombas.add(new Goomba(screen, rect.getX() / Main.PPM, rect.getY() / Main.PPM));
            }
        }

        turtles = new Array<Turtle>();
        MapLayer turtlesLayer = map.getLayers().get("Turtles");
        if (turtlesLayer != null) {
            for (MapObject object : turtlesLayer.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                turtles.add(new Turtle(screen, rect.getX() / Main.PPM, rect.getY() / Main.PPM));
            }
        }
    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }

    public Array<Enemy> getEnemies() {
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return enemies;
    }
}
