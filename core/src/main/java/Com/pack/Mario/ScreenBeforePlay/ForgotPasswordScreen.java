package Com.pack.Mario.ScreenBeforePlay;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ForgotPasswordScreen implements Screen {
    Game game;
    Stage stage;
    Skin skin;
    TextField emailField;
    TextButton submitButton;
    TextButton backButton;

    public ForgotPasswordScreen(Game game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        CreateTable();
        ClickSubmit();
        ClickBack();
    }

    public void CreateTable() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        emailField = new TextField("", skin);
        emailField.setMessageText("Email");
        submitButton = new TextButton("Submit", skin);
        backButton = new TextButton("Back", skin);

        Label title = new Label("Forgot Password", skin);
        title.setFontScale(1.5f);

        Label description = new Label("Enter your email address below and we'll send you a link to reset your password.", skin);
        description.setWrap(true);
        description.setAlignment(Align.center);

        table.add(title).colspan(2).padBottom(10).padTop(20);
        table.row();
        table.add(description).width(320).colspan(2).padBottom(20);
        table.row();
        table.add(emailField).colspan(2).padTop(20).padBottom(20).width(300);
        table.row();
        table.add(submitButton).colspan(2).padBottom(30).padTop(20).width(200);
        table.row();
        table.add(backButton).colspan(2).padBottom(30).padTop(20).width(200);
    }

    public void ClickSubmit() {
        submitButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (submitButton.isPressed()) {
                    // Handle submit logic here
                    System.out.println("Submit Clicked");
                }
                return false;
            }
        });
    }

    public void ClickBack() {
        backButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (backButton.isPressed()) {
                    game.setScreen(new LoginScreen(game));
                }
                return false;
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
