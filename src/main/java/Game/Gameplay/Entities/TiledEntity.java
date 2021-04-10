package Game.Gameplay.Entities;

import Game.Gameplay.Exceptions.Exception_SnakeDead;

public abstract class TiledEntity {
    private String image;
    private int x, y;

    protected TiledEntity(String image, int x, int y) {
        setImage(image);
        setXY(x,y);
    }

    public void setImage(String image) {
        this.image=image;
    }

    public String getImage() {
        return image;
    }

    private void setX(int x) {
        this.x = x;
    }

    private void setY(int y) {
        this.y = y;
    }

    private void setXY(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract void collide() throws Exception_SnakeDead;
}
