package items;

import bomberman.Player;

abstract class Item {
    protected int x, y;
    protected boolean isCollected = false;
    public abstract void applyEffect(Player player);
}
