package Game.Gameplay.Entities;

import Game.Gameplay.Config.GameplayConfig;
import Game.Gameplay.Level;
import Game.SnakeGame;

public class Watermelon extends Food {
    private static final String IMAGE_WATERMELON;

    static {
        IMAGE_WATERMELON = "watermelon";
    }

    public Watermelon(int x, int y) {
        super(IMAGE_WATERMELON,x,y);
    }

    @Override
    public void collide() {
        super.collide();
        Level currentLevelObject = SnakeGame.getCurrentLevelObject();

        currentLevelObject.decreaseSnakeLength();
        int newSpeed = currentLevelObject.getSnakeSpeed()+20;
        if (newSpeed > GameplayConfig.SNAKE_SPEED_MAX) {
            newSpeed = GameplayConfig.SNAKE_SPEED_MAX;
        }
        currentLevelObject.setSnakeSpeed(newSpeed);
    }
}
