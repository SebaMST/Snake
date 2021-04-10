package Game.Gameplay.Timers;

import Game.Gameplay.Entities.*;
import Game.Gameplay.Entities.Food;
import Game.Gameplay.Level;
import Game.Gameplay.Map;

import java.util.ArrayList;

public class Timer_RespawnFood extends Timer_SnakeGame {

    public Timer_RespawnFood(int delay, final Level level) {
        super(delay);

        if(level == null) {
            throw new IllegalArgumentException("Timer_RespawnFood: constructor, level must be != null (level = "+level+")");
        }

        addActionListener(e -> {
            level.resetFood();
        });
    }
}
