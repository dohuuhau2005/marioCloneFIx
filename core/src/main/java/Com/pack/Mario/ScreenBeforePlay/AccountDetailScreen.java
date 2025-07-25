package Com.pack.Mario.ScreenBeforePlay;

import Com.pack.Mario.Main;
import Com.pack.Mario.Model.User;
import Com.pack.Mario.Model.UserDao;
import Com.pack.Mario.Model.Validation;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class AccountDetailScreen implements Screen {

    private final Game game;
    private final Stage stage;
    private final Skin skin;
    User user;
    String email;
    Button ChangeProfileButton;
    Button ChangePasswordButton;
    Button SignOutButton;
    TextField passwordField;
    TextField ConfirmpasswordField;
    private Texture avatarTexture;
    private Texture backgroundTexture;
    private SpriteBatch batch;

    public AccountDetailScreen(Game game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.batch = new SpriteBatch();
        Preferences prefs = Gdx.app.getPreferences("UserSession");
        user = ((Main) game).getCurrentUser();
        email = prefs.getString("email", null);
        System.out.println(user.getEmail());
        System.out.println(user.getDobDay());

        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("ma2.jpg")); // Thêm ảnh nền

        buildUI();
        ClickButton();
    }

    private void buildUI() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // ===== HEADER =====
        Table header = new Table();
        header.setBackground(skin.newDrawable("white", new Color(0.2f, 0.4f, 0.8f, 1))); // blue bar
        header.pad(15);

        Label titleLabel = new Label("Account Details", skin);
        titleLabel.setFontScale(1.8f);
        titleLabel.setColor(Color.WHITE);
        header.add(titleLabel).expandX().left().padLeft(20);

        TextButton backButton = new TextButton("Back", skin);
        backButton.getLabel().setFontScale(1.2f);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));
            }
        });
        header.add(backButton).right().width(100).height(40).padRight(10);

        root.add(header).expandX().fillX();
        root.row();

        // ===== CONTENT =====
        Table content = new Table();
        content.pad(30);
        content.defaults().pad(20);

        Table infoPanel = new Table(skin);
        infoPanel.setBackground(skin.newDrawable("white", new Color(0.95f, 0.95f, 0.95f, 0.9f)));
        infoPanel.pad(25);
        infoPanel.defaults().pad(15).left();

        Label usernameLabel = new Label("Username: " + user.getUsername(), skin);
        Label DOBLabel = new Label("Date Of Birth: " + user.getDobDay() + " " + user.getDobMonth() + " " + user.getDobYear(), skin);
        Label rankingLabel = new Label("Ranking: #1234", skin);
        Label levelLabel = new Label("Level: 15", skin);////////////////thieu level
        ChangeProfileButton = new TextButton("Change Profile", skin);
        ChangePasswordButton = new TextButton("Change Password", skin);
        SignOutButton = new TextButton("Sign Out", skin);

        for (Label label : new Label[]{usernameLabel, rankingLabel, levelLabel, DOBLabel}) {
            label.setFontScale(1.7f);
            infoPanel.add(label).left().row();
        }

        // ===== AVATAR =====
        Image avatarImg;
        try {
            avatarTexture = new Texture(Gdx.files.internal("avatar.jpg"));
            avatarImg = new Image(avatarTexture);
            avatarImg.setSize(140, 180);
        } catch (Exception e) {
            avatarImg = new Image();
            avatarImg.setColor(Color.GRAY);
            avatarImg.setSize(140, 180);
        }

// ===== INFO PANEL =====
        infoPanel = new Table(skin);

// Bo góc nếu bạn có `white-rounded` hoặc `default-round` trong `uiskin.atlas`
// Nếu không có thì xài drawable trắng bình thường
        infoPanel.setBackground(skin.newDrawable("white", new Color(0.95f, 0.95f, 0.95f, 0.9f)));
// Có thể dùng ảnh radius thực tế nếu muốn đẹp hơn

        infoPanel.pad(25);
        infoPanel.defaults().pad(15).left();

        usernameLabel = new Label("Username: " + user.getUsername(), skin);
        DOBLabel = new Label("Date Of Birth: " + user.getDobDay() + " - " + user.getDobMonth() + " - " + user.getDobYear(), skin);
        rankingLabel = new Label("Ranking: #1234", skin);
        levelLabel = new Label("Level: 15", skin);

        for (Label label : new Label[]{usernameLabel, rankingLabel, levelLabel, DOBLabel}) {
            label.setFontScale(1.5f);
            infoPanel.add(label).left().row();
        }
        infoPanel.add(ChangeProfileButton).left();
        infoPanel.add(ChangePasswordButton).right().row();
//        infoPanel.add(SignOutButton).width(200).height(20).center().row();
// ===== Vertical Table chứa avatar trên, info dưới =====
        Table profileTable = new Table();
        profileTable.add(avatarImg).padBottom(30).width(100).height(120).row();
        profileTable.add(infoPanel).width(500).row(); // Width cố định cho đẹp
        profileTable.add(SignOutButton).width(500).height(50).row();
        root.add(profileTable).center().expand();

    }

    void makeDiallog() {

    }

    public void ClickButton() {
        ChangePasswordButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Tạo dialog nhập password
                final Dialog passwordDialog = new Dialog("Change Password", skin) {
                    @Override
                    protected void result(Object object) {
                        boolean confirmed = (Boolean) object;
                        if (confirmed) {
                            String newPassword = passwordField.getText();
                            String confirmPassword = ConfirmpasswordField.getText();
                            System.out.println("New password: " + newPassword);
                            if (newPassword.equals(confirmPassword)) {
                                String error = new Validation().CheckPassword(newPassword);
                                if (error == null) {
                                    new UserDao().ChangePassword(email, newPassword);
                                } else {
                                    Dialog errorDialog = new Dialog("Password Error", skin);
                                    errorDialog.text(error);
                                    errorDialog.button("ok");
                                    errorDialog.center();
                                    errorDialog.setVisible(true);
                                    errorDialog.show(stage);
                                }

                            } else {
                                Dialog errorDialog = new Dialog("Password Error", skin);
                                errorDialog.text("Passwords does not match");
                                errorDialog.button("ok");
                                errorDialog.center();
                                errorDialog.setVisible(true);
                                errorDialog.show(stage);
                            }
                        }
                    }
                };

                passwordDialog.pad(20);
                passwordDialog.getContentTable().defaults().pad(10);

                // Tạo textfield nhập password
                passwordField = new TextField("", skin);
                passwordField.setMessageText("Enter new password");
                passwordField.setPasswordCharacter('*');
                passwordField.setPasswordMode(true);

                ConfirmpasswordField = new TextField("Confirm new password", skin);
                ConfirmpasswordField.setPasswordCharacter('*');
                ConfirmpasswordField.setPasswordMode(true);

                passwordDialog.text("Please enter your new password:");
                passwordDialog.getContentTable().add(passwordField).width(300).row();
                passwordDialog.getContentTable().add(ConfirmpasswordField).width(300).row();

                passwordDialog.button("Confirm", true);
                passwordDialog.button("Cancel", false);

                passwordDialog.show(stage); // Hiển thị trên stage chính
            }
        });
    }


    @Override
    public void show() {
        InputAdapter escListener = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    game.setScreen(new HomeScreen(game));
                }
                return false;
            }
        };
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, escListener));
    }

    @Override
    public void render(float delta) {
        // ===== CLEAR SCREEN =====
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // ===== DRAW BACKGROUND =====
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // ===== DRAW UI =====
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
        skin.dispose();
        batch.dispose();
        if (avatarTexture != null) avatarTexture.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
    }
}

