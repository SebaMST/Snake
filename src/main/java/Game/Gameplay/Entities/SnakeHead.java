package Game.Gameplay.Entities;

import Game.GUI.GameUI;
import Game.Gameplay.Exceptions.Exception_SnakeDead;

public class SnakeHead extends SnakeBody {
    private static final String IMAGE_SNAKE_HEAD;

    static {
        IMAGE_SNAKE_HEAD = "snakehead";
    }

    public SnakeHead(int x, int y, int direction) {
        super(IMAGE_SNAKE_HEAD+direction+(GameUI.getInvertedKeys() ? "_daze" : ""),x,y);
    }

    @Override
    public void collide() throws Exception_SnakeDead {
        throw new Exception_SnakeDead("it collided with a snake head");
    }
}
