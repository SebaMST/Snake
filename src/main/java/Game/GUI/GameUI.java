package Game.GUI;

import Game.Gameplay.Config.GameplayConfig;
import Game.Gameplay.Level;
import Game.Gameplay.Timers.Timer_LevelDeadline;
import Game.Gameplay.Timers.Timer_SnakeGame;
import Game.SnakeGame;
import Game.Gameplay.Map;
import Game.Gameplay.Entities.TiledEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GameUI {
    public static final int TILE_SIZE_PX, BACKGROUND_TILE_SIZE_PX;
    private static final JPanel GAME_JPANEL;
    public static final int STATUS_HEIGHT_PX;
    public static final int STATUS_FONT_SIZE_PX;
    static {
        TILE_SIZE_PX = 30;
        BACKGROUND_TILE_SIZE_PX = 50;
        STATUS_HEIGHT_PX = (int)(TILE_SIZE_PX*1.7);
        STATUS_FONT_SIZE_PX = (int)(0.9*TILE_SIZE_PX);

        GAME_JPANEL = new JPanel() {
            public void paint(Graphics g) {
                g.clearRect(0, 0, gameFrame.getWidth(), gameFrame.getHeight());
                g.setColor(Color.BLACK);
                g.fillRect(0,0,gameFrame.getWidth(),STATUS_HEIGHT_PX-4);
                g.setColor(Color.white);
                g.fillRect(0,STATUS_HEIGHT_PX-4,gameFrame.getWidth(),4);

                Map map = SnakeGame.getCurrentLevelObject().getMap();
                Image image = ImageLoader.getImageByName(map.getMapType());
                //paint background
                for (int i = 0; i <= gameFrame.getHeight() / BACKGROUND_TILE_SIZE_PX; i++) {
                    for (int j = 0; j <= gameFrame.getWidth() / BACKGROUND_TILE_SIZE_PX; j++) {
                        g.drawImage(image, j * BACKGROUND_TILE_SIZE_PX, STATUS_HEIGHT_PX+i * BACKGROUND_TILE_SIZE_PX, BACKGROUND_TILE_SIZE_PX, BACKGROUND_TILE_SIZE_PX, this);
                    }
                }
                ArrayList<TiledEntity> tiledEntitiesToPaint = new ArrayList<>();
                try {
                    tiledEntitiesToPaint.addAll(SnakeGame.getCurrentLevelObject().getSnake());
                    tiledEntitiesToPaint.addAll(SnakeGame.getCurrentLevelObject().getFood());
                    tiledEntitiesToPaint.addAll(SnakeGame.getCurrentLevelObject().getObstacles());
                } catch(ArrayIndexOutOfBoundsException ignored) {
                }

                for(TiledEntity tiledEntity : tiledEntitiesToPaint) {
                    try {
                        image = ImageLoader.getImageByName(tiledEntity.getImage());
                        g.drawImage(image,tiledEntity.getX()*TILE_SIZE_PX,STATUS_HEIGHT_PX+tiledEntity.getY()*TILE_SIZE_PX,TILE_SIZE_PX,TILE_SIZE_PX,this);
                    } catch(NullPointerException ignored) {
                    }
                }

                g.setFont(new Font(Font.DIALOG, Font.BOLD, STATUS_FONT_SIZE_PX));

                g.setColor(Color.pink);
                g.drawString(SnakeGame.getCountLives()+"x ",(int)(TILE_SIZE_PX*0.5),(int)(TILE_SIZE_PX*1.05));
                image = ImageLoader.getImageByName("fruitoflife");
                g.drawImage(image,(int)(TILE_SIZE_PX*1.6),(int)(TILE_SIZE_PX*0.24),TILE_SIZE_PX,TILE_SIZE_PX,this);

                int size = SnakeGame.getCurrentLevelObject().getSnake().size();
                g.setColor(size < 5 ? Color.red : Color.green);
                g.drawString("Size: "+size,(int)(TILE_SIZE_PX*3.5),(int)(TILE_SIZE_PX*1.05));

                g.setColor(Color.cyan);
                int currentSpeed = SnakeGame.getCurrentLevelObject().getSnakeSpeed();
                int initialSpeed = SnakeGame.getCurrentLevelObject().getGameplayConfig().getSnakeSpeed();
                int percent = (int)(((double)currentSpeed/initialSpeed - 1.) * 100);
                String displaySpeed = percent==0 ? "" : ("SPD: "+ (percent<0 ? "" : "+") + percent +"%");
                g.drawString(displaySpeed,(int)(TILE_SIZE_PX*7.5),(int)(TILE_SIZE_PX*1.05));

                if(invertedKeys) {
                    g.setColor(Color.magenta);
                    g.drawString("Dazed", (int) (TILE_SIZE_PX * 13.6), (int) (TILE_SIZE_PX * 1.05));
                }

                for (Timer_SnakeGame t : SnakeGame.getCurrentLevelObject().getTimers().values()) {
                    if(t instanceof Timer_LevelDeadline) {
                        double remaining = (double)(t.getRemaining()/100)/10;
                        g.setColor(remaining <= 10. ? Color.red : Color.yellow);
                        g.drawString("Deadline: " + remaining, map.getWidth()*TILE_SIZE_PX-(int)(TILE_SIZE_PX*7.0),(int)(TILE_SIZE_PX*1.05));
                    }
                }

                g.setFont(new Font(Font.DIALOG, Font.BOLD, 3*STATUS_FONT_SIZE_PX));

                if(SnakeGame.getPaused()) {
                    g.setColor(Color.WHITE);
                    g.drawString("PAUSED", (int)(map.getWidth()*TILE_SIZE_PX*0.5-TILE_SIZE_PX*5.2), (int)(map.getHeight()*TILE_SIZE_PX*0.45)+STATUS_HEIGHT_PX+4 + (int) (TILE_SIZE_PX * 1.05));
                }
            }
        };
    }

    private static JFrame gameFrame;

    public static void create() {
        GAME_JPANEL.setFocusable(true);
        GAME_JPANEL.addKeyListener(GameUI.KEY_LISTENER);

        gameFrame = new JFrame();
        gameFrame.setJMenuBar(GameUI.createMenuBar());
        gameFrame.add(GAME_JPANEL);
        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gameFrame.setVisible(true);

        GameUI.startRunnableRepaint();
    }

    public static void updateFrame() {
        Map mc = SnakeGame.getCurrentLevelObject().getMap();
        int windowWidth = mc.getWidth()*TILE_SIZE_PX;
        int windowHeight = mc.getHeight()*TILE_SIZE_PX;
        gameFrame.setPreferredSize(new Dimension(windowWidth,windowHeight));
        gameFrame.pack();
        int diffWidth = windowWidth-GAME_JPANEL.getSize().width;
        int diffHeight = windowHeight-GAME_JPANEL.getSize().height;
        gameFrame.setPreferredSize(new Dimension(windowWidth+diffWidth,windowHeight+diffHeight+STATUS_HEIGHT_PX));
        gameFrame.pack();

        gameFrame.setLocationRelativeTo(null);
        GameUI.setFrameTitle("SNAKE - LEVEL " + SnakeGame.getCurrentLevel());
    }

    public static JMenuBar createMenuBar() {
        JMenuBar gameMenuBar = new JMenuBar();

        JMenu game = new JMenu("Game");

        JMenuItem newgame = new JMenuItem("New Game");
        newgame.addActionListener(e -> {
            SnakeGame.reset();
        });
        JMenuItem gotolevel = new JMenuItem("Go to Level");
        gotolevel.addActionListener(e -> {
            SnakeGame.setPaused(true);
            String[] options = new String[SnakeGame.COUNT_LEVELS];
            for(int i=1;i<=SnakeGame.COUNT_LEVELS;i++) {
                options[i-1] = "Level "+i;
            }
            int levelOption = JOptionPane.showOptionDialog(null,"Go to level...","Please select the level you would like to play!",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
            if(levelOption>=0 && levelOption<options.length) {
                SnakeGame.setCurrentLevel(levelOption + 1);
                SnakeGame.setPaused(false);
            } else {
                System.exit(0);
            }
        });
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> {
            System.exit(0);
        });

        game.add(newgame);
        game.add(gotolevel);
        game.addSeparator();
        game.add(exit);

        JMenu help = new JMenu("Help");

        JMenuItem instruction = new JMenuItem("Instructions");
        instruction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = "Instructions";
                GameUI.showMessageDialog(title,SnakeGame.getCurrentLevelObject().getHelpMessage());
            }
        });

        JMenuItem creator = new JMenuItem("About");
        creator.addActionListener(e -> {
            String title = "About";
            String message = "" +
                    "<html>" +
                    "   <body>" +
                    "       <div width='300px' align='center'>" +
                    "           This snake game was developed by <font color='blue'>Ștefănigă Beniamin</font>" +
                    "           <br>Student at Computer & Information Technology department of" +
                    "           <br><br>'Politehnica' University of Timișoara. 2021" +
                    "       </div>" +
                    "   </body>" +
                    "</html>";
            GameUI.showMessageDialog(title,message);
        });

        help.add(instruction);
        help.add(creator);

        gameMenuBar.add(game);
        gameMenuBar.add(help);

        return gameMenuBar;
    }

    public static void setFrameTitle(String title) {
        gameFrame.setTitle(title);
    }

    public static void showMessageDialog(String title, String message) {
        SnakeGame.setPaused(true);
        JLabel messageLabel = new JLabel(message);
        JOptionPane.showMessageDialog(null, messageLabel,title,JOptionPane.PLAIN_MESSAGE);
        SnakeGame.setPaused(false);
    }

    public static int showConfirmationDialog(String title, String message) {
        SnakeGame.setPaused(true);
        JLabel messageLabel = new JLabel(message);
        int result = JOptionPane.showConfirmDialog(null,messageLabel,title,JOptionPane.YES_NO_OPTION);
        SnakeGame.setPaused(false);
        return result;
    }

    //Threading
    private static final Runnable RUNNABLE_REPAINT;
    private static boolean runningRepaint;
    private static Thread thread;

    static {
        runningRepaint = false;
        RUNNABLE_REPAINT = new Runnable() {
            @Override
            public void run() {
                while (runningRepaint) {
                    GameUI.GAME_JPANEL.repaint();
                }
            }
        };
    }

    public static void startRunnableRepaint() {
        if (!runningRepaint) {
            runningRepaint = true;
            thread = new Thread(RUNNABLE_REPAINT);
            thread.start();
        }
    }

    public static void stopRunnableRepaint() {
        if (runningRepaint) {
            runningRepaint = false;
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Delay

    public static void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    //Key Control
    private static final int[] MOVEMENT_KEYS;
    private static boolean invertedKeys;

    static {
        MOVEMENT_KEYS = new int[4];
        invertedKeys = false;
    }

    public static void resetMovementKeys() {
        MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_UP]=KeyEvent.VK_UP;
        MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_RIGHT] = KeyEvent.VK_RIGHT;
        MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_DOWN] = KeyEvent.VK_DOWN;
        MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_LEFT] = KeyEvent.VK_LEFT;
        invertedKeys=false;
    }

    public static void invertMovementKeys() {
        int aux = MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_UP];
        MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_UP] = MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_DOWN];
        MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_DOWN] = aux;
        aux = MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_RIGHT];
        MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_RIGHT] = MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_LEFT];
        MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_LEFT] = aux;
        invertedKeys = !invertedKeys;
    }

    public static boolean getInvertedKeys() {
        return invertedKeys;
    }

    private static final KeyListener KEY_LISTENER = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            boolean paused = SnakeGame.getPaused();
            if(paused) {
                if(key == KeyEvent.VK_P) {
                    SnakeGame.setPaused(false);
                }
            } else {
                Level currentLevel = SnakeGame.getCurrentLevelObject();
                int direction = currentLevel.getSnakeDirection();

                if (key == MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_UP]) {
                    if(direction!=GameplayConfig.SNAKE_DIRECTION_UP && direction!=GameplayConfig.SNAKE_DIRECTION_DOWN) {
                        currentLevel.setSnakeDirection(GameplayConfig.SNAKE_DIRECTION_UP);
                    }
                } else if (key == MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_RIGHT]) {
                    if(direction!=GameplayConfig.SNAKE_DIRECTION_RIGHT && direction!=GameplayConfig.SNAKE_DIRECTION_LEFT) {
                        currentLevel.setSnakeDirection(GameplayConfig.SNAKE_DIRECTION_RIGHT);
                    }
                } else if (key == MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_DOWN]) {
                    if(direction!=GameplayConfig.SNAKE_DIRECTION_DOWN && direction!=GameplayConfig.SNAKE_DIRECTION_UP) {
                        currentLevel.setSnakeDirection(GameplayConfig.SNAKE_DIRECTION_DOWN);
                    }
                } else if (key == MOVEMENT_KEYS[GameplayConfig.SNAKE_DIRECTION_LEFT]) {
                    if(direction!=GameplayConfig.SNAKE_DIRECTION_LEFT && direction!=GameplayConfig.SNAKE_DIRECTION_RIGHT) {
                        currentLevel.setSnakeDirection(GameplayConfig.SNAKE_DIRECTION_LEFT);
                    }
                } else if (key == KeyEvent.VK_P) {
                    SnakeGame.setPaused(true);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    };
}
