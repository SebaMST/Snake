package Game.Gameplay.Entities;

import Game.Gameplay.Exceptions.Exception_SnakeDead;

public class Obstacle extends TiledEntity {
    private static final String IMAGE_OBSTACLE;

    static {
        IMAGE_OBSTACLE = "obstacle";
    }

    public Obstacle(int x, int y) {
        super(IMAGE_OBSTACLE,x,y);
    }

    @Override
    public void collide() throws Exception_SnakeDead {
        throw new Exception_SnakeDead("it collided with an obstacle");
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Obstacle) {
            return ((Obstacle)obj).getX()==getX() && ((Obstacle)obj).getY()==getY();
        }
        return false;
    }
}
