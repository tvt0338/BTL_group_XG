import java.util.Random;

public class Tanker extends Monster{
    private int hp;

    public Tanker(int x, int y, double speed) {
        super(x, y, speed);
        this.hp = 3;
    }

    @Override
    public void update() {
        if (!isAlive()) return;
        moveSlowly();
    }

    @Override
    public boolean dealDamage(Player player) {
        if (x == player.getX() && y == player.getY()) {
            return true;
        }
        return false;
    }

    private void moveSlowly() {
        Random random = new Random();
        int direction = random.nextInt(4);
        int newX = x, newY = y;
        switch (direction) {
            case 0: newY -= 1; break;
            case 1: newY += 1; break;
            case 2: newX -= 1; break;
            case 3: newX += 1; break;
        }

        if (isValidMove(newX, newY)) {
            x = newX;
            y = newY;
            System.out.println("Tanker moved to:" + x + " " + y);
        }
    }

    public void takeDamage() {
        hp--;
        System.out.println("Remaining tanker HP: " + hp);
        if (hp <= 0) {
            isAlive = false;
            System.out.println("Tanker destroyed!");
        }
    }

    public int getHP() { return hp; }
}