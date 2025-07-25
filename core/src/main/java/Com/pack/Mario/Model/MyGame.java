package Com.pack.Mario.Model;

import Com.pack.Mario.ScreenBeforePlay.LoginScreen;
import com.badlogic.gdx.Game;

public class MyGame extends Game {
    public User CurrentUser;

    public User getCurrentUser() {
        return this.CurrentUser;
    }

    public void setCurrentUser(User u) {
        this.CurrentUser = u;
    }

    @Override
    public void create() {
        this.setScreen(new LoginScreen(this));

    }
}
