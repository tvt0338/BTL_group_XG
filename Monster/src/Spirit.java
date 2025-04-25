import java.util.ArrayList;
import java.util.Random;

public class Spirit extends Monster{
    private ArrayList<Bomb> bombs;
    private Random random;
    private static final int DETECTION_RANGE = 5;
    private Player player;
    private long lastBombTime;
    private static final long BOMB_COOLDOWN = 5000;

    public Spirit(int x, int y, double speed, Player player) {
        super(x, y, speed);
        this.bombs = new ArrayList<>();
        this.random = new Random();
        this.player = player;
        this.lastBombTime = 0;
    }

    private boolean isPlayerInRange() {
        return Math.abs(player.getX() - x) <= DETECTION_RANGE &&
                Math.abs(player.getY() - y) <= DETECTION_RANGE;
    }
    public void update() {
        if (!isAlive()) return;

        if (isPlayerInRange()) {
            chasePlayer();
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastBombTime >= BOMB_COOLDOWN) {
                placeBomb();
                lastBombTime = currentTime;
            }
        } else {
            moveRandomly();
        }

        for (Bomb bomb : bombs) {
            bomb.update();
        }
        bombs.removeIf(Bomb::hasExploded);
    }

    @Override
    public boolean dealDamage(Player player) {
        return false;
    }

    private void chasePlayer() {
        int newX = x, newY = y;
        if (player.getX() > x) newX += 1;
        else if (player.getX() < x) newX -= 1;
        if (player.getY() > y) newY += 1;
        else if (player.getY() < y) newY -= 1;
    }

    private void moveRandomly() {
        int direction = random.nextInt(4);
        int newX = x, newY = y;
        switch (direction) {
            case 0: newY -= 1; break;
            case 1: newY += 1; break;
            case 2: newX -= 1; break;
            case 3: newX += 1; break;
        }

        if(isValidMove(newX, newY)) {
            x = newX;
            y = newY;
            System.out.println("Spirit moved to: " + x + " " + y);
        }
    }

    private void placeBomb() {
        bombs.add(new Bomb(x, y, 1));
        System.out.println("Spirit placed bomb at (" + x + " " + y + ")");
    }

    public void takeDamage() {
        isAlive = false;
        System.out.println("Spirit destroyed!");
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }
}