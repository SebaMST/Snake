package Game.Gameplay.Entities;

import Game.Gameplay.Exceptions.Exception_SnakeDead;
import Game.Gameplay.Level;
import Game.SnakeGame;

public class Berry extends Food {
    private static final String IMAGE_BERRY;

    static {
        IMAGE_BERRY = "berry";
    }

    public Berry(int x, int y) {
        super(IMAGE_BERRY,x,y);
    }

    @Override
    public void collide() throws Exception_SnakeDead {
        super.collide();
        Level currentLevelObject = SnakeGame.getCurrentLevelObject();

        currentLevelObject.decreaseSnakeLength();
        currentLevelObject.decreaseSnakeLength();
    }
}
