package Game.Gameplay.Entities;

import Game.GUI.GameUI;
import Game.Gameplay.Level;
import Game.SnakeGame;

public class Mushroom extends Food {
    private static final String IMAGE_MUSHROOM;

    static {
        IMAGE_MUSHROOM = "mushroom";
    }

    public Mushroom(int x, int y) {
        super(IMAGE_MUSHROOM,x,y);
    }

    @Override
    public void collide() {
        super.collide();
        Level currentLevelObject = SnakeGame.getCurrentLevelObject();

        currentLevelObject.decreaseSnakeLength();
        GameUI.invertMovementKeys();
    }
}
