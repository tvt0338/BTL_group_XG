public class Player {
    private int x, y;
    private int health;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.health = 3;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public void move(int dx, int dy) {
        int newX = x + dx;
        int newY = y + dy;
        if (newX > 0 && newX < Game.MAP_WIDTH - 1 &&
            newY > 0 && newY < Game.MAP_HEIGHT - 1) {
            x = newX;
            y = newY;
        }
    }

    public void takeDamage() {
        health--;
        System.out.println("Bomberman has taken 1 dmg, remaining: " + health);
        if (health <= 0) {
            System.out.println("Bomberman died");
        }
    }
}
