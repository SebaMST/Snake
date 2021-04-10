package Game.Gameplay;

import Game.Gameplay.Config.GameplayConfig;

import java.util.*;

public class Level5 extends Level {
    private static final Map MAP;
    private static final GameplayConfig GAMEPLAY_CONFIG;

    static {
        final int mapWidth = 40, mapHeight = 20;
        final boolean enableBorders = false;
        final String mapType = "snow";

        MAP = new Map(mapWidth, mapHeight, enableBorders, mapType);

        final int initialSnakeDirection = 0;
        final int snakeSpeed = 80;
        final int initialSnakeSize = 3;
        final int requiredSnakeSize = initialSnakeSize+19;

        final LinkedHashMap<String,Double> availableFood = new LinkedHashMap<>();
        availableFood.put("FruitOfLife",0.03);
        availableFood.put("Lemon",0.15);
        availableFood.put("Watermelon",0.3);
        availableFood.put("Mushroom",0.5);
        availableFood.put("Apple",0.75);
        availableFood.put("Berry",1.0);
        final int nrOfSimultaneousFruits = 7;
        final int nrOfSimultaneousObstacles = 10;

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

    public Level5() {
        super(GAMEPLAY_CONFIG, MAP);
    }
}
