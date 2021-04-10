package Game.Gameplay.Factory;

import Game.Gameplay.Entities.Food;

public class FoodFactory {
    private static final String PATH_PACKAGE = "Game.Gameplay.Entities.";

    public static Food create(String foodType, int x, int y) {
        try {
            Class<?> c = Class.forName(PATH_PACKAGE + foodType);
            if(c.getSuperclass().equals(Food.class)) {
                return (Food) c.getDeclaredConstructor(int.class, int.class).newInstance(x,y);
            } else {
                throw new IllegalArgumentException("FoodFactory: create, foodType must be a subclass of Food");
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("FoodFactory: create, foodType string contains the name of a Food whose class was not found: ");
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static String getPathPackage() {
        return PATH_PACKAGE;
    }
}
