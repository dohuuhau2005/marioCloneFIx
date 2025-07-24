package Com.pack.Mario.ScreenBeforePlay;

import Com.pack.Mario.Model.UserDao;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class LoginScreen implements Screen {
    Game game;
    Stage stage;//giao dien + actor + input
    TextButton loginButton;
    Skin skin;
    TextField UsernameField;
    TextField PasswordField;
    TextButton forgotPasswordButton;
    TextButton createAccountButton;

    public LoginScreen(Game game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        CreateTable();
        ClickLogin();
        ClickForgotPassword();
        ClickCreateAccount();
    }

    //tao table
    public void CreateTable() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        UsernameField = new TextField("", skin);
        UsernameField.setMessageText("Email");
        PasswordField = new TextField("", skin);
        PasswordField.setPasswordMode(true);
        PasswordField.setPasswordCharacter('*');
        PasswordField.setMessageText("Password");
        loginButton = new TextButton("login", skin);
        forgotPasswordButton = new TextButton("Forgot Password?", skin);
        createAccountButton = new TextButton("Create an account", skin);
        Label title = new Label("Login", skin);
        title.setFontScale(1.5f);
        table.add(title).colspan(2).padBottom(30).padTop(20);
        table.row();
        table.add(UsernameField).colspan(3).padTop(20).padBottom(20).width(300);
        table.row();
        table.add(PasswordField).colspan(3).padTop(20).padBottom(10).width(300);
        table.row();
        Table linksTable = new Table();
        linksTable.add(forgotPasswordButton).left();
        linksTable.add(createAccountButton).right().expandX();

        table.add(linksTable).colspan(3).width(300).padBottom(10);
        table.row();
        table.add(loginButton).colspan(3).padBottom(30).padTop(10).width(300);
    }

    public void ClickLogin() {
        loginButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (loginButton.isPressed()) {
                    String email = UsernameField.getText();
                    String password = PasswordField.getText();
//                    if (username.equals("admin") && password.equals("admin")) {
//                        System.out.println("Login Successful");
//                        game.setScreen(new HomeScreen(game));
//
//                    }
                    if (new UserDao().Login(email, password)) {
                        System.out.println("Login Successful");
                        Preferences prefs = Gdx.app.getPreferences("UserSession");

                        prefs.putString("email", email);
                        prefs.flush();
                        game.setScreen(new HomeScreen(game));

                    } else {
                        System.out.println("Login Failed");
                        Dialog errorDialog = new Dialog("Log In Error", skin);
                        errorDialog.text("Email or Password is wrong");
                        errorDialog.button("OK", true); // Thêm nút OK


                        errorDialog.show(stage);
                    }
                }
                return false;
            }
        });
    }

    public void ClickForgotPassword() {
        forgotPasswordButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (forgotPasswordButton.isPressed()) {
                    game.setScreen(new ForgotPasswordScreen(game));
                }
                return false;
            }
        });
    }

    public void ClickCreateAccount() {
        createAccountButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (createAccountButton.isPressed()) {
                    game.setScreen(new SignUpScreen(game));
                }
                return false;
            }
        });
    }

    @Override
    public void show() {

        InputAdapter EscClickExit = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    showDialog();
                }
                return false;
            }

        };
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(EscClickExit);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void showDialog() {
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

