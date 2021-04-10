package Game.Gameplay;

import Game.Gameplay.Config.GameplayConfig;

import java.util.*;

public class Level4 extends Level {
    private static final Map MAP;
    private static final GameplayConfig GAMEPLAY_CONFIG;

    static {
        final int mapWidth = 25, mapHeight = 30;
        final boolean enableBorders = false;
        final String mapType = "water";

        MAP = new Map(mapWidth, mapHeight, enableBorders, mapType);

        final int initialSnakeDirection = 2;
        final int snakeSpeed = 80;
        final int initialSnakeSize = 2;
        final int requiredSnakeSize = initialSnakeSize+13;

        final LinkedHashMap<String,Double> availableFood = new LinkedHashMap<>();
        availableFood.put("FruitOfLife",0.02);
        availableFood.put("Lemon",0.17);
        availableFood.put("Watermelon",0.34);
        availableFood.put("Apple",0.7);
        availableFood.put("Berry",1.0);
        final int nrOfSimultaneousFruits = 5;
        final int nrOfSimultaneousObstacles = 5;

        final HashMap<String, Integer> timers = new HashMap<>();
        timers.put("RespawnFood",4000);
        timers.put("RespawnObstacles",3000);
        timers.put("LevelDeadline",(requiredSnakeSize-initialSnakeSize)*5000);

        GAMEPLAY_CONFIG = new GameplayConfig(
                initialSnakeDirection, snakeSpeed,
                initialSnakeSize, requiredSnakeSize,
                availableFood, nrOfSimultaneousFruits, nrOfSimultaneousObstacles,
                timers
        );
    }

    public Level4() {
        super(GAMEPLAY_CONFIG, MAP);
    }
}


