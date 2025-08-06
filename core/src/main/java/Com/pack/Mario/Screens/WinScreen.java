package Com.pack.Mario.Screens;

import Com.pack.Mario.Main;
import Com.pack.Mario.ScreenBeforePlay.LevelSelectScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Màn hình hiện ra khi Game Over
 * Gốc: by brentaureli on 10/8/15 — Đã dịch chú thích sang tiếng Việt
 */
public class WinScreen implements Screen {
    private final Viewport viewport;
    private final Stage stage;

    private final Game game;

    public WinScreen(Game game) {
        this.game = game;


        // Viewport cố định theo tỉ lệ chuẩn
        viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((Main) game).batch);

        // Tạo hình nền
        Texture bgTexture = new Texture(Gdx.files.internal("win.png"));
        Image background2 = new Image(new TextureRegionDrawable(new TextureRegion(bgTexture)));
        background2.setFillParent(true);
        background2.getColor().a = 0.5f;

// Thêm background trước table để nó nằm dưới
        stage.addActor(background2);

        // Kiểu chữ cho Label
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        // Tạo Table chứa các thành phần
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("You Won !!!!", font);
        Label playAgainLabel = new Label("Click to Continue", font);

        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);

        stage.addActor(table);
    }

    @Override
    public void show() {
        // Không cần thêm gì ở đây
    }

    @Override
    public void render(float delta) {
        // Nếu click bất kỳ chỗ nào -> select man
        if (Gdx.input.justTouched()) {
            game.setScreen(new LevelSelectScreen(game)); // ✅ Truyền map cũ vào
            dispose();
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Không cần xử lý ở đây
    }

    @Override
    public void pause() {
        // Không cần xử lý ở đây
    }

    @Override
    public void resume() {
        // Không cần xử lý ở đây
    }

    @Override
    public void hide() {
        // Không cần xử lý ở đây
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
