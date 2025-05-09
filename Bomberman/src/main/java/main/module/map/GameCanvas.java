package main.module.map;

import main.module.map.Objects.OBJ_Bomb;
import main.module.map.SetPosition.AssetSetter;
import main.module.map.Utils.MapManager;
import main.module.map.collision.CollisionChecker;
import main.module.map.ui.UI;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.module.map.entities.Entity;
import main.module.map.entities.Player;
import main.module.map.tile.TileManager;

import java.util.*;

public class GameCanvas extends Canvas {
    //Screen settings
    static final int originalTilePixels = 16;
    static final int scale = 2;
    public final int tileSize = originalTilePixels * scale;
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 20;
    public static final int screenWidth = 640;
    public static final int screenHeight = 640;

    private AnimationTimer gameTimer;
    private final GraphicsContext gc;

    //Character positions
//    private double x = 100;
//    private double y = 100;
//    private final int speed = 8;

    //World Setting
    public int maxWorldCol = 30;
    public int maxWorldRow = 30;
    public int worldWidth = tileSize * maxWorldCol;
    public int worldHeight = tileSize * maxWorldRow;

    //Class implement
    public KeyHandler keyController = new KeyHandler(this);

    public TileManager tileM = new TileManager(this);

    public AssetSetter aSetter = new AssetSetter(this);

    public CollisionChecker cChecker = new CollisionChecker(this);

    public UI ui = new UI(this);

    private final MapManager mapManager;

    //Entity And Object
    public Player player;
    public Entity[] obj = new Entity[8];
    public Entity[] monsters = new Entity[50];
    public List<OBJ_Bomb> bombs = new ArrayList<>();
    ArrayList<Entity> entities = new ArrayList<>();
    public Set<String> usedPositions;

    //GAME STATE
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int gameOverState = 3;

    //Map tracking.
//    private String[] maps = {"/Maps/Map_01.txt", "/Maps/Map_02.txt", "/Maps/Map_03.txt"};
//    private int currentMapIndex = 0;


    /**
     * Constructor.
     */
    public GameCanvas() {
        super(screenWidth, screenHeight);
        this.setFocusTraversable(true);
        gc = this.getGraphicsContext2D();

        player = new Player(this, keyController);
        mapManager = new MapManager(this, cChecker, tileM, player);

        drawMap();
    }

    public void setUpGame() {
        aSetter.setObject();
        aSetter.setNPC();
        gameState = playState;
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

        if (gameState == playState) {
            player.update();

            for (int i = 0; i < monsters.length; i++) {
                if (monsters[i] != null) {
                    monsters[i].update();
                    if (monsters[i].life <= 0) {
                        monsters[i] = null;
                    }
                }
            }

            for (int i = 0; i < bombs.size(); i++) {
                OBJ_Bomb bomb = bombs.get(i);
                if (bomb != null) {
                    bomb.update();
                    if (bomb.isExploded() &&
                            bomb.getExplodeCounter() >= bomb.getExplodeTime() + 10) {
                        bombs.remove(i);
                        i--;
                    }
                }
            }

            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].update();
                }
            }
        }


        mapManager.checkSwitchMap();
    }

    public void draw() {
        drawMap();
        drawCharacter();
    }
    public void drawMap() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, screenWidth, screenHeight);

        tileM.draw(gc);

    }

    public void drawCharacter() {
//        player.draw(gc);

        entities.add(player);
        for (int i = 0; i < monsters.length; i++) {
            if (monsters[i] != null) {
                entities.add(monsters[i]);
            }
        }

        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                entities.add(obj[i]);
//                System.out.println("Added object: " + obj[i].name + " at (" + obj[i].worldX + ", " + obj[i].worldY + ")");
            }
        }

        Collections.sort(entities, new Comparator<Entity>() {
            @Override
            public int compare(Entity e1, Entity e2) {
                int result = Integer.compare(e1.worldY, e2.worldY);
                return result;
            }
        });

        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).draw(gc);
        }

        for (OBJ_Bomb bomb : bombs) {
            if (bomb != null) {
                bomb.draw(gc);
            }
        }

//        for (int i = 0; i < entities.size(); i++) {
//            entities.remove(i);
//        }
        entities.clear();
        ui.draw(gc);
    }
}

//        for(int i = 0; i < obj.length; i++) {
//            if (obj[i] != null) {
//                obj[i].draw(gc);
//            }
//        }
