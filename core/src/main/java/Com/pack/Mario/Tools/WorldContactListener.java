package Com.pack.Mario.Tools;

import Com.pack.Mario.Main;
import Com.pack.Mario.Sprites.Enemies.Enemy;
import Com.pack.Mario.Sprites.Items.Item;
import Com.pack.Mario.Sprites.Mario;
import Com.pack.Mario.Sprites.Other.FireBall;
import Com.pack.Mario.Sprites.TileObjects.InteractiveTileObject;
import com.badlogic.gdx.physics.box2d.*;


//import com.brentaureli.mariobros.Sprites.TileObjects.InteractiveTileObject;

/**
 * Created by brentaureli on 9/4/15.
 */
public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();


        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case Main.MARIO_HEAD_BIT | Main.BRICK_BIT:
            case Main.MARIO_HEAD_BIT | Main.COIN_BIT:
                if (fixA.getFilterData().categoryBits == Main.MARIO_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                break;
            case Main.ENEMY_HEAD_BIT | Main.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == Main.ENEMY_HEAD_BIT)
                    ((Enemy) fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
                else
                    ((Enemy) fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());
                break;
            case Main.ENEMY_BIT | Main.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == Main.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case Main.MARIO_BIT | Main.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == Main.MARIO_BIT)
                    ((Mario) fixA.getUserData()).hit((Enemy) fixB.getUserData());
                else
                    ((Mario) fixB.getUserData()).hit((Enemy) fixA.getUserData());
                break;
            case Main.ENEMY_BIT | Main.ENEMY_BIT:
                ((Enemy) fixA.getUserData()).hitByEnemy((Enemy) fixB.getUserData());
                ((Enemy) fixB.getUserData()).hitByEnemy((Enemy) fixA.getUserData());
                break;
            case Main.ITEM_BIT | Main.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == Main.ITEM_BIT)
                    ((Item) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case Main.ITEM_BIT | Main.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == Main.ITEM_BIT)
                    ((Item) fixA.getUserData()).use((Mario) fixB.getUserData());
                else
                    ((Item) fixB.getUserData()).use((Mario) fixA.getUserData());
                break;
            case Main.FIREBALL_BIT | Main.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == Main.FIREBALL_BIT)
                    ((FireBall) fixA.getUserData()).setToDestroy();
                else
                    ((FireBall) fixB.getUserData()).setToDestroy();
                break;
            case Main.FIREBALL_BIT | Main.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == Main.FIREBALL_BIT)
                    ((FireBall) fixA.getUserData()).hit((Enemy) fixB.getUserData());
                else
                    ((FireBall) fixB.getUserData()).hit((Enemy) fixA.getUserData());
                break;
            case Main.MARIO_BIT | Main.END_BIT:
                if (fixA.getFilterData().categoryBits == Main.MARIO_BIT)
                    ((Mario) fixA.getUserData()).setWon(true);
                else
                    ((Mario) fixB.getUserData()).setWon(true);
                break;
        }
        if ((fixA.getUserData() != null && fixA.getUserData().equals("end")) ||
            (fixB.getUserData() != null && fixB.getUserData().equals("end"))) {

            // Kiểm tra va chạm với Mario
            if (fixA.getUserData() instanceof Mario || fixB.getUserData() instanceof Mario) {
                Mario mario = (Mario) (fixA.getUserData() instanceof Mario ? fixA.getUserData() : fixB.getUserData());

                if (!mario.isWon()) {
                    mario.setWon(true); // đánh dấu đã thắng
                    mario.onReachEnd(); // gọi qua màn hoặc win
                }
            }
        }

    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
