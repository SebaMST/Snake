package Game.Gameplay.Entities;

import Game.Gameplay.Exceptions.Exception_SnakeDead;

public class SnakeBody extends TiledEntity {

    private static final String IMAGE_SNAKE_BODY;

    static {
        IMAGE_SNAKE_BODY = "snakebody";
    }

    public SnakeBody(int x, int y) {
        super(IMAGE_SNAKE_BODY,x,y);
    }

    protected SnakeBody(String image, int x, int y) {
        super(image,x,y);
    }

    @Override
    public void collide() throws Exception_SnakeDead {
        throw new Exception_SnakeDead("it collided with its own tail");
    }
}
