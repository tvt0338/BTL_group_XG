package Main_Modules.map01;

import Main_Modules.KeyHandler;
import Main_Modules.collisions.CollisionChecker;
import Main_Modules.entities.Player;
import Main_Modules.tile.TileManager;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameCanvas extends Canvas {
    //Screen settings
    static final int originalTilePixels = 16;
    static final int scale = 4;
    public static final int tileSize = originalTilePixels * scale;
    public static final int maxScreenCol = 16;
    public static final int maxScreenRow = 12;
    public static final int screenWidth = tileSize * maxScreenCol;
    public static final int screenHeight = tileSize * maxScreenRow;

    private AnimationTimer gameTimer;
    private GraphicsContext gc;

    //Character positions
    private double x = 100;
    private double y = 100;
    private final int speed = 8;

    //World Setting
    public final int maxWorldCol = 60;
    public final int maxWorldRow = 30;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //Class implement
    public KeyHandler keyController;

    public Player player;

    public TileManager tileM = new TileManager(this);

    public CollisionChecker cChecker = new CollisionChecker(this);


    /**
     * Constructor.
     */
    public GameCanvas() {
        super(screenWidth, screenHeight);
        this.setFocusTraversable(true);
        gc = this.getGraphicsContext2D();

        keyController = new KeyHandler(this);
        player = new Player(this, keyController);

        drawMap();
    }

    public void startGame() {
        gameTimer = new AnimationTimer() {
            long lastTime = System.nanoTime();
            int frameCount = 0;

            @Override
            public void handle(long l) {
                update();
                draw();

                //Print FPS
                frameCount++;
                if (l - lastTime >= 1000000000) {
                    System.out.println("FPS: " + frameCount);
                    frameCount = 0;
                    lastTime = l;
                }
            }
        };
        gameTimer.start();
    }

    public void update() {
        player.update();
    }

    public void draw() {
        drawMap();
        drawCharacter();
    }
    public void drawMap() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, screenWidth, screenHeight);

        tileM.draw(gc);
    }

    public void drawCharacter() {
        player.draw(gc);
    }
}
