package Game.Gameplay.Timers;

import Game.Gameplay.Entities.*;
import Game.Gameplay.Level;
import Game.Gameplay.Map;

public class Timer_RespawnObstacles extends Timer_SnakeGame {

    public Timer_RespawnObstacles(int delay, final Level level) {
        super(delay);

        if(level == null) {
            throw new IllegalArgumentException("Timer_RespawnObstacles: constructor, level must be != null (level = "+level+")");
        }

        addActionListener(e -> {
            level.resetObstacles();
        });
    }
}
