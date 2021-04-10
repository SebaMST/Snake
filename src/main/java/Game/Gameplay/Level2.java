package Game.Gameplay;

import Game.Gameplay.Config.GameplayConfig;

import java.util.*;

public class Level2 extends Level {
    private static final Map MAP;
    private static final GameplayConfig GAMEPLAY_CONFIG;

    static {
        final int mapWidth = 50, mapHeight = 30;
        final boolean enableBorders = true;
        final String mapType = "desert";

        MAP = new Map(mapWidth, mapHeight, enableBorders, mapType);

        final int initialSnakeDirection = 3;
        final int snakeSpeed = 120;
        final int initialSnakeSize = 4;
        final int requiredSnakeSize = initialSnakeSize+8;

        final LinkedHashMap<String,Double> availableFood = new LinkedHashMap<>();
        availableFood.put("Apple",0.21);
        availableFood.put("Berry",1.0);
        final int nrOfSimultaneousFruits = 30;
        final int nrOfSimultaneousObstacles = 0;

        final HashMap<String, Integer> timers = new HashMap<>();
        timers.put("RespawnFood",5000);

        GAMEPLAY_CONFIG = new GameplayConfig(
                initialSnakeDirection, snakeSpeed,
                initialSnakeSize, requiredSnakeSize,
                availableFood, nrOfSimultaneousFruits, nrOfSimultaneousObstacles,
                timers
        );
    }

    public Level2() {
        super(GAMEPLAY_CONFIG, MAP);
    }
}


