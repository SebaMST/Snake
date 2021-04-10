package Game.Gameplay.Entities;

import Game.Gameplay.Level;
import Game.SnakeGame;

public abstract class Food extends TiledEntity {

    protected Food(String image, int x, int y) {
        super(image, x, y);
    }

    @Override
    public void collide() {
        Level currentLevelObject = SnakeGame.getCurrentLevelObject();
        currentLevelObject.removeFood(this);
        currentLevelObject.generateRandomFoodRandomCoords();

        currentLevelObject.increaseSnakeLength();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Food) {
            return ((Food)obj).getX()==getX() && ((Food)obj).getY()==getY();
        }
        return false;
    }
}
