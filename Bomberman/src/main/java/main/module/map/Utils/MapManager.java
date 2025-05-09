package main.module.map.Utils;

import main.module.map.GameCanvas;
import main.module.map.collision.CollisionChecker;
import main.module.map.entities.Player;
import main.module.map.tile.TileManager;

public class MapManager {
    private GameCanvas gc;
    private TileManager tileM;
    private CollisionChecker collisionChecker;
    private Player player;

    private String[] maps = {"/Maps/Map_01.txt", "/Maps/Map_02.txt", "/Maps/Map_03.txt"};
    private int currentMapIndex = 0;

    private int defaultMaxWorldCol = 30;
    private int defaultMaxWorldRow = 30;

    private int map03MaxWorldCol = 30;
    private int map03MaxWorldRow = 30;

    public MapManager(GameCanvas gc, CollisionChecker collisionChecker, TileManager tileM, Player player) {
        this.gc = gc;
        this.collisionChecker = collisionChecker;
        this.tileM = tileM;
        this.player = player;

        loadMap();

    }

    public void checkSwitchMap() {
        if (player.worldX >= 800 && player.worldY >= 800) {
            if (currentMapIndex == maps.length - 1) {
                System.out.println("Victory");
            } else {
                System.out.println("map switched");
                switchMap();
            }
        }
    }

    public void switchMap() {
        currentMapIndex++;
        System.out.println("Switching to map: " + maps[currentMapIndex]);

        updateMap();

        player.worldX = 50;
        player.worldY = 30;

        loadMap();
        gc.cChecker = new CollisionChecker(gc);
    }

    public void updateMap() {
        if (currentMapIndex == 2) {
            gc.maxWorldCol = map03MaxWorldCol;
            gc.maxWorldRow = map03MaxWorldRow;
        } else {
            gc.maxWorldCol = defaultMaxWorldCol;
            gc.maxWorldRow = defaultMaxWorldRow;
        }

        gc.worldWidth = gc.tileSize * gc.maxWorldCol;
        gc.worldHeight = gc.tileSize * gc.maxWorldRow;
    }

    private void loadMap() {
        tileM.mapTileNum = new int[gc.maxWorldCol][gc.maxWorldRow];
        tileM.loadMap(maps[currentMapIndex]);
    }

    public int getMaxWorldCol() {
        return gc.maxWorldCol;
    }

    public int getMaxWorldRow() {
        return gc.maxWorldRow;
    }
}
