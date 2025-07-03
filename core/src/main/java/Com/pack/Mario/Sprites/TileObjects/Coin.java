package Com.pack.Mario.Sprites.TileObjects;

import Com.pack.Mario.Main;
import Com.pack.Mario.Scenes.Hud;
import Com.pack.Mario.Screens.PlayScreen;
import Com.pack.Mario.Sprites.Items.ItemDef;
import Com.pack.Mario.Sprites.Items.Mushroom;
import Com.pack.Mario.Sprites.Mario;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;




/**
 * Created by brentaureli on 8/28/15.
 */
public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Coin(PlayScreen screen, MapObject object){
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(Main.COIN_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if(getCell().getTile().getId() == BLANK_COIN)
            Main.manager.get("audio/sounds/bump.wav", Sound.class).play();
        else {
            if(object.getProperties().containsKey("mushroom")) {
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / Main.PPM),
                        Mushroom.class));
                Main.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
            }
            else
                Main.manager.get("audio/sounds/coin.wav", Sound.class).play();
            getCell().setTile(tileSet.getTile(BLANK_COIN));
            Hud.addScore(100);
        }
    }
}
