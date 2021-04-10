package Game.Gameplay;

import Game.Gameplay.Config.GameplayConfig;

import java.util.*;

public class Level3 extends Level {
    private static final Map MAP;
    private static final GameplayConfig GAMEPLAY_CONFIG;

    static {
        final int mapWidth = 30, mapHeight = 30;
        final boolean enableBorders = true;
        final String mapType = "ground";

        MAP = new Map(mapWidth, mapHeight, enableBorders, mapType);

        final int initialSnakeDirection = 2;
        final int snakeSpeed = 120;
        final int initialSnakeSize = 4;
        final int requiredSnakeSize = initialSnakeSize+11;

        final LinkedHashMap<String,Double> availableFood = new LinkedHashMap<>();
        availableFood.put("FruitOfLife",0.05);
        availableFood.put("Apple",0.41);
        availableFood.put("Berry",1.0);
        final int nrOfSimultaneousFruits = 4;
        final int nrOfSimultaneousObstacles = 5;

        final HashMap<String, Integer> timers = new HashMap<>();
        timers.put("RespawnFood",5000);
        timers.put("RespawnObstacles",3000);

        GAMEPLAY_CONFIG = new GameplayConfig(
                initialSnakeDirection, snakeSpeed,
                initialSnakeSize, requiredSnakeSize,
                availableFood, nrOfSimultaneousFruits, nrOfSimultaneousObstacles,
                timers
        );
    }

    public Level3() {
        super(GAMEPLAY_CONFIG, MAP);
    }
}


