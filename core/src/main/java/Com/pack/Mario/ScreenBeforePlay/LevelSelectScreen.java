package Com.pack.Mario.ScreenBeforePlay;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelSelectScreen implements Screen {

    private final Game game;
    private final Stage stage;
    private final Skin skin;
    private Texture backgroundTexture;

    public LevelSelectScreen(Game game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("ma4.jpg")); // Đặt background ma4

        buildUI();
    }

    private void buildUI() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // ===== HEADER =====
        Table header = new Table();
        header.setBackground(skin.newDrawable("white", new Color(0.2f, 0.4f, 0.8f, 0.9f)));
        header.pad(15);

        Label title = new Label("Level", skin);
        title.setFontScale(1.8f);
        title.setColor(Color.BLACK);
        header.add(title).expandX().left().padLeft(20);

        TextButton closeBtn = new TextButton("X", skin);
        closeBtn.getLabel().setFontScale(1.2f);
        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));
            }
        });
        header.add(closeBtn).right().width(50);

        root.add(header).fillX();
        root.row();

        // ===== LEVEL BUTTONS =====
        Table levelTable = new Table();
        levelTable.defaults().pad(40).width(150).height(70);

        for (int i = 1; i <= 6; i++) {
            TextButton levelBtn = new TextButton(String.valueOf(i), skin);
            levelBtn.getLabel().setFontScale(1.5f);
            levelBtn.getStyle().fontColor = Color.BLACK;
            levelBtn.getStyle().up = skin.newDrawable("white", Color.GREEN);

            final int level = i;
            levelBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Selected level: " + level);
                }
            });

            levelTable.add(levelBtn).expand().uniform(); // uniform để các nút bằng nhau
            if (i % 2 == 0) levelTable.row(); // 2 cột
        }

        root.add(levelTable).expand().center();
    }

    @Override
    public void show() {
        new Exit(skin).PressExit(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Vẽ background
        stage.getBatch().begin();
        stage.getBatch().draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();

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
        if (backgroundTexture != null) backgroundTexture.dispose();
    }
}
