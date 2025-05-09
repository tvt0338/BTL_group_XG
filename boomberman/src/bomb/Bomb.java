package bomb;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import bomberman.Player;

import java.util.Objects;

public class Bomb {
    private double x, y;
    private final double radius = 25;
    private final long createdTime;  // thời gian đặt bom
    private boolean exploded = false;
    private int[][] mapData;
    private int cellSize;
    private boolean[][] explosionArea; // Lưu vùng nổ
    private int explosionRange; // Phạm vi nổ
    private Player player; // Tham chiếu đến player

    // Thời gian chờ nổ và thời gian hiệu ứng nổ
    private static final long EXPLOSION_DELAY = 1500; // 3 giây chờ nổ
    private static final long EXPLOSION_DURATION = 1000; // 1 giây hiệu ứng nổ

    // Ảnh bom và ảnh nổ các hướng
    private static final Image bombImage = new Image(Objects.requireNonNull(Bomb.class.getResourceAsStream("bomb.png")));
    private static final Image explodeCenter = new Image(Objects.requireNonNull(Bomb.class.getResourceAsStream("explode_center.png")));
    private static final Image explodeUp = new Image(Objects.requireNonNull(Bomb.class.getResourceAsStream("explode_depth.png")));
    private static final Image explodeDown = new Image(Objects.requireNonNull(Bomb.class.getResourceAsStream("explode_depth.png")));
    private static final Image explodeLeft = new Image(Objects.requireNonNull(Bomb.class.getResourceAsStream("explode_width.png")));
    private static final Image explodeRight = new Image(Objects.requireNonNull(Bomb.class.getResourceAsStream("explode_width.png")));

    public Bomb(double x, double y, Player player) {
        this.x = x;
        this.y = y;
        this.createdTime = System.currentTimeMillis();
        this.player = player;
        this.explosionRange = player.getBombRange();
    }

    public void setMapData(int[][] mapData, int cellSize) {
        this.mapData = mapData;
        this.cellSize = cellSize;
    }

    public boolean isExploded() {
        return exploded;
    }

    private void calculateExplosionArea() {
        if (mapData == null) return;

        int bombCol = (int)Math.floor(x / cellSize);
        int bombRow = (int)Math.floor(y / cellSize);
        explosionArea = new boolean[mapData.length][mapData[0].length];

        // Đánh dấu vị trí bom
        explosionArea[bombRow][bombCol] = true;

        // Kiểm tra 4 hướng
        int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}}; // up, down, left, right
        for (int[] dir : directions) {
            for (int i = 1; i <= explosionRange; i++) {
                int newRow = bombRow + dir[0] * i;
                int newCol = bombCol + dir[1] * i;

                // Kiểm tra giới hạn map
                if (newRow < 0 || newRow >= mapData.length || newCol < 0 || newCol >= mapData[0].length) {
                    break;
                }

                // Kiểm tra va chạm với tường
                if (mapData[newRow][newCol] == 1) { // Tường không thể phá
                    break;
                }

                // Đánh dấu vùng nổ
                explosionArea[newRow][newCol] = true;

                // Nếu là tường có thể phá, phá nó và dừng lan truyền
                if (mapData[newRow][newCol] == 3) { // Tường có thể phá
                    mapData[newRow][newCol] = 2; // Chuyển thành cỏ
                    break;
                }
            }
        }
    }

    // Vẽ bomb hoặc hiệu ứng nổ
    public void render(GraphicsContext gc) {
        long currentTime = System.currentTimeMillis();
        if (!exploded && currentTime - createdTime >= EXPLOSION_DELAY) {
            exploded = true;
            calculateExplosionArea();
        }

        if (!exploded) {
            gc.drawImage(bombImage, x - radius, y - radius, radius * 2, radius * 2);
        } else if (explosionArea != null) {
            int bombCol = (int)Math.floor(x / cellSize);
            int bombRow = (int)Math.floor(y / cellSize);
            double tileSize = radius * 2;

            // Vẽ nổ trung tâm
            gc.drawImage(explodeCenter, x - radius, y - radius, tileSize, tileSize);

            // Vẽ nổ 4 hướng dựa trên vùng nổ
            // Hướng lên
            for (int i = 1; i <= explosionRange; i++) {
                int newRow = bombRow - i;
                if (newRow < 0 || !explosionArea[newRow][bombCol]) break;
                gc.drawImage(explodeUp, x - radius, y - radius - (i * tileSize), tileSize, tileSize);
            }

            // Hướng xuống
            for (int i = 1; i <= explosionRange; i++) {
                int newRow = bombRow + i;
                if (newRow >= mapData.length || !explosionArea[newRow][bombCol]) break;
                gc.drawImage(explodeDown, x - radius, y - radius + (i * tileSize), tileSize, tileSize);
            }

            // Hướng trái
            for (int i = 1; i <= explosionRange; i++) {
                int newCol = bombCol - i;
                if (newCol < 0 || !explosionArea[bombRow][newCol]) break;
                gc.drawImage(explodeLeft, x - radius - (i * tileSize), y - radius, tileSize, tileSize);
            }

            // Hướng phải
            for (int i = 1; i <= explosionRange; i++) {
                int newCol = bombCol + i;
                if (newCol >= mapData[0].length || !explosionArea[bombRow][newCol]) break;
                gc.drawImage(explodeRight, x - radius + (i * tileSize), y - radius, tileSize, tileSize);
            }
        }
    }

    public boolean isExpired() {
        // Xoá bom khỏi danh sách sau khi hiệu ứng nổ kết thúc
        return exploded && System.currentTimeMillis() - createdTime > (EXPLOSION_DELAY + EXPLOSION_DURATION);
    }

    public boolean isInExplosionArea(double playerX, double playerY) {
        if (!exploded || explosionArea == null) return false;

        int playerCol = (int)Math.floor(playerX / cellSize);
        int playerRow = (int)Math.floor(playerY / cellSize);

        return playerRow >= 0 && playerRow < explosionArea.length &&
                playerCol >= 0 && playerCol < explosionArea[0].length &&
                explosionArea[playerRow][playerCol];
    }

    public double getX() { return x; }
    public double getY() { return y; }
}
