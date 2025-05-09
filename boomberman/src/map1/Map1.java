package map1;

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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import enemy.Balloom;
import enemy.Oneal;
import portal.Portal;
import items.Speed;
import items.Flame;
import items.Health;

public class Map1 extends Application {
    private static final String MAP_PATH = "src/map1/Map_01.txt";
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
    private List<Balloom> ballooms = new ArrayList<>();
    private List<Oneal> oneals = new ArrayList<>();
    private List<Speed> speedItems = new ArrayList<>();
    private List<Flame> flameItems = new ArrayList<>();
    private List<Health> healthItems = new ArrayList<>();
    private Portal portal;
    private boolean isMapComplete = false;
    private Stage stage;
    private AnimationTimer gameLoop;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        // Lấy kích thước màn hình trước
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        screenWidth = screenBounds.getWidth();
        screenHeight = screenBounds.getHeight();

        loadImages();
        loadMap();
        Player.loadSprites("src/images/player/");
        Balloom.loadSprites();
        Oneal.loadSprites();
        Portal.loadSprites();
        Speed.loadSprite();
        Flame.loadSprite();
        Health.loadSprite();

        // Tính cellSize để map vừa chiều rộng màn hình
        cols = mapData[0].length;
        rows = mapData.length;
        cellSize = (int) (screenWidth / cols);

        // Khởi tạo player ở vị trí giữa ô đầu tiên
        player = new Player(100, 100);
        player.setMapData(mapData, cellSize);

        // Thêm 6 Speed items
        speedItems.add(new Speed(1300, 100));
        speedItems.add(new Speed(400, 100));
        speedItems.add(new Speed(700, 700));
        speedItems.add(new Speed(800, 500));
        speedItems.add(new Speed(300, 900));
        speedItems.add(new Speed(500, 600));

        // Thêm 3 Flame items
        flameItems.add(new Flame(300, 100));
        flameItems.add(new Flame(500, 300));
        flameItems.add(new Flame(700, 500));

        // Thêm 3 Health items
        healthItems.add(new Health(300, 200));
        healthItems.add(new Health(550, 350));
        healthItems.add(new Health(750, 600));

        // Thêm nhiều Balloom và Oneal ngẫu nhiên vào map để test
        Random rand = new Random();
        int numBallooms = 5;
        int numOneals = 3;
        for (int i = 0; i < numBallooms; i++) {
            int x = rand.nextInt(cols);
            int y = rand.nextInt(rows);
            int moveThreshold = 50 + rand.nextInt(11);
            if (mapData[y][x] == 2) {
                ballooms.add(new Balloom(x, y, moveThreshold, cellSize));
            } else {
                i--;
            }
        }
        for (int i = 0; i < numOneals; i++) {
            int x = rand.nextInt(cols);
            int y = rand.nextInt(rows);
            int moveThreshold = 50 + rand.nextInt(11);
            if (mapData[y][x] == 2) {
                oneals.add(new Oneal(x, y, moveThreshold, cellSize, player));
            } else {
                i--;
            }
        }

        portal = new Portal(cols - 2, rows - 2, cellSize); // Đặt portal ở góc phải dưới

        Canvas canvas = new Canvas(screenWidth, screenHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Scene scene = new Scene(new StackPane(canvas));
        stage.setScene(scene);
        stage.setTitle("Map1");
        stage.setFullScreen(true); // hoặc stage.setMaximized(true);
        stage.show();

        // Xử lý phím
        scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));

        // Vòng lặp game
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

        // Cập nhật map data cho tất cả bomb
        for (Bomb bomb : player.getBombs()) {
            bomb.setMapData(mapData, cellSize);
        }
        if (player.getHp() <= 0) {
            isGameOver = true;
        }
        // Kiểm tra va chạm với Speed items
        speedItems.removeIf(speedItem -> {
            double dx = player.getX() - speedItem.getX();
            double dy = player.getY() - speedItem.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance < (player.getSize() + speedItem.getSize()) / 2) {
                speedItem.applyEffect(player);
                return true; // Xóa item sau khi ăn
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
                return true; // Xóa item sau khi ăn
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
                return true; // Xóa item sau khi ăn
            }
            return false;
        });

        // Update Ballooms
        for (Balloom b : ballooms) {
            b.setMapData(mapData, cellSize);
            b.setBombs(player.getBombs());
            b.update();
            if (b.isAlive()) {
                b.dealDamage(player);
            }
        }
        ballooms.removeIf(b -> !b.isAlive());
        // Update Oneals
        for (Oneal o : oneals) {
            o.setMapData(mapData, cellSize);
            o.setBombs(player.getBombs());
            o.update();
            if (o.isAlive()) {
                o.dealDamage(player);
            }
        }
        oneals.removeIf(o -> !o.isAlive());

        // Kiểm tra nếu đã tiêu diệt hết quái vật
        if (ballooms.isEmpty() && oneals.isEmpty() && !isMapComplete) {
            portal.setActive(true);
            isMapComplete = true;
        }

        // Kiểm tra va chạm với portal
        if (portal.isActive() && portal.isColliding(player.getX(), player.getY(), player.getSize())) {
            try {
                gameLoop.stop(); // Dừng vòng lặp game trước khi chuyển scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/map/map2.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Map 2");
                stage.setFullScreen(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, screenWidth, screenHeight);

        // Vẽ nền
        gc.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight);
        // Camera theo player
        double offsetX = player.getX() - screenWidth / 2;
        double offsetY = player.getY() - screenHeight / 2;

        gc.save();
        gc.translate(-offsetX, -offsetY);

        // Vẽ map
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int val = mapData[r][c];
                Image img = tileImages.get(val);
                if (img != null)
                    gc.drawImage(img, c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }


        // Vẽ player
        player.render(gc);

        // Vẽ Speed items
        for (Speed speedItem : speedItems) {
            speedItem.render(gc);
        }

        // Vẽ Flame items
        for (Flame flameItem : flameItems) {
            flameItem.render(gc);
        }

        // Vẽ Health items
        for (Health healthItem : healthItems) {
            healthItem.render(gc);
        }

        // Vẽ Ballooms
        for (Balloom b : ballooms) {
            b.render(gc);
        }

        // Vẽ Oneals
        for (Oneal o : oneals) {
            o.render(gc);
        }

        // Vẽ portal
        portal.render(gc);

        gc.restore();

        // Hiển thị khung Game Over nếu player chết
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
