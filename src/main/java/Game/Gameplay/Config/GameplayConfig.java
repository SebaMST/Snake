package Game.Gameplay.Config;

import Game.GUI.GameUI;
import Game.Gameplay.Factory.FoodFactory;

import java.util.*;

public class GameplayConfig {
    public static final int SNAKE_DIRECTION_UP;
    public static final int SNAKE_DIRECTION_RIGHT;
    public static final int SNAKE_DIRECTION_DOWN;
    public static final int SNAKE_DIRECTION_LEFT;

    public static final int SNAKE_SPEED_MIN;
    public static final int SNAKE_SPEED_MAX;

    public static final int REQUIRED_SNAKE_SIZE_MIN;

    static {
        SNAKE_DIRECTION_UP=0;
        SNAKE_DIRECTION_RIGHT=1;
        SNAKE_DIRECTION_DOWN=2;
        SNAKE_DIRECTION_LEFT=3;

        SNAKE_SPEED_MIN = 40;
        SNAKE_SPEED_MAX = 280;

        REQUIRED_SNAKE_SIZE_MIN = 2;
    }

    private final int
            initialSnakeDirection, snakeSpeed,
            initialSnakeSize, requiredSnakeSize,
            nrOfSimultaneousFood, nrOfSimultaneousObstacles;
    private final LinkedHashMap<String,Double> availableFood;
    private final HashMap<String,Integer> timers;

    public GameplayConfig(
            int initialSnakeDirection, int snakeSpeed,
            int initialSnakeSize, int requiredSnakeSize,
            LinkedHashMap<String,Double> availableFood,
            int nrOfSimultaneousFood, int nrOfSimultaneousObstacles,
            HashMap<String,Integer> timers
    ) {
        if(initialSnakeDirection >= 0 && initialSnakeDirection <= 3) {
            this.initialSnakeDirection = initialSnakeDirection;
        } else {
            throw new IllegalArgumentException("GameplayConfig: constructor, initialSnakeDirection must be >= 0 and <=3 (initialSnakeDirection = " + initialSnakeDirection + ")");
        }

        if (snakeSpeed >= SNAKE_SPEED_MIN && snakeSpeed <= SNAKE_SPEED_MAX) {
            this.snakeSpeed = snakeSpeed;
        } else {
            throw new IllegalArgumentException("GameplayConfig: constructor, snakeSpeed must be >=" + SNAKE_SPEED_MIN + " and <= " + SNAKE_SPEED_MAX + " (snakeSpeed = "+ snakeSpeed +")");
        }

        if (initialSnakeSize > 0) {
            this.initialSnakeSize = initialSnakeSize;
        } else {
            throw new IllegalArgumentException("GameplayConfig: constructor, initialSnakeSize must be > 0 (initialSnakeSize = " + initialSnakeSize + ")");
        }

        if (requiredSnakeSize >= REQUIRED_SNAKE_SIZE_MIN) {
            this.requiredSnakeSize = requiredSnakeSize;
        } else {
            throw new IllegalArgumentException("GameplayConfig: constructor, requiredSnakeSize must be > 1 (requiredSnakeSize = " + requiredSnakeSize + ")");
        }

        for (Map.Entry<String,Double> foodEntry : availableFood.entrySet()) {
            try {
                Class.forName(FoodFactory.getPathPackage() + foodEntry.getKey());
            } catch (ClassNotFoundException cnfe) {
                throw new IllegalArgumentException("GameplayConfig: constructor, availableFood LinkedHashMap contains a key with the name of a class that was not found: "+foodEntry.getKey());
            }
        }
        Object[] probabilityThresholdsObjects = availableFood.values().toArray();
        Double[] probabilityThresholds = Arrays.copyOf(probabilityThresholdsObjects,probabilityThresholdsObjects.length,Double[].class);
        if(probabilityThresholds[probabilityThresholds.length-1]==1.0) {
            for (int i = 0; i < probabilityThresholds.length - 1; i++) {
                if (probabilityThresholds[i + 1] < probabilityThresholds[i]) {
                    throw new IllegalArgumentException("GameplayConfig: constructor, availableFood LinkedHashMap's sequence of entry values is not ordered ascending.");
                }
            }
        } else {
            throw new IllegalArgumentException("GameplayConfig: constructor, availableFood LinkedHashMap's last entry values is different than 1.");
        }
        this.availableFood=availableFood;

        if (nrOfSimultaneousFood > 0) {
            this.nrOfSimultaneousFood = nrOfSimultaneousFood;
        } else {
            throw new IllegalArgumentException("GameplayConfig: constructor, nrOfSimultaneousFood must be > 0 (nrOfSimultaneousFood = " + nrOfSimultaneousFood + ")");
        }

        if (nrOfSimultaneousObstacles >= 0) {
            this.nrOfSimultaneousObstacles = nrOfSimultaneousObstacles;
        } else {
            throw new IllegalArgumentException("GameplayConfig: constructor, nrOfSimultaneousObstacles must be null or > 0 (nrOfSimultaneousObstacles = " + nrOfSimultaneousObstacles + ")");
        }

        for(Map.Entry<String,Integer> timer : timers.entrySet()) {
            String timerName = timer.getKey();
            try {
                Class.forName("Game.Gameplay.Timers.Timer_"+timerName);
            } catch (ClassNotFoundException cnfe) {
                throw new IllegalArgumentException("GameplayConfig: constructor, timers HashMap contains a timer with a name whose class was not found: "+timerName);
            }
            Integer timerDelay = timer.getValue();
            if(timerDelay == null || timerDelay <= 0) {
                throw new IllegalArgumentException("GameplayConfig: constructor, timers HashMap contains a timer with a delay <= 0: "+timerName+" -> "+timerDelay);
            }
        }
        this.timers = timers;

    }

    //Getters
    public int getInitialSnakeDirection() {
        return initialSnakeDirection;
    }

    public int getInitialSnakeSize() {
        return initialSnakeSize;
    }

    public int getSnakeSpeed() {
        return snakeSpeed;
    }

    public int getRequiredSnakeSize() {
        return requiredSnakeSize;
    }

    public LinkedHashMap<String,Double> getAvailableFood() {
        return availableFood;
    }

    public int getNrOfSimultaneousFood() {
        return nrOfSimultaneousFood;
    }

    public int getNrOfSimultaneousObstacles() {
        return nrOfSimultaneousObstacles;
    }

    public HashMap<String, Integer> getTimers() {
        return timers;
    }
}
