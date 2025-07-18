// === FireBall.java ===
package Com.pack.Mario.Sprites.Other;

import Com.pack.Mario.Main;
import Com.pack.Mario.Screens.PlayScreen;
import Com.pack.Mario.Sprites.Enemies.Enemy;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class FireBall extends Sprite {
    private final World world;
    private final Animation<TextureRegion> fireAnimation;
    private final boolean fireRight;
    private final PlayScreen screen;
    private float stateTime;
    private boolean destroyed;
    private boolean setToDestroy;
    private Body b2body;

    public FireBall(PlayScreen screen, float x, float y, boolean fireRight) {
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("fireball"), i * 8, 0, 8, 8));
        }
        fireAnimation = new Animation<>(0.2f, frames);
        setRegion(fireAnimation.getKeyFrame(0));
        setBounds(x, y, 6 / Main.PPM, 6 / Main.PPM);

        if (!world.isLocked()) defineFireBall();
        else {
            // Delay body creation to update step (optional fallback logic)
            System.out.println("World is locked during FireBall init, skipping creation.");
        }
    }

    private void defineFireBall() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 12 / Main.PPM : getX() - 12 / Main.PPM, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / Main.PPM);
        fdef.filter.categoryBits = Main.FIREBALL_BIT;
        fdef.filter.maskBits = Main.GROUND_BIT |
            Main.COIN_BIT |
            Main.BRICK_BIT |
            Main.ENEMY_BIT |
            Main.OBJECT_BIT;

        fdef.shape = shape;
        fdef.restitution = 1;
        fdef.friction = 0;

        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(fireRight ? 2 : -2, 2.5f));
    }

    public void update(float dt) {
        stateTime += dt;
        setRegion(fireAnimation.getKeyFrame(stateTime, true));

        if (destroyed || b2body == null) return;

        setPosition(b2body.getPosition().x - getWidth() / 2,
            b2body.getPosition().y - getHeight() / 2);

        if ((stateTime > 3 || setToDestroy) && !destroyed) {
            if (!world.isLocked()) {
                world.destroyBody(b2body);
                b2body = null;
                destroyed = true;
            }
        }

        if (b2body != null) {
            if (b2body.getLinearVelocity().y > 2f)
                b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);

            if ((fireRight && b2body.getLinearVelocity().x < 0) ||
                (!fireRight && b2body.getLinearVelocity().x > 0))
                setToDestroy();
        }
    }

    public void setToDestroy() {
        setToDestroy = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void hit(Enemy enemy) {
        setToDestroy();
        enemy.hitByFireBall();
    }
}
