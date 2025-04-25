import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private ArrayList<Monster> monsters;
    private Void voidEnemy;
    private Spirit spirit;
    private Tanker tanker;
    private Player player;
    private Random random;
    public static final int MAP_WIDTH = 20;
    public static final int MAP_HEIGHT = 20;

    public Game() {
        monsters = new ArrayList<>();
        random = new Random();

        for (int i = 0; i < 10; i++) {
            int[] pos = spawnRandomly();
        }

        player = new Player(spawnRandomly()[0], spawnRandomly()[1]);
        voidEnemy = new Void(spawnRandomly()[0], spawnRandomly()[1], 1.0);
        spirit = new Spirit(spawnRandomly()[0], spawnRandomly()[1], 0.8, player);
        tanker = new Tanker(spawnRandomly()[0], spawnRandomly()[1], 0.5);

        monsters.add(voidEnemy);
        monsters.add(spirit);
        monsters.add(tanker);
    }

    private int[] spawnRandomly() {
        int x, y;
        do {
            x = random.nextInt(MAP_WIDTH - 2) + 1;
            y = random.nextInt(MAP_HEIGHT - 2) + 1;
        } while (!isValidSpawnPosition(x, y));
        return new int[]{x, y};
    }

    private boolean isValidSpawnPosition(int x, int y) {
        if (x <= 0 || x >= MAP_WIDTH - 1 || y <= 0 || y >= MAP_HEIGHT) {
            return false;
        }
        if (player != null && x == player.getX() && y == player.getY()) {
            return false;
        }
        for (Monster monster : monsters) {
            if (monster.isAlive() && monster.getX() == x && monster.getY() == y) {
                return false;
            }
        }
        return true;
    }

    public void update() {
        for (Monster monster : monsters) {
            monster.update();
            if (monster != spirit && monster.dealDamage(player)) {
                player.takeDamage();
            }
        }

        for (Bomb bomb : spirit.getBombs()) {
            if (bomb.hasExploded()) {
                int bx = bomb.getX(), by = bomb.getY();
                int radius  = bomb.getBlastRadius();

                int[][] directions = {{0, 0}, {0, 1}, {0, -1}, {1, 0}, {-1, 0}};
                for (int[] dir : directions) {
                    int tx = bx + dir[0], ty = by + dir[1];
                    if (tx >= 0 && tx < MAP_WIDTH && ty >= 0 && ty < MAP_HEIGHT) {
                        if (tx == player.getX() && ty == player.getY()) {
                            player.takeDamage();
                        }
                        if (tx == voidEnemy.getX() && ty == voidEnemy.getY()) {
                            voidEnemy.takeDamage();
                        }
                        if (tx == spirit.getX() && ty == spirit.getY()) {
                            spirit.takeDamage();
                        }
                        if (tx == tanker.getX() && ty == tanker.getY()) {
                            tanker.takeDamage();
                        }
                    }
                }
            }
        }
        if (!tanker.isAlive()) {
            int[] newPos = spawnRandomly();
            tanker = new Tanker(newPos[0], newPos[1], 0.5);
            monsters.removeIf(e -> !e.isAlive());
            monsters.add(tanker);

        }
    }

    public void run() {
        while (player.getHealth() > 0) {
            update();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Game over");
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.run();
    }

}
