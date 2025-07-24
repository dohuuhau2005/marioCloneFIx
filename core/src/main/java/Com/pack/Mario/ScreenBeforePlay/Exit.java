package Com.pack.Mario.ScreenBeforePlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Exit {
    Skin skin;

    public Exit(Skin skin) {
        this.skin = skin;
    }

    public void PressExit(Stage stage) {
        //input processor just have 1 threat --> multi processor
        InputAdapter EscClickExit = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    showDialog(stage);
                }
                return false;
            }

        };
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(EscClickExit);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void showDialog(Stage stage) {
        Dialog dialog = new Dialog("Exit ?", new Skin(Gdx.files.internal("uiskin.json"))) {
            @Override
            public void result(Object object) {
                boolean yes = (boolean) object;
                if (yes) {
                    Gdx.app.exit();
                } else {
                    this.hide();
                }
            }
        };

        dialog.text("Exit ?");
        dialog.button("Yes", true);
        dialog.button("No", false);
        dialog.show(stage);
    }
}
