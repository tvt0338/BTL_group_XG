public class Bomb {
    private int x, y;
    private long placedTime;
    private static final long EXPLOSION_TIME = 4000;
    private boolean exploded;
    private int blastRadius;

    public Bomb(int x, int y, int blastRadius) {
        this.x = x;
        this.y = y;
        this.placedTime = System.currentTimeMillis();
        this.exploded = false;
        this.blastRadius = blastRadius;
    }

    public void update() {
        if (exploded) {return; }
        long currentTime = System.currentTimeMillis();
        if (currentTime - placedTime >= EXPLOSION_TIME) {
            exploded();
        }
    }

    public void exploded() {
        exploded = true;
        System.out.println("Bomb at (" + x + " " + y + ") exploded ");
    }

    public boolean hasExploded() {
        return exploded;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getBlastRadius() {
        return blastRadius;
    }
}
