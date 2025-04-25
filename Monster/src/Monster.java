public abstract class Monster {
    protected int x, y;
    protected double speed;
    protected boolean isAlive;

    public Monster(int x, int y, double speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.isAlive = true;
    }

    public abstract void update();

    public int getX() {return x;}
    public int getY() {return y;}

    public boolean isAlive() {
        return isAlive;
    }
    public abstract boolean dealDamage(Player player);

    protected boolean isValidMove(int newX, int newY) {
        return newX > 0 && newX < Game.MAP_WIDTH - 1 &&
                newY > 0 && newY < Game.MAP_HEIGHT - 1;
    }
}
