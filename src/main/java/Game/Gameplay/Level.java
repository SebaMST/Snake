package Game.Gameplay;

import Game.Gameplay.Config.GameplayConfig;
import Game.Gameplay.Entities.*;
import Game.Gameplay.Exceptions.Exception_LevelCompleted;
import Game.Gameplay.Exceptions.Exception_SnakeDead;
import Game.Gameplay.Factory.FoodFactory;
import Game.Gameplay.Factory.Timer_SnakeGameFactory;
import Game.Gameplay.Timers.Timer_SnakeGame;

import javax.swing.Timer;
import java.util.*;

public abstract class Level {

    public static final int[] SNAKE_STEP_X;
    public static final int[] SNAKE_STEP_Y;
    static {
        SNAKE_STEP_X = new int[]{0, 1, 0, -1};
        SNAKE_STEP_Y = new int[]{-1, 0, 1, 0};
    }
    private int snakeDirection;
    private int snakeSpeed;
    private final LinkedList<SnakeBody> snake;

    private final ArrayList<Food> food;
    private final ArrayList<Obstacle> obstacles;

    private final GameplayConfig gameplayConfig;
    private final Map map;

    private final HashMap<String,Timer_SnakeGame> timers;
    private boolean expired;

    private String helpMessage;

    protected Level(GameplayConfig gameplayConfig, Map map) {
        this.gameplayConfig = gameplayConfig;
        this.map = map;

        snake = new LinkedList<>();
        food = new ArrayList<>();
        obstacles = new ArrayList<>();

        helpMessage = buildHelpMessage();

        timers = new HashMap<>();
        for(java.util.Map.Entry<String, Integer> timerEntry : gameplayConfig.getTimers().entrySet()) {
            String timerType = timerEntry.getKey();
            timers.put(timerType, Timer_SnakeGameFactory.create(timerType,timerEntry.getValue(),this));
        }
    }

    public void tick() throws Exception_LevelCompleted, Exception_SnakeDead {
        if(gameplayConfig.getRequiredSnakeSize()==snake.size()) {
            throw new Exception_LevelCompleted(getClass().getSimpleName().substring(5));
        } else if(expired) {
            throw new Exception_SnakeDead("it remained out of time");
        }

        moveSnake();
    }

    public void setPaused(boolean paused) {
        for(Timer_SnakeGame t: timers.values() ) {
            t.pause(paused);
        }
    }

    public void reset() {
        map.reset();

        this.snakeDirection=gameplayConfig.getInitialSnakeDirection();
        this.snakeSpeed=gameplayConfig.getSnakeSpeed();
        resetSnake();

        resetFood();
        resetObstacles();

        resetTimers();
    }

    private void resetSnake() {
        snake.clear();
        int initialSnakeSize = gameplayConfig.getInitialSnakeSize();
        int initialX = map.getWidth()/2-SNAKE_STEP_X[snakeDirection]*(initialSnakeSize-1);
        int initialY = map.getHeight()/2-SNAKE_STEP_Y[snakeDirection]*(initialSnakeSize-1);

        SnakeHead snakeHead = new SnakeHead(initialX, initialY, snakeDirection);
        snake.add(snakeHead);
        map.setTiledEntity(initialX,initialY,snakeHead);

        for(int i=0; i<initialSnakeSize-1;i++) {
            increaseSnakeLength();
        }
        System.out.println("");
    }

    public void resetObstacles() {
        for(Obstacle o : obstacles) {
            map.unsetTiledEntity(o.getX(),o.getY());
        }
        obstacles.clear();
        //generating obstacles at random position
        for(int i=0;i<gameplayConfig.getNrOfSimultaneousObstacles();i++) {
            generateTiledEntityRandomCoords("Obstacle");
        }
    }

    public void resetFood() {
        for(Food f : food) {
            map.unsetTiledEntity(f.getX(), f.getY());
        }
        food.clear();

        for(int i=0;i<gameplayConfig.getNrOfSimultaneousFood();i++) {
            generateRandomFoodRandomCoords();
        }
    }

    public void removeFood(Food f) {
        food.remove(f);
    }

    public void removeObstacle(Obstacle o) {
        obstacles.remove(o);
    }

    public ArrayList<Food> getFood() {
        return food;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public synchronized void generateTiledEntityRandomCoords(String tiledEntityType) {
        boolean isObstacle = tiledEntityType.equals("Obstacle");
        boolean isAvailableFood = getGameplayConfig().getAvailableFood().containsKey(tiledEntityType);

        if(!isObstacle && !isAvailableFood) {
            throw new IllegalArgumentException("Level: generateTiledEntityRandomCoords, tiledEntityType is the name of an entity that does not exist or is not allowed: "+tiledEntityType);
        }

        Coords freeCoords = map.generateRandomFreeCoords();
        int freeCoordsX = freeCoords.getX(), freeCoordsY = freeCoords.getY();

        TiledEntity tiledEntity;

        if(isObstacle) {
            tiledEntity = new Obstacle(freeCoordsX,freeCoordsY);
            obstacles.add((Obstacle)tiledEntity);
        } else {
            tiledEntity = FoodFactory.create(tiledEntityType,freeCoordsX,freeCoordsY);
            food.add((Food)tiledEntity);
        }

        map.setTiledEntity(freeCoordsX,freeCoordsY,tiledEntity);
    }

    public void generateRandomFoodRandomCoords() {
        Random r = new Random(System.nanoTime());
        LinkedHashMap<String,Double> availableFood = gameplayConfig.getAvailableFood();

        double randomValue10 = r.nextDouble();

        for(java.util.Map.Entry<String,Double> availableFoodEntry : availableFood.entrySet()) {
            if(randomValue10 < availableFoodEntry.getValue()) {
                generateTiledEntityRandomCoords(availableFoodEntry.getKey());
                break;
            }
        }
    }

    private void resetTimers() {
        for(Timer timer : timers.values()) {
            timer.restart();
        }
        expired = false;
    }

    private Coords getNextSnakeCoords() {
        SnakeHead oldHead = getSnakeHead();
        int oldHeadX = oldHead.getX(), oldHeadY = oldHead.getY();

        int newHeadX = oldHeadX+SNAKE_STEP_X[snakeDirection];
        int newHeadY = oldHeadY+SNAKE_STEP_Y[snakeDirection];

        if(map.getEnabledBorders()) {
            if(map.invalidCoords(newHeadX,newHeadY)) {
                throw new Exception_SnakeDead("it collided with the map border");
            }
        } else {
            Coords normalized = map.normalizeCoords(newHeadX, newHeadY);
            newHeadX = normalized.getX();
            newHeadY = normalized.getY();
        }

        return new Coords(newHeadX, newHeadY);
    }

    public void moveSnake() throws Exception_SnakeDead {
        Coords nextCoords = getNextSnakeCoords();
        int nextX = nextCoords.getX(), nextY = nextCoords.getY();

        TiledEntity tiledEntity = map.getTiledEntity(nextX,nextY);
        if(tiledEntity!=null) {
            tiledEntity.collide();
        } else {
            //only move snake
            increaseSnakeLength();
            decreaseSnakeLength();
        }
    }

    public void increaseSnakeLength() {
        SnakeHead oldHead = getSnakeHead();
        int oldHeadX = oldHead.getX(), oldHeadY = oldHead.getY();
        Coords nextCoords = getNextSnakeCoords();
        int newHeadX = nextCoords.getX(), newHeadY = nextCoords.getY();

        SnakeHead snakeHead = new SnakeHead(newHeadX, newHeadY, snakeDirection);
        snake.addFirst(snakeHead);
        map.setTiledEntity(newHeadX, newHeadY, snakeHead);

        SnakeBody snakeBody = new SnakeBody(oldHeadX, oldHeadY);
        snake.set(1, snakeBody);
        map.setTiledEntity(oldHeadX, oldHeadY, snakeBody);
    }

    public void decreaseSnakeLength() throws Exception_SnakeDead {
        if(snake.size()<=1) {
            throw new Exception_SnakeDead("it lost its head");
        }
        SnakeBody snakeTailTip = snake.removeLast();
        map.unsetTiledEntity(snakeTailTip.getX(), snakeTailTip.getY());
    }

    public void expire() {
        expired = true;
    }

    public void setSnakeDirection(int snakeDirection) {
        if(snakeDirection< GameplayConfig.SNAKE_DIRECTION_UP || snakeDirection>GameplayConfig.SNAKE_DIRECTION_LEFT) {
            throw new IllegalArgumentException("Snake:, setSnakeDirection snakeDirection must be >=0 and <=3 (snakeDirection = "+snakeDirection+")");
        }
        this.snakeDirection = snakeDirection;
    }

    public int getSnakeDirection() {
        return snakeDirection;
    }

    public void setSnakeSpeed(int snakeSpeed) {
        if (snakeSpeed < GameplayConfig.SNAKE_SPEED_MIN && snakeSpeed > GameplayConfig.SNAKE_SPEED_MAX) {
            throw new IllegalArgumentException("GameplayConfig: constructor, snakeSpeed must be >=" + GameplayConfig.SNAKE_SPEED_MIN + " and <= " + GameplayConfig.SNAKE_SPEED_MAX + " (snakeSpeed = "+ snakeSpeed +")");
        }
        this.snakeSpeed=snakeSpeed;
    }

    public int getSnakeSpeed() {
        return snakeSpeed;
    }

    public synchronized Map getMap() {
        return map;
    }

    public GameplayConfig getGameplayConfig() {
        return gameplayConfig;
    }

    public LinkedList<SnakeBody> getSnake() {
        return snake;
    }

    public SnakeHead getSnakeHead() {
            return (SnakeHead)snake.getFirst();
    }

    public HashMap<String,Timer_SnakeGame> getTimers() {
        return timers;
    }

    private String buildHelpMessage() {
        String className = getClass().getSimpleName();

        StringBuilder fruits = new StringBuilder();
        for(String s : gameplayConfig.getAvailableFood().keySet()) {
            if(!s.equals("FruitOfLife")) {
                fruits.append(s).append(" ");
            }
        }

        String border = getMap().getEnabledBorders() ? "<br>\t-Avoid the map <font color='red'>border</font> so you don't die!" : "";
        String obstacles = getGameplayConfig().getNrOfSimultaneousObstacles() > 0 ? "<br>\t-Avoid the <font color='red'>obstacles</font> so you don't die!" : "";

        return "" +
                "<html>" +
                "   <body>" +
                "       <div width='300px' align='left'>" +
                "           Welcome to <font color='blue'>Level "+ className.substring(5) + "</font> of this game!" +
                "           <br><br>To do:" +
                "           <div margin-left='20px'>" +
                "              <br>-Have the snake reach a size of <font color='green'>" + getGameplayConfig().getRequiredSnakeSize() + "</font>" +
                "              <br>-Meet the consequences of eating any of these fruits:" +
                "              <br><font color='green'>" + fruits + "</font>" +
                                border +
                                obstacles +
                "              <br>-Be careful for the on-going <font color='red'>timers</font>, if there are any!" +
                "              <br>The map in this level is type <font color='purple'>" + getMap().getMapType() +"</font>."+
                "              <br><br>Good Luck!" +
                "           </div>" +
                "       </div>" +
                "   </body>" +
                "</html>";
    }

    public String getHelpMessage() {
        return helpMessage;
    }
}