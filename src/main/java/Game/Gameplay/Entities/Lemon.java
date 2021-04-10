package Game.Gameplay.Entities;

import Game.Gameplay.Config.GameplayConfig;
import Game.Gameplay.Level;
import Game.SnakeGame;

public class Lemon extends Food {
    private static final String IMAGE_LEMON;

    static {
        IMAGE_LEMON = "lemon";
    }

    public Lemon(int x, int y) {
        super(IMAGE_LEMON,x,y);
    }

    @Override
    public void collide() {
        super.collide();
        Level currentLevelObject = SnakeGame.getCurrentLevelObject();

        currentLevelObject.decreaseSnakeLength();
        int newSpeed = currentLevelObject.getSnakeSpeed()-20;
        if(newSpeed < GameplayConfig.SNAKE_SPEED_MIN) {
            newSpeed = GameplayConfig.SNAKE_SPEED_MIN;
        }
        currentLevelObject.setSnakeSpeed(newSpeed);
    }
}
