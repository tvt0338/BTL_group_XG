package portal;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Portal {
    private double x, y;
    private int cellSize;
    private static Image portalImage;
    private boolean isActive = false;

    public Portal(double x, double y, int cellSize) {
        this.x = x;
        this.y = y;
        this.cellSize = cellSize;
    }

    public static void loadSprites() {
        portalImage = new Image(Portal.class.getResourceAsStream("/portal/portal.gif"));
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public void render(GraphicsContext gc) {
        if (!isActive) return;
        gc.drawImage(portalImage, x * cellSize, y * cellSize, cellSize, cellSize);
    }

    public boolean isColliding(double playerX, double playerY, double playerSize) {
        if (!isActive) return false;
        double portalX = x * cellSize;
        double portalY = y * cellSize;
        double portalCenterX = portalX + cellSize / 2.0;
        double portalCenterY = portalY + cellSize / 2.0;
        double playerCenterX = playerX;
        double playerCenterY = playerY;
        double distance = Math.hypot(portalCenterX - playerCenterX, portalCenterY - playerCenterY);
        return distance < cellSize / 2.0;
    }
}