package Com.pack.Mario.Sprites.Enemies;

import Com.pack.Mario.Screens.PlayScreen;
import Com.pack.Mario.Sprites.Mario;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;


/**
 * Created by brentaureli on 9/14/15.
 */
public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;

    // ✅ Thêm vào đây
    protected boolean setToDestroy = false;
    protected boolean destroyed = false;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(-1, -2);
        b2body.setActive(false);
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void hitOnHead(Mario mario);
    public abstract void hitByEnemy(Enemy enemy);
    public void hitByFireBall() {
        // Ví dụ đơn giản: kẻ địch biến mất
        setToDestroy = true;
    }

    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }
}
