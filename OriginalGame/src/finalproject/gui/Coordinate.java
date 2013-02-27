/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * Coordinate.java
 * Coordinate location to use with map
 */

package finalproject.gui;

/**
 * Coordinate class for use with map
 * @author Brandon Barber
 */
public class Coordinate
{
    private int xCoord, yCoord;
    
    /**
     * Constructs a new coordinate
     * @param x the x position
     * @param y the y position
     */
    public Coordinate(int x, int y)
    {
        xCoord = x;
        yCoord = y;
    }
    
    /**
     * Gets the x of the coordinate
     * @return the x position
     */
    public int getX()
    {
        return xCoord;
    }
    
    /**
     * Gets the y of the coordinate
     * @return the y position
     */
    public int getY()
    {
        return yCoord;
    }
    
    /**
     * Returns if the coordinate has the same x and y as another
     * @param o the other coordinate
     * @return if the two are equal
     */
    @Override
    public boolean equals(Object o)
    {
        if(o instanceof Coordinate)
        {
            Coordinate other = (Coordinate)o;
            return xCoord == other.getX() && yCoord == other.getY();
        }
        return false;
    }
}
