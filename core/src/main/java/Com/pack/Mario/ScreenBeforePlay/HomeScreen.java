package Com.pack.Mario.ScreenBeforePlay;

import Com.pack.Mario.Model.User;
import Com.pack.Mario.Model.UserDao;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class HomeScreen implements Screen {
    Game game;
    User user;
    String email;
    private Stage stage;
    private Texture marioTexture, logoTexture;
    private Image marioImage, logoImage;
    private Skin skin;

    public HomeScreen(Game game) {
        this.game = game;
        this.stage = new Stage();
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        Preferences prefs = Gdx.app.getPreferences("UserSession");
        email = prefs.getString("email", null);
        if (email != null) {
            this.user = new UserDao().GetUser(email);

            System.out.println("helloooooooooooooooo " + email);
        } else {
            System.out.println("nooo chx dang nhap kia");
        }
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {


        new Exit(skin).PressExit(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json")); // Dùng skin mặc định

        // Load ảnh
        marioTexture = new Texture(Gdx.files.internal("mario.jpg"));
        logoTexture = new Texture(Gdx.files.internal("mario.jpg"));

        marioImage = new Image(marioTexture);
        logoImage = new Image(logoTexture);

        // Label tên người chơi
        Label nameLabel = new Label("Hello " + user.getUsername(), skin);

        // Các nút
        TextButton renameButton = new TextButton("Detail Profile", skin);
        TextButton playButton = new TextButton("Play", skin);
        TextButton historyButton = new TextButton("Rank", skin);

        // Gắn sự kiện (có thể xử lý sau)
        playButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // Ví dụ chuyển sang màn chơi
                System.out.println("Bắt đầu chơi...");
            }
        });

        renameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Đổi tên...");
            }
        });

        // Dàn layout bằng Table
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Thêm vào layout
        table.add(marioImage).size(200).padBottom(20).row();
        table.add(nameLabel).padBottom(10).row();
        table.add(renameButton).padBottom(15).row();
        table.add(logoImage).size(250, 100).padBottom(10).row();
        table.add(playButton).size(150, 50).padBottom(10).row();
        table.add(historyButton).size(150, 50).padBottom(10).row();

        // Add vào stage
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.5f, 1, 1); // Màu nền xanh
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
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
        marioTexture.dispose();
        logoTexture.dispose();
    }
}
