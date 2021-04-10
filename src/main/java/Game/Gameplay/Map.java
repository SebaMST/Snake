package Game.Gameplay;

import Game.GUI.ImageLoader;
import Game.Gameplay.Entities.*;

import java.util.Random;

public class Map {

    public static final int WIDTH_MIN, WIDTH_MAX, HEIGHT_MIN, HEIGHT_MAX;
    static {
        WIDTH_MIN = 10;
        WIDTH_MAX = 100;
        HEIGHT_MIN = 10;
        HEIGHT_MAX = 70;
    }

    private final int width, height;
    private final boolean initialEnabledBorders;
    private boolean enabledBorders;
    private final String mapType;

    private TiledEntity[][] tiledEntitiesMatrix;

    public Map(int width, int height, boolean enabledBorders, String mapType) {
        if(width < WIDTH_MIN || width > WIDTH_MAX || height < HEIGHT_MIN || height > HEIGHT_MAX ) {
            throw new IllegalArgumentException("Invalid Width x Height: "+width+"x"+height);
        }
        this.width=width;
        this.height=height;
        initialEnabledBorders=enabledBorders;
        this.enabledBorders=initialEnabledBorders;
        ImageLoader.getImageByName(mapType);
        this.mapType=mapType;
    }

    public void reset() {
        tiledEntitiesMatrix = new TiledEntity[height][width];
        enabledBorders = initialEnabledBorders;
    }

    public synchronized void setTiledEntity(int x, int y, TiledEntity tiledEntity) {
        if(invalidCoords(x, y)) {
            throw new IllegalArgumentException("Invalid (x,y) = ("+x+","+y+")");
        }
        tiledEntitiesMatrix[y][x] = tiledEntity;
    }

    public synchronized void unsetTiledEntity(int x, int y) {
        setTiledEntity(x,y,null);
    }

    public synchronized TiledEntity getTiledEntity(int x, int y) {
        if(invalidCoords(x, y)) {
            throw new IllegalArgumentException("Invalid (x,y) = ("+x+","+y+")");
        }
        return tiledEntitiesMatrix[y][x];
    }

    public boolean invalidCoords(int x, int y) {
        return x < 0 || x > width - 1 || y < 0 || y > height - 1;
    }

    public Coords normalizeCoords(int x, int y) {
        int xMax = width-1;
        int yMax = height-1;

        int newX = x, newY = y;

        if(newX > xMax) {
            newX=newX%xMax-1;
        } else if(newX < 0){
            newX=xMax+newX+1;
        }
        if(newY > yMax) {
            newY=newY%yMax-1;
        } else if(newY < 0){
            newY=yMax+newY+1;
        }

        return new Coords(newX,newY);
    }

    public Coords generateRandomFreeCoords() {
        int randomX, randomY;
        Random r = new Random(System.nanoTime());
        do {
            randomX = r.nextInt(width);
            randomY = r.nextInt(height);
        }while(getTiledEntity(randomX,randomY)!=null);

        return new Coords(randomX,randomY);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean getInitialEnabledBorders() {
        return initialEnabledBorders;
    }

    public boolean getEnabledBorders() {
        return enabledBorders;
    }

    public void setEnabledBorders(boolean enabledBorders) {
        this.enabledBorders = enabledBorders;
    }

    public String getMapType() {
        return mapType;
    }

    /*public void printMap() {
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                TiledEntity t = getTiledEntity(j,i);
                if(t==null)
                    System.out.print(".");
                else if(t instanceof Apple) {
                    System.out.print("A");
                }else if (t instanceof Berry) {
                    System.out.print("B");
                }else if (t instanceof Obstacle) {
                    System.out.print("O");
                }else System.out.print("-");
            }
            System.out.println("");
        }
    }*/
}
