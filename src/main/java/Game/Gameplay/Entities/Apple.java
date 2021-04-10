package Game.Gameplay.Entities;

public class Apple extends Food {
    private static final String IMAGE_APPLE;

    static {
        IMAGE_APPLE = "apple";
    }

    public Apple(int x, int y) {
        super(IMAGE_APPLE,x,y);
    }

    @Override
    public void collide() {
        super.collide();
    }
}
