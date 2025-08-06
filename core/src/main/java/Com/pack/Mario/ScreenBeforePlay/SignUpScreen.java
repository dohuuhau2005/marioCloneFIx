package Com.pack.Mario.ScreenBeforePlay;

import Com.pack.Mario.Model.User;
import Com.pack.Mario.Model.UserDao;
import Com.pack.Mario.Model.Validation;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.List;


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
    SelectBox<String> DayBox;
    SelectBox<String> MonthBox;
    SelectBox<String> YearBox;
    User user;

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
        Texture backgroundTexture = new Texture(Gdx.files.internal("signup.jpg"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true); // Cho phủ toàn màn hình

        stage.addActor(backgroundImage); //  Add đầu tiên để nằm dưới cùng

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
        //data  time
        DayBox = new SelectBox<>(skin);
        MonthBox = new SelectBox<>(skin);
        YearBox = new SelectBox<>(skin);
        String[] days = new String[32];
        for (int i = 0; i <= 31; i++) {
            days[i] = String.valueOf(i);
        }
        DayBox.setItems(days);

        String[] months = new String[13];
        for (int i = 0; i <= 12; i++) {
            months[i] = String.valueOf(i);
        }
        MonthBox.setItems(months);

        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        String[] years = new String[101];
        for (int i = 0; i <= 100; i++) {
            years[i] = String.valueOf(currentYear - 100 + i);
        }
        YearBox.setItems(years);
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
        Label dobLabel = new Label("Date of Birth:", skin);

        table.row().padTop(20);
        table.add(dobLabel).colspan(3).left().padBottom(10);

        Table dobTable = new Table();
        dobTable.add(DayBox).padRight(10).width(80);
        dobTable.add(MonthBox).padRight(10).width(100);
        dobTable.add(YearBox).width(120);

        table.row();
        table.add(dobTable).colspan(3).center();
        table.row();
        signUpButton.getLabel().setAlignment(Align.center);
        table.add(signUpButton).colspan(3).padBottom(30).padTop(20).width(300);
        table.row();
        loginButton.getLabel().setAlignment(Align.center);
        table.add(loginButton).colspan(3).padTop(10).width(300);
    }

    public void ClickSignUp() {
        signUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (passwordField.getText().equals(confirmPasswordField.getText())) {
                    user = new User();
                    user.setUsername(usernameField.getText());
                    user.setEmail(emailField.getText());
                    user.setPassword(passwordField.getText());
                    user.setDobDay(Integer.parseInt(DayBox.getSelected()));
                    user.setDobMonth(Integer.parseInt(MonthBox.getSelected()));
                    user.setDobYear(Integer.parseInt(YearBox.getSelected()));

                    List<String> errors = new Validation().Validate(user);

                    if (errors == null || errors.isEmpty()) {
                        if (new Validation().ExistUser(user)) {
                            Dialog errorDialog = new Dialog("Sign Up Error", skin);
                            errorDialog.text("Email or Username already exists");
                            errorDialog.button("OK", true); // Thêm nút OK


                            errorDialog.show(stage);
                        } else {
                            UserDao userDao = new UserDao();
                            userDao.Insert(user);

                            Dialog successDialog = new Dialog("Success", skin);
                            successDialog.text("Sign up successful!");
                            successDialog.button("OK", true);
                            successDialog.show(stage);
                        }

                    } else {
                        StringBuilder message = new StringBuilder();
                        for (String error : errors) {
                            message.append("- ").append(error).append("\n");
                        }

                        Dialog errorDialog = new Dialog("Sign Up Error", skin);
                        errorDialog.text(message.toString());
                        errorDialog.button("OK", true); // Thêm nút OK

                        // Khi nhấn OK thì gán errors = null
                        errorDialog.getButtonTable().getCells().first().getActor().addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {

                                errors.clear();
                                System.out.println("Đã bấm OK và reset lỗi");
                            }
                        });

                        errorDialog.show(stage);
                    }
                } else {
                    Dialog errorDialog = new Dialog("Sign Up Error", skin);
                    errorDialog.text("Passwords and ConfirmPassword do not match");
                    errorDialog.button("OK", true);
                    errorDialog.show(stage);
                }
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

        new Exit(skin).PressExit(stage);

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
