package Com.pack.Mario.Sprites.TileObjects;

import Com.pack.Mario.Main;
import Com.pack.Mario.Scenes.Hud;
import Com.pack.Mario.Screens.PlayScreen;
import Com.pack.Mario.Sprites.Mario;
import Com.pack.Mario.Sprites.TileObjects.InteractiveTileObject;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by brentaureli on 8/28/15.
 */
public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Main.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if(mario.isBig()) {
            setCategoryFilter(Main.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);
            Main.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
        }
        Main.manager.get("audio/sounds/bump.wav", Sound.class).play();
    }

}
