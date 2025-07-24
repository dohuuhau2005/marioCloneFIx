package Com.pack.Mario.ScreenBeforePlay;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class RankingScreen implements Screen {

    private final Game game;
    private final Stage stage;
    private final Skin skin;

    private Texture backgroundTexture;

    public RankingScreen(Game game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("ma3.jpg"));

        buildUI();
    }

    private void buildUI() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // ===== HEADER =====
//        Table header = new Table();
//        header.setBackground(skin.newDrawable("white", new Color(0.2f, 0.4f, 0.8f, 0.9f)));
//        header.pad(15);
//
//        Label title = new Label("Ranking", skin);
//        title.setFontScale(1.8f);
//        title.setColor(Color.BLACK);
//        header.add(title).expandX().left().padLeft(20);
//
//        TextButton closeBtn = new TextButton("X", skin);
//        closeBtn.getLabel().setFontScale(1.2f);
//        closeBtn.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                game.setScreen(new HomeScreen(game));
//            }
//        });
//        header.add(closeBtn).right().width(50);
//
//        root.add(header).fillX();
//        root.row();

        // ===== 4 RANKING ROWS =====
        Table rankingTable = new Table();
        rankingTable.padTop(30); // Padding top để tách header

        addPlayerRow(rankingTable, "NO.1", "Hào Chiến Tướng", "p1.jpg", Color.RED, 1.3f);
        addPlayerRow(rankingTable, "NO.2", "Bảo Vô Song", "p2.jpg", Color.BLACK, 1.1f);
        addPlayerRow(rankingTable, "NO.3", "Hậu DamDang", "p3.jpg", Color.BLACK, 1.1f);
        addPlayerRow(rankingTable, "NO.4", "Phụng No.1", "p4.jpg", Color.BLACK, 1.1f);

        // Không cần scroll vì chỉ có 4 người
        root.add(rankingTable).expand().top().padTop(20);
    }

    private void addPlayerRow(Table table, String rank, String name, String imgPath, Color nameColor, float fontScale) {
        Table row = new Table();
        row.setBackground(skin.newDrawable("white", new Color(1, 1, 1, 0.7f))); // nền mờ 70%
        row.pad(20);

        Label rankLabel = new Label(rank, skin);
        rankLabel.setFontScale(fontScale);
        rankLabel.setColor(nameColor);

        Texture avatarTexture;
        try {
            avatarTexture = new Texture(Gdx.files.internal(imgPath));
        } catch (Exception e) {
            avatarTexture = new Texture(Gdx.files.internal("ma2.jpg"));
        }

        Image avatarImg = new Image(avatarTexture);
        avatarImg.setSize(60, 60);

        Label nameLabel = new Label(name, skin);
        nameLabel.setFontScale(fontScale);
        nameLabel.setColor(nameColor);

        row.add(rankLabel).padRight(20);
        row.add(avatarImg).size(60).padRight(20);
        row.add(nameLabel).expandX().left();

        // Add row với khoảng cách lớn giữa các row
        table.add(row).expandX().fillX().padBottom(30).row();
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
