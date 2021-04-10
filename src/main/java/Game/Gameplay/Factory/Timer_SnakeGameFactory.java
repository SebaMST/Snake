package Game.Gameplay.Factory;

import Game.Gameplay.Entities.Food;
import Game.Gameplay.Level;
import Game.Gameplay.Timers.Timer_SnakeGame;

import java.lang.reflect.InvocationTargetException;

public class Timer_SnakeGameFactory {
    private static final String PATH_PACKAGE = "Game.Gameplay.Timers.Timer_";

    public static Timer_SnakeGame create(String timerType, int delay, Level level) {
        try {
            Class<?> c = Class.forName(PATH_PACKAGE + timerType);
            if (c.getSuperclass().equals(Timer_SnakeGame.class)) {
                return (Timer_SnakeGame) c.getDeclaredConstructor(int.class, Level.class).newInstance(delay, level);
            } else {
                throw new IllegalArgumentException("FoodFactory: create, timerType must be a subclass of Food");
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Timer_SnakeGameFactory: create, timerType string contains the name of a Timer_SnakeGame whose class was not found: ");
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static String getPathPackage() {
        return PATH_PACKAGE;
    }
}
