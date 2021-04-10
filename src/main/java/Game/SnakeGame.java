package Game;

import Game.GUI.GameUI;
import Game.Gameplay.*;
import Game.Gameplay.Config.GameplayConfig;
import Game.Gameplay.Exceptions.Exception_GameCompleted;
import Game.Gameplay.Exceptions.Exception_LevelCompleted;
import Game.Gameplay.Exceptions.Exception_SnakeDead;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SnakeGame {

    private static final ArrayList<Level> SNAKE_LEVELS;
    public static final int COUNT_LEVELS;

    static {
        SNAKE_LEVELS = new ArrayList<>(Arrays.asList(new Level1(), new Level2(), new Level3(), new Level4(), new Level5()));
        COUNT_LEVELS = SNAKE_LEVELS.size();
    }

    private static Level currentLevelObject;
    private static int currentLevel;

    public static int countLives;

    private static boolean paused;

    //metoda principala
    public static void main(String[] args) {
        GameUI.create();

        reset();
        play();
    }

    public static void play() {
        try {
            while (true) {
                if (!paused) {
                    try {
                        GameUI.delay(10000 / (currentLevelObject.getSnakeSpeed()==0 ? 10000 : currentLevelObject.getSnakeSpeed()));
                        currentLevelObject.tick();
                    } catch (Exception_LevelCompleted e) {
                        if (currentLevel == COUNT_LEVELS) {
                            throw new Exception_GameCompleted();
                        } else {
                            int result = GameUI.showConfirmationDialog("Level Completed", e.getMessage());
                            if (result == JOptionPane.YES_OPTION) {
                                setCurrentLevel(currentLevel + 1);
                            } else {
                                System.exit(0);
                            }
                        }
                    } catch (Exception_SnakeDead e) {
                        int result = GameUI.showConfirmationDialog("Game Over", e.getMessage());
                        if (result == JOptionPane.YES_OPTION) {
                            if(countLives<=1) {
                                reset();
                            } else {
                                setCountLives(countLives-1);
                                setCurrentLevel(currentLevel);
                            }
                        } else if (result == JOptionPane.NO_OPTION) {
                            System.exit(0);
                        }
                    }
                } else {
                    System.out.print("");
                }
            }
        } catch (Exception_GameCompleted e) {
            int result = GameUI.showConfirmationDialog("Game Completed",e.getMessage());
            if(result == JOptionPane.YES_OPTION) {
                    reset();
                    play();
            } else if (result == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
        }
    }

    public static void reset() {
        GameUI.showMessageDialog(
                "Welcome",
                "<html>" +
                        "   <body>" +
                        "       <div width='300px' align='left'>" +
                        "           <font color='blue'>Welcome to Snake!</font>" +
                        "           <br><br>You have a total of <font color='green'>3 lives</font>, so play carefully!" +
                        "           <br>Please read the instructions if you need any help!" +
                        "           <br><br>Good Luck!" +
                        "       <div>" +
                        "   </body>" +
                        "</html>"
        );
        setCountLives(3);
        setCurrentLevel(1);
        setPaused(false);
    }

    public static void setPaused(boolean paused) {
        SnakeGame.paused=paused;
        if(currentLevelObject!=null) {
            currentLevelObject.setPaused(paused);
        }
        if(paused) {
            GameUI.stopRunnableRepaint();
        } else {
            GameUI.startRunnableRepaint();
        }
    }

    public static void setCurrentLevel(int currentLevel) {
        SnakeGame.currentLevel = currentLevel;
        SnakeGame.currentLevelObject = SNAKE_LEVELS.get(currentLevel-1);
        GameUI.updateFrame();
        GameUI.showMessageDialog("Level "+currentLevel, currentLevelObject.getHelpMessage());
        SnakeGame.currentLevelObject.reset();
        GameUI.resetMovementKeys();
    }

    public static int getCurrentLevel() {
        return SnakeGame.currentLevel;
    }

    public static Level getCurrentLevelObject() {
        return SnakeGame.currentLevelObject;
    }

    public static boolean getPaused() {
        return SnakeGame.paused;
    }

    public static void setCountLives(int countLives) {
        SnakeGame.countLives = countLives;
    }

    public static int getCountLives() {
        return countLives;
    }
}
