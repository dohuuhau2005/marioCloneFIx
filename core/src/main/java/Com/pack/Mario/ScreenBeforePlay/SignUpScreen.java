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


public class SignUpScreen implements Screen {
    Game game;
    Stage stage;
    Skin skin;
    TextField usernameField;
    TextField emailField;
    TextField passwordField;
    TextField confirmPasswordField;
    TextButton signUpButton;
    TextButton loginButton;

    public SignUpScreen(Game game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        CreateTable();
        ClickSignUp();
        ClickLogin();
    }

    public void CreateTable() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");
        emailField = new TextField("", skin);
        emailField.setMessageText("Email");
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMessageText("Password");
        confirmPasswordField = new TextField("", skin);
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');
        confirmPasswordField.setMessageText("Confirm Password");
        signUpButton = new TextButton("Sign Up", skin);
        loginButton = new TextButton("Already have an account? Login", skin);

        Label title = new Label("Sign Up", skin);
        title.setFontScale(1.5f);

        table.add(title).colspan(2).padBottom(30).padTop(20);
        table.row();
        table.add(usernameField).colspan(3).padTop(20).padBottom(20).width(300);
        table.row();
        table.add(emailField).colspan(3).padTop(20).padBottom(20).width(300);
        table.row();
        table.add(passwordField).colspan(3).padTop(20).padBottom(20).width(300);
        table.row();
        table.add(confirmPasswordField).colspan(3).padTop(20).padBottom(20).width(300);
        table.row();
        signUpButton.getLabel().setAlignment(Align.center);
        table.add(signUpButton).colspan(3).padBottom(30).padTop(20).width(300);
        table.row();
        loginButton.getLabel().setAlignment(Align.center);
        table.add(loginButton).colspan(3).padTop(10).width(300);
    }

    public void ClickSignUp() {
        signUpButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (signUpButton.isPressed()) {
                    // Handle sign up logic here
                    System.out.println("Sign Up Clicked");
                }
                return false;
            }
        });
    }

    public void ClickLogin() {
        loginButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (loginButton.isPressed()) {
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
