package Game.Gameplay.Entities;

import Game.SnakeGame;

public class FruitOfLife extends Food {
    private static final String IMAGE_FRUITOFLIFE;

    static {
        IMAGE_FRUITOFLIFE = "fruitoflife";
    }

    public FruitOfLife(int x, int y) {
        super(IMAGE_FRUITOFLIFE,x,y);
    }

    @Override
    public void collide() {
        super.collide();
        int currentLivesCount = SnakeGame.getCountLives();
        if(currentLivesCount<5) {
            SnakeGame.setCountLives(currentLivesCount + 1);
        }
    }
}
