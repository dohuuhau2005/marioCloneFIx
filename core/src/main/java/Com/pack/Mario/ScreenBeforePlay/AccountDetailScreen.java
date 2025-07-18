package Com.pack.Mario.ScreenBeforePlay;

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
    private Texture avatarTexture;
    private Texture backgroundTexture;
    private SpriteBatch batch;

    public AccountDetailScreen(Game game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("ma2.jpg")); // Thêm ảnh nền

        buildUI();
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

        Label usernameLabel = new Label("Username: MarioFan123", skin);
        Label rankingLabel = new Label("Ranking: #1234", skin);
        Label levelLabel = new Label("Level: 15", skin);

        for (Label label : new Label[]{usernameLabel, rankingLabel, levelLabel}) {
            label.setFontScale(1.3f);
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

        Table centerTable = new Table();
        centerTable.add(infoPanel).padRight(60);
        centerTable.add(avatarImg).width(140).height(180);

        root.add(centerTable).expand().center();
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

