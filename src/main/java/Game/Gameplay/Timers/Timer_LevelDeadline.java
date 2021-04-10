package Game.Gameplay.Timers;

import Game.Gameplay.Level;

public class Timer_LevelDeadline extends Timer_SnakeGame {

    public Timer_LevelDeadline(int delay, final Level level) {
        super(delay);

        if(level == null) {
            throw new IllegalArgumentException("Timer_RespawnFood: constructor, level must be != null (level = "+level+")");
        }

        addActionListener(e -> {
            level.expire();
        });
        setRepeats(false);
    }
}
