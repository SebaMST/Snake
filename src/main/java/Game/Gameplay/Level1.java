package Game.Gameplay;

import Game.Gameplay.Config.GameplayConfig;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Level1 extends Level {
    private static final Map MAP;
    private static final GameplayConfig GAMEPLAY_CONFIG;

    static {
        final int mapWidth = 20, mapHeight = 20;
        final boolean enableBorders = false;
        final String mapType = "grass";

        MAP = new Map(mapWidth, mapHeight, enableBorders, mapType);

        final int initialSnakeDirection = 1;
        final int snakeSpeed = 120;
        final int initialSnakeSize = 5;
        final int requiredSnakeSize = initialSnakeSize+5;

        final LinkedHashMap<String,Double> availableFood = new LinkedHashMap<>();
        availableFood.put("Apple",1.0);
        final int nrOfSimultaneousFruits = 1;
        final int nrOfSimultaneousObstacles = 0;

        final HashMap<String, Integer> timers = new HashMap<>();

        GAMEPLAY_CONFIG = new GameplayConfig(
                initialSnakeDirection, snakeSpeed,
                initialSnakeSize, requiredSnakeSize,
                availableFood, nrOfSimultaneousFruits, nrOfSimultaneousObstacles,
                timers
        );
    }

    public Level1() {
        super(GAMEPLAY_CONFIG, MAP);
    }
}
