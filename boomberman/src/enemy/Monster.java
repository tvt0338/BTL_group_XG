package enemy;

import bomberman.Player;
import javafx.scene.canvas.GraphicsContext;

public abstract class Monster {
    protected int x, y;
    protected double speed;
    protected int hp, maxHp;
    protected int size; // Kích thước, giống bomberman
    protected boolean isAlive;

    public Monster(int x, int y, double speed, int maxHp, int size) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.size = size;
        this.isAlive = true;
    }

    public abstract void update();

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isAlive() { return isAlive; }

    public abstract boolean dealDamage(Player player);

    public abstract void takeDamage(int damage);

    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public double getSpeed() { return speed; }
    public int getSize() { return size; }

    // Kiểm tra di chuyển hợp lệ (có thể override ở class con)
    protected boolean isValidMove(int newX, int newY, int mapWidth, int mapHeight) {
        return newX > 0 && newX < mapWidth - 1 &&
                newY > 0 && newY < mapHeight - 1;
    }

    public abstract void render(GraphicsContext gc);
}