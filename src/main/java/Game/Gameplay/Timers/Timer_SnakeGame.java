package Game.Gameplay.Timers;

import Game.Gameplay.Level;

import javax.swing.*;

public abstract class Timer_SnakeGame extends Timer {
    private long lastStarted, lastStopped=0;

    private final int initialDelay;

    private void updateLastStarted() {
        lastStarted = System.currentTimeMillis();
    }
    private void updateLastStopped() {
        lastStopped = System.currentTimeMillis();
    }

    protected Timer_SnakeGame(int delay) {
        super(delay, null);
        this.initialDelay=delay;

        addActionListener(e -> {
            if(isRepeats()) {
                updateLastStarted();
                lastStopped=0;
            }
        });
    }

    @Override
    public void setDelay(int delay) {
        super.setDelay(delay);
    }

    @Override
    public void start() {
        updateLastStarted();
        super.start();
    }

    @Override
    public void stop() {
        updateLastStopped();
        super.stop();
    }

    public void pause(boolean paused) {
        if(paused) {
            stop();
        } else {
            start();
        }
    }

    public long getRemaining() {
        //System.out.println("current delay"+getDelay());
        long remaining = getDelay() - (System.currentTimeMillis() - lastStarted);
        if(remaining<1) {
            remaining=1;
        }
        return remaining;
    }

    public int getInitialDelay() {
        return initialDelay;
    }

}
