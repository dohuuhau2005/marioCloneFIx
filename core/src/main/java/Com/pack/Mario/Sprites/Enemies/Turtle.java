package Com.pack.Mario.Sprites.Enemies;

import Com.pack.Mario.Main;
import Com.pack.Mario.Screens.PlayScreen;
import Com.pack.Mario.Sprites.Mario;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

public class Turtle extends Enemy {
    public static final int KICK_LEFT = -2;
    public static final int KICK_RIGHT = 2;

    public enum State {WALKING, STANDING_SHELL, MOVING_SHELL}
    public State currentState;
    public State previousState;

    private TextureRegion shell;
    private Animation<TextureRegion> walkAnimation;
    private float stateTime;

    public Turtle(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 16, 0, 16, 24));
        walkAnimation = new Animation<TextureRegion>(0.2f, frames);

        shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24);

        currentState = previousState = State.WALKING;
        stateTime = 0;

        setBounds(getX(), getY(), 16 / Main.PPM, 24 / Main.PPM);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Main.PPM);

        fdef.filter.categoryBits = Main.ENEMY_BIT;
        fdef.filter.maskBits = Main.GROUND_BIT |
            Main.COIN_BIT |
            Main.BRICK_BIT |
            Main.ENEMY_BIT |
            Main.OBJECT_BIT |
            Main.MARIO_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-5, 8).scl(1 / Main.PPM);
        vertices[1] = new Vector2(5, 8).scl(1 / Main.PPM);
        vertices[2] = new Vector2(-3, 3).scl(1 / Main.PPM);
        vertices[3] = new Vector2(3, 3).scl(1 / Main.PPM);
        head.set(vertices);

        fdef.shape = head;
        fdef.restitution = 1.8f; // nảy mạnh
        fdef.filter.categoryBits = Main.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        switch (currentState) {
            case STANDING_SHELL:
            case MOVING_SHELL:
                region = shell;
                break;
            case WALKING:
            default:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
        }

        if (velocity.x > 0 && !region.isFlipX()) {
            region.flip(true, false);
        } else if (velocity.x < 0 && region.isFlipX()) {
            region.flip(true, false);
        }

        stateTime = (currentState == previousState) ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }

    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));

        // Nếu shell đứng yên hơn 5s thì tự động đi tiếp
        if (currentState == State.STANDING_SHELL && stateTime > 5f) {
            currentState = State.WALKING;
            velocity.x = 1;
            System.out.println("Shell wakes up!");
        }

        b2body.setLinearVelocity(velocity);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 / Main.PPM);
    }

    @Override
    public void hitOnHead(Mario mario) {
        if (currentState == State.STANDING_SHELL) {
            // Đá shell
            if (mario.b2body.getPosition().x > b2body.getPosition().x)
                kick(KICK_LEFT);
            else
                kick(KICK_RIGHT);
        } else {
            // Biến thành shell
            currentState = State.STANDING_SHELL;
            velocity.x = 0;
        }
    }

    @Override
    public void hitByEnemy(Enemy enemy) {
        if (enemy instanceof Turtle) {
            // Nếu bị trúng turtle khác đang lăn -> chết
            Turtle turtle = (Turtle) enemy;
            if (turtle.currentState == State.MOVING_SHELL && currentState != State.MOVING_SHELL) {
                setToDestroy = true;
            } else {
                reverseVelocity(true, false);
            }
        } else if (currentState != State.MOVING_SHELL) {
            reverseVelocity(true, false);
        }
    }

    public void kick(int direction) {
        velocity.x = direction;
        currentState = State.MOVING_SHELL;
    }
}
