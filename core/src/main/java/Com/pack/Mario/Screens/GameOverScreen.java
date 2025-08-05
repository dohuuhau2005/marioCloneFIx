package Com.pack.Mario.Screens;

import Com.pack.Mario.Main;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Màn hình hiện ra khi Game Over
 * Gốc: by brentaureli on 10/8/15 — Đã dịch chú thích sang tiếng Việt
 */
public class GameOverScreen implements Screen {
    private final Viewport viewport;
    private final Stage stage;

    private final Game game;
    private final String mapFile; // ✅ Tên map để load lại đúng Level

    public GameOverScreen(Game game, String mapFile) {
        this.game = game;
        this.mapFile = mapFile;

        // Viewport cố định theo tỉ lệ chuẩn
        viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((Main) game).batch);

        // Kiểu chữ cho Label
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        // Tạo Table chứa các thành phần
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("GAME OVER", font);
        Label playAgainLabel = new Label("Click to Play Again", font);

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
        // Nếu click bất kỳ chỗ nào -> chơi lại
        if (Gdx.input.justTouched()) {
            game.setScreen(new PlayScreen((Main) game, mapFile)); // ✅ Truyền map cũ vào
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
