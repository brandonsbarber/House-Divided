/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * Map.java
 * Map used to portray landscape
 */

package finalproject.gui;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Map used for java cartographer
 * @author Brandon Barber
 */
public class Map
{
    private int[][] mapValues;
    
    public static final int SMALL = 100;
    public static final int MEDIUM = 200;
    public static final int LARGE = 300;
    public static final int EXTRA_LARGE = 300;
    

    public static final int GRASS_ONE = 0;
    public static final int GRASS_TWO = 1;
    public static final int GRASS_THREE = 2;
    public static final int SNOW = 3;
    public static final int STONE = 4;
    public static final int DIRT = 5;
    public static final int SAND = 6;
    public static final int WATER = 7;
    
    public static final int CONCRETE = 8;
    public static final int BRIDGE = 9;
    public static final int DOCK = 10;
    public static final int ROAD_BLANK = 11;
    
    public static final int NUM_TILE_TYPES = 12;
    public static final int NUM_NATURAL_TILES = 8;
    public static final int NUM_ARTIFICIAL_TILES = 4;
    
    public static final String[] N_BUTTON_NAMES = {"Grass 1","Grass 2","Grass 3","Snow","Stone","Dirt","Sand","Water"};
    public static final String[] A_BUTTON_NAMES = {"Concrete","Bridge","Dock","Road"};
    
    private TreeMap<Integer,ArrayList<Integer>> spawn1 = new TreeMap<Integer,ArrayList<Integer>>();
    private TreeMap<Integer,ArrayList<Integer>> spawn2 = new TreeMap<Integer,ArrayList<Integer>>();
    
    public static int SQUARE_SIZE = 15;
    
    /**Constructs a square map with the given dimension
     * 
     * @param dimension side length for square
     */
    public Map(int dimension)
    {
        mapValues = new int[dimension][dimension];
    }
    
    /**
     * Sets default terrain to be used
     * @param defaultTerrain which tile to place at all locations
     */
    public void setDefault(int defaultTerrain)
    {
        for(int row = 0; row < mapValues.length; row++)
        {
            for(int column = 0; column < mapValues.length; column++)
            {
                mapValues[row][column] = defaultTerrain;
            }
        }
    }
    
    /**
     * Gets the tile at the given coordinate
     * @param coord coordinate to look at
     * @return the tile code at the given location
     */
    public int getTile(Coordinate coord)
    {
        return mapValues[coord.getX()][coord.getY()];
    }
    
    /**
     * Sets tile at given location to given type
     * @param coord location
     * @param tile tile type to be placed
     */
    public void setTile(Coordinate coord, int tile)
    {
        mapValues[coord.getX()][coord.getY()] = tile;
    }
    
    /**
     * Sets map data to given array
     * @param map 2D array of ints used to replace Map data
     */
    public void loadMap(int[][] map)
    {
        mapValues = map;
    }
    
    /**
     * Returns the side length of the map
     * @return the side length of the map
     */
    public int getDimension()
    {
        return mapValues.length; 
    }
    
    /**
     * Creates a clone of the int data of the map
     * @return the int data of the map
     */
    public int[][] getData()
    {
        int[][] clone = new int[getDimension()][getDimension()];
        for(int i = 0; i < getDimension(); i++)
        {
            for(int h = 0; h < getDimension(); h++)
            {
                clone[i][h] = mapValues[i][h];
            }
        }
        return clone;
    }
    
    /**
     * Sets display square size to given value
     * @param newSize new square size
     */
    public static void setSquareSize(int newSize)
    {
        SQUARE_SIZE = newSize;
    }
    
    /**
     * Sets a spawn area based on the specified coordinates
     * @param x1 first x
     * @param y1 first y
     * @param x2 second x
     * @param y2 second y
     */
    public void setSpawnArea(int x1, int y1, int x2, int y2)
    {
        if(spawn1.isEmpty())
        {
            for(int x = x1/SQUARE_SIZE; x < x2/SQUARE_SIZE; x++)
            {
                for(int y = y1/SQUARE_SIZE; y < y2/SQUARE_SIZE; y++)
                {
                    if(!spawn1.containsKey(x))
                        spawn1.put(x,new ArrayList<Integer>());
                    spawn1.get(x).add(y);
                }
            }
        }
        else
        {
            for(int x = x1/SQUARE_SIZE; x < x2/SQUARE_SIZE; x++)
            {
                for(int y = y1/SQUARE_SIZE; y < y2/SQUARE_SIZE; y++)
                {
                    if(!spawn2.containsKey(x))
                        spawn2.put(x,new ArrayList<Integer>());
                    spawn2.get(x).add(y);
                }
            }
        }
    }
    
    /**
     * Gets a map of all spawn 1 areas
     * @return union spawn areas
     */
    public TreeMap<Integer,ArrayList<Integer>> getSpawn1()
    {
        return spawn1;
    }
    
    /**
     * Gets a map of all spawn 2 areas
     * @return confederate spawn areas
     */
    public TreeMap<Integer,ArrayList<Integer>> getSpawn2()
    {
        return spawn2;
    }
}
