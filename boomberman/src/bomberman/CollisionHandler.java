package bomberman;

public class CollisionHandler {
    private int[][] mapData;
    private int cellSize;
    private double radius;

    public CollisionHandler(int[][] mapData, int cellSize, double radius) {
        this.mapData = mapData;
        this.cellSize = cellSize;
        this.radius = radius;
    }

    public boolean canMoveTo(double x, double y, String direction) {
        // Tính toán vị trí các góc của player
        double leftX = x - radius;
        double rightX = x + radius - 1;
        double topY = y - radius;
        double bottomY = y + radius - 1;

        int leftCol = (int)Math.floor(leftX / cellSize);
        int rightCol = (int)Math.floor(rightX / cellSize);
        int topRow = (int)Math.floor(topY / cellSize);
        int bottomRow = (int)Math.floor(bottomY / cellSize);

        // Kiểm tra 4 góc đều không nằm trong tường
        if (isColliding(leftCol, topRow) ||
                isColliding(rightCol, topRow) ||
                isColliding(leftCol, bottomRow) ||
                isColliding(rightCol, bottomRow)) {
            return false;
        }
        return true;
    }

    private boolean isColliding(int col, int row) {
        // Kiểm tra giới hạn map
        if (col < 0 || col >= mapData[0].length || row < 0 || row >= mapData.length) {
            return true;
        }

        // Kiểm tra va chạm với tường (1) và tường có thể phá được (3)
        int tileType = mapData[row][col];
        return tileType == 1 || tileType == 3;
    }

    public void setMapData(int[][] mapData) {
        this.mapData = mapData;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
} 