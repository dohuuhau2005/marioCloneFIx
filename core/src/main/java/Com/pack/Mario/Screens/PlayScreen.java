package Com.pack.Mario.Screens;

import Com.pack.Mario.Main;
import Com.pack.Mario.Model.UserDao;
import Com.pack.Mario.Scenes.Hud;
import Com.pack.Mario.Sprites.Enemies.Enemy;
import Com.pack.Mario.Sprites.Items.Item;
import Com.pack.Mario.Sprites.Items.ItemDef;
import Com.pack.Mario.Sprites.Items.Mushroom;
import Com.pack.Mario.Sprites.Mario;
import Com.pack.Mario.Tools.B2WorldCreator;
import Com.pack.Mario.Tools.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.LinkedBlockingQueue;

import static Com.pack.Mario.Sprites.Mario.State.DEAD;

public class PlayScreen implements Screen {
    public static boolean alreadyDestroyed = false;

    private final Main game;
    private final TextureAtlas atlas;
    private final OrthographicCamera gamecam;
    private final Viewport gamePort;
    private final Hud hud;

    private final TmxMapLoader maploader;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    private final World world;
    private final Box2DDebugRenderer b2dr;
    private final B2WorldCreator creator;

    private final Mario player;
    private final Music music;

    private final Array<Item> items;
    private final LinkedBlockingQueue<ItemDef> itemsToSpawn;

    private final String mapFile;
    private final String email;

    public PlayScreen(Main game, String mapFile) {
        this.game = game;
        this.mapFile = mapFile;
        this.atlas = new TextureAtlas("Mario_and_Enemies.pack");
        System.out.println("Map file: " + mapFile);
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM, gamecam);
        hud = new Hud(game.batch);

        maploader = new TmxMapLoader();
        map = maploader.load(mapFile); // Load bản đồ từ tham số
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Main.PPM);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);


        String numberStr = mapFile.replaceAll("\\D+", ""); // Bỏ hết ký tự không phải số
        int levelSelected = Integer.parseInt(numberStr); // Chuyển thành số nguyên
        System.out.println("Level: " + levelSelected); // Output: 3


        player = new Mario(this, game, levelSelected);


        // Lấy email người dùng từ Preferences
        Preferences prefs = Gdx.app.getPreferences("UserSession");
        email = prefs.getString("email", "");

        world.setContactListener(new WorldContactListener());

        music = Main.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
    }

    public void spawnItem(ItemDef idef) {
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            ItemDef idef = itemsToSpawn.poll();
            if (idef.type == Mushroom.class) {
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {
    }

    public void handleInput(float dt) {
        if (player.currentState != DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) player.jump();
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.2f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.2f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) player.fire();
        }
    }

    public void update(float dt) {
        handleInput(dt);
        handleSpawningItems();

        world.step(1 / 60f, 6, 2);
        player.update(dt);

        for (Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224 / Main.PPM) {
                enemy.b2body.setActive(true);
            }
        }

        for (Item item : items)
            item.update(dt);

        hud.update(dt);

        if (player.currentState != DEAD) {
            gamecam.position.x = player.b2body.getPosition().x;
        }

        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : creator.getEnemies())
            enemy.draw(game.batch);
        for (Item item : items)
            item.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (gameOver()) {
            int finalScore = Hud.getScore();
            System.out.println("Your Score: " + finalScore);
            new UserDao().UpdateScore(email, finalScore);
            game.setScreen(new GameOverScreen(game, mapFile)); // vẫn truyền mapFile
            dispose();
        }
    }

    public boolean gameOver() {
        return player.currentState == DEAD && player.getStateTimer() > 3;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    public Hud getHud() {
        return hud;
    }
}
