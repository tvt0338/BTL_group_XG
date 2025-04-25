import java.util.Random;

public class Void extends Monster{
    private boolean isVisible;
    private long lastToggleTime;
    private static final long INVISIBLE_TIME = 3000;
    private static final long VISIBLE_TIME = 1000;
    private Random random;
    private double invisibleSpeed;

    public Void(int x, int y, double speed) {
        super(x, y, speed);
        this.isVisible = true;
        this.lastToggleTime = System.currentTimeMillis();
        this.random = new Random();
        this.invisibleSpeed = speed * 1.5;
    }

    @Override
    public void update() {
        if (!isAlive()) {return; }

        long currentTime = System.currentTimeMillis();
        if (isVisible && currentTime - lastToggleTime >= VISIBLE_TIME) {
            isVisible = false;
            lastToggleTime = currentTime;
            System.out.println("Void is now invisible.");
        } else if (!isVisible && currentTime - lastToggleTime >= INVISIBLE_TIME) {
            isVisible = true;
            lastToggleTime = currentTime;
            System.out.println("Void will appear at (" + x + " " + y + ")");
        }

        moveRandomly(isVisible ? speed : invisibleSpeed);
    }

    private void moveRandomly(double v) {
        int direction = random.nextInt(4);
        int newX = x, newY = y;
        switch (direction) {
            case 0: newY -= 1; break;
            case 1: newY += 1; break;
            case 2: newX -= 1; break;
            case 3: newX += 1; break;
        }

        //If(check OutOfBound)
        if (isValidMove(newX, newY)) {
            x = newX;
            y = newY;
            if (isVisible) {
                System.out.println("Void moved to " + x + " " + y);
            }
        }
    }

    @Override
    public boolean dealDamage(Player player) {
        if (isVisible && x == player.getX() && y == player.getY()) {
            System.out.println("Void deals 1 dmg to Bomberman. ");
            return true;
        }
        return false;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void takeDamage() {
        isAlive = false;
        System.out.println("Void was killed!");
    }
}