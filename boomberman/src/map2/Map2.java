package map2;

import bomb.Bomb;
import bomberman.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import enemy.Balloom;
import enemy.Oneal;
import portal.Portal;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.animation.RotateTransition;
import javafx.util.Duration;
import javafx.geometry.Point3D;
import enemy.Void;
import enemy.Tanker;
import enemy.Fly;
import items.Speed;
import items.Flame;
import items.God_eye;
import items.Health;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Map2 extends Application {
    private static final String MAP_PATH = "src/map2/Map_02.txt";
    private static final String IMAGE_PATH = "src/images/map/";
    private Map<Integer, Image> tileImages = new HashMap<>();
    private int[][] mapData;

    private Player player;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private Image backgroundImage;

    private int cellSize;
    private int rows, cols;
    private double screenWidth, screenHeight;
    private boolean isGameOver = false;
    private List<Void> voids = new ArrayList<>();
    private List<Tanker> tankers = new ArrayList<>();
    private List<Fly> flies = new ArrayList<>();
    private List<Balloom> ballooms = new ArrayList<>();
    private List<Oneal> oneals = new ArrayList<>();
    private List<Speed> speedItems = new ArrayList<>();
    private List<Flame> flameItems = new ArrayList<>();
    private List<God_eye> godEyeItems = new ArrayList<>();
    private List<Health> healthItems = new ArrayList<>();

    private Portal portal;
    private boolean isMapComplete = false;
    private Stage stage;

    private final double NORMAL_SPEED = 3.0;
    private final double SLOW_SPEED = 1.0;

    private AnimationTimer gameLoop;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        screenWidth = screenBounds.getWidth();
        screenHeight = screenBounds.getHeight();

        loadImages();
        loadMap();
        Player.loadSprites("src/images/player/");
        Balloom.loadSprites();
        Oneal.loadSprites();
        Portal.loadSprites();
        Void.loadSprites();
        Tanker.loadSprites();
        Fly.loadSprites();
        Speed.loadSprite();
        Flame.loadSprite();
        God_eye.loadSprite();
        Health.loadSprite();

        cols = mapData[0].length;
        rows = mapData.length;
        cellSize = (int) (screenWidth / cols);

        player = new Player(100, 100);
        player.setMapData(mapData, cellSize);

        Random rand = new Random();
        int numVoids = 5;
        for (int i = 0; i < numVoids; i++) {
            int x = rand.nextInt(cols);
            int y = rand.nextInt(rows);
            int moveThreshold = 50 + rand.nextInt(11);
            if (mapData[y][x] == 2) {
                voids.add(new Void(x, y, moveThreshold, cellSize, player));
            } else {
                i--;
            }
        }


        int numTankers = 3; // Số lượng quái tanker
        for (int i = 0; i < numTankers; i++) {
            int x = rand.nextInt(cols);
            int y = rand.nextInt(rows);
            int moveThreshold = 30 + rand.nextInt(20);
            if (mapData[y][x] == 2) { // Chỉ spawn ở ô grass
                tankers.add(new Tanker(x, y, moveThreshold, cellSize, player));
            } else {
                i--; // Chọn lại vị trí nếu không hợp lệ
            }
        }

        int numFlies = 5;
        for (int i = 0; i < numFlies; i++) {
            int x = rand.nextInt(cols);
            int y = rand.nextInt(rows);
            double moveThreshold = 0.25+ rand.nextDouble(0.9);
            if (mapData[y][x] == 2) { // chỉ spawn ở ô grass
                flies.add(new Fly(x, y, moveThreshold, cellSize, player));
            } else {
                i--;
            }
        }


        portal = new Portal(cols - 2, rows - 2, cellSize);

        // Thêm 6 Speed items
        speedItems.add(new Speed(200, 100));
        speedItems.add(new Speed(1300, 100));
        speedItems.add(new Speed(600, 300));
        speedItems.add(new Speed(800, 400));
        speedItems.add(new Speed(300, 500));
        speedItems.add(new Speed(480, 900));

        // Thêm 3 Flame items
        flameItems.add(new Flame(300, 100));
        flameItems.add(new Flame(600, 300));
        flameItems.add(new Flame(800, 500));

        // Thêm 5 God_eye items
        godEyeItems.add(new God_eye(100, 250));
        godEyeItems.add(new God_eye(450, 290));
        godEyeItems.add(new God_eye(850, 350));
        godEyeItems.add(new God_eye(350, 430));
        godEyeItems.add(new God_eye(550, 550));

        // Thêm 3 Health items
        healthItems.add(new Health(300, 150));
        healthItems.add(new Health(610, 650));
        healthItems.add(new Health(650, 550));

        Canvas canvas = new Canvas(screenWidth, screenHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Scene scene = new Scene(new StackPane(canvas));
        stage.setScene(scene);
        stage.setTitle("Map2");
        stage.setFullScreen(true);
        stage.show();

        scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));

        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                update();
                render(gc);
            }
        };
        gameLoop.start();
    }

    private void update() {
        if (isGameOver) return;
        player.update(pressedKeys);
        for (Bomb bomb : player.getBombs()) {
            bomb.setMapData(mapData, cellSize);
        }
        if (player.getHp() <= 0) {
            isGameOver = true;
        }
        for (Void v : voids) {
            v.setMapData(mapData, cellSize);
            v.setBombs(player.getBombs());
            v.update();
            if (v.isAlive()) {
                v.dealDamage(player);
            }
        }
        voids.removeIf(v -> !v.isAlive());

        for (Tanker t : tankers) {
            t.setMapData(mapData, cellSize);
            t.setBombs(player.getBombs());
            t.update();
            if (t.isAlive()) {
                t.dealDamage(player);
            }
        }
        tankers.removeIf(t -> !t.isAlive());

        for (Fly f : flies) {
            f.setMapData(mapData, cellSize);
            f.setBombs(player.getBombs());
            f.update();
            if (f.isAlive()) {
                f.dealDamage(player);
            }
        }
        flies.removeIf(f -> !f.isAlive());

        // Kiểm tra va chạm với Speed items
        speedItems.removeIf(speedItem -> {
            double dx = player.getX() - speedItem.getX();
            double dy = player.getY() - speedItem.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance < (player.getSize() + speedItem.getSize()) / 2) {
                speedItem.applyEffect(player);
                return true;
            }
            return false;
        });

        // Kiểm tra va chạm với Flame items
        flameItems.removeIf(flameItem -> {
            double dx = player.getX() - flameItem.getX();
            double dy = player.getY() - flameItem.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance < (player.getSize() + flameItem.getSize()) / 2) {
                flameItem.applyEffect(player);
                return true;
            }
            return false;
        });

        // Kiểm tra va chạm với God_eye items
        godEyeItems.removeIf(godEyeItem -> {
            double dx = player.getX() - godEyeItem.getX();
            double dy = player.getY() - godEyeItem.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance < (player.getSize() + godEyeItem.getSize()) / 2) {
                godEyeItem.applyEffect(player);
                return true;
            }
            return false;
        });

        // Kiểm tra va chạm với Health items
        healthItems.removeIf(healthItem -> {
            double dx = player.getX() - healthItem.getX();
            double dy = player.getY() - healthItem.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance < (player.getSize() + healthItem.getSize()) / 2) {
                healthItem.applyEffect(player);
                return true;
            }
            return false;
        });

        if (voids.isEmpty() && flies.isEmpty() && tankers.isEmpty() && !isMapComplete) {
            portal.setActive(true);
            isMapComplete = true;
        }

        // Làm chậm player khi lại gần portal
        double portalCenterX = (cols - 2) * cellSize + cellSize / 2.0;
        double portalCenterY = (rows - 2) * cellSize + cellSize / 2.0;
        double playerCenterX = player.getX();
        double playerCenterY = player.getY();
        double distance = Math.hypot(portalCenterX - playerCenterX, portalCenterY - playerCenterY);
        if (portal.isActive() && distance < cellSize * 1.5) {
            player.setSpeed(SLOW_SPEED);
        } else {
            player.setSpeed(NORMAL_SPEED);
        }

        if (portal.isActive() && portal.isColliding(player.getX(), player.getY(), player.getSize())) {
            try {
                // Dừng game loop trước khi chuyển scene
                if (gameLoop != null) {
                    gameLoop.stop();
                }

                // Load scene mới trước
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/map/map3.fxml"));
                Parent nextRoot = loader.load();
                Scene nextScene = new Scene(nextRoot);

                // Set scene mới cho stage
                stage.setScene(nextScene);
                stage.setTitle("Map 3");
                stage.setFullScreen(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, screenWidth, screenHeight);
        gc.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight);
        double offsetX = player.getX() - screenWidth / 2;
        double offsetY = player.getY() - screenHeight / 2;
        gc.save();
        gc.translate(-offsetX, -offsetY);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int val = mapData[r][c];
                Image img = tileImages.get(val);
                if (img != null)
                    gc.drawImage(img, c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }
        player.render(gc);
        for (Void v : voids) {
            v.render(gc);
        }
        for (Tanker t : tankers) {
            t.render(gc);
        }

        for (Fly f : flies) {
            f.render(gc);
        }

        portal.render(gc);

        // Vẽ Speed items
        for (Speed speedItem : speedItems) {
            speedItem.render(gc);
        }

        // Vẽ Flame items
        for (Flame flameItem : flameItems) {
            flameItem.render(gc);
        }

        // Vẽ God_eye items
        for (God_eye godEyeItem : godEyeItems) {
            godEyeItem.render(gc);
        }

        // Vẽ Health items
        for (Health healthItem : healthItems) {
            healthItem.render(gc);
        }

        gc.restore();
        if (isGameOver) {
            gc.save();
            gc.setGlobalAlpha(0.7);
            gc.setFill(javafx.scene.paint.Color.BLACK);
            gc.fillRect(0, 0, screenWidth, screenHeight);
            gc.setGlobalAlpha(1.0);
            gc.setFill(javafx.scene.paint.Color.WHITE);
            gc.setFont(new javafx.scene.text.Font(80));
            gc.fillText("GAME OVER", screenWidth / 2 - 220, screenHeight / 2);
            gc.restore();
        }
    }

    private void loadImages() throws Exception {
        tileImages.put(1, new Image(new FileInputStream(IMAGE_PATH + "wall.png")));
        tileImages.put(2, new Image(new FileInputStream(IMAGE_PATH + "grass.png")));
        tileImages.put(3, new Image(new FileInputStream(IMAGE_PATH + "destructiblewall.png")));
        backgroundImage = new Image(new FileInputStream("src/images/map/background.png"));
    }

    private void loadMap() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(MAP_PATH));
        int height = lines.size();
        int width = lines.get(0).trim().split("\\s+").length;
        mapData = new int[height][width];
        for (int i = 0; i < height; i++) {
            String[] parts = lines.get(i).trim().split("\\s+");
            for (int j = 0; j < width; j++) {
                mapData[i][j] = Integer.parseInt(parts[j]);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}