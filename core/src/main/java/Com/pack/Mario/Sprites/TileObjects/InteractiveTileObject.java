package Com.pack.Mario.Sprites.TileObjects;

import Com.pack.Mario.Main;
import Com.pack.Mario.Screens.PlayScreen;
import Com.pack.Mario.Sprites.Mario;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;


/**
 * Created by brentaureli on 8/28/15.
 */
public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected Rectangle bounds;
    protected Body body;
    protected PlayScreen screen;
    protected MapObject object;

    protected Fixture fixture;

    public InteractiveTileObject(PlayScreen screen, MapObject object) {
        this.object = object;
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
//        Lấy thông tin hình chữ nhật (Rectangle) từ MapObject trong Tiled Map.
//        Tạo Box2D StaticBody để đại diện cho object đó trong thế giới vật lý
        this.bounds = ((RectangleMapObject) object).getRectangle();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
//        Dùng Main.PPM (Pixel Per Meter) để chuyển đổi từ pixel sang mét (chuẩn của Box2D).
//            Tạo PolygonShape với kích thước là nửa chiều rộng và nửa chiều cao.
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / Main.PPM, (bounds.getY() + bounds.getHeight() / 2) / Main.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / Main.PPM, bounds.getHeight() / 2 / Main.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);

    }

    public abstract void onHeadHit(Mario mario);

    //Giúp set nhóm va chạm (category) cho đối tượng — rất quan trọng khi bạn muốn điều khiển ai có thể va chạm với ai trong Box2D.
    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    //Lấy ô tile hiện tại (Cell) tại vị trí của object.
//layer 1 phải là TileLayer có tileset, không phải ObjectLayer.
//Dùng khi bạn muốn thay tile (ví dụ đập Brick thì đổi thành tile khác hoặc xoá tile).
    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int) (body.getPosition().x * Main.PPM / 16),
            (int) (body.getPosition().y * Main.PPM / 16));
    }

}
