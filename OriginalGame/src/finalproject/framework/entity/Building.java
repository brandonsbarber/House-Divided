/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * Building.java
 * Class for a building in the game
 */

package finalproject.framework.entity;

public class Building extends Entity implements Comparable
{
    private int spawnX, spawnY, count,cp;
    
    public static int num = 1;
    private BuildingConstant constant;
    
    /**
     * Constructs a new building at the specified location
     * @param t type of the building
     * @param x x-position
     * @param y y-position
     */
    public Building(EntityType t, int x, int y)
    {
        super(t,x,y);
        constant = (BuildingConstant)ConstantLoader.getConstant(t);
        spawnX = constant.getSpawnX();
        spawnY = constant.getSpawnY();
        cp = constant.getCP();
        count = num++;
    }
    
    /**
     * Constructs a new building at the specified location with the given count
     * @param t type of the building
     * @param x x-position
     * @param y y-position
     * @param count count of the building
     */
    public Building(EntityType t, int x, int y, int count)
    {
        super(t,x,y);
        constant = (BuildingConstant)ConstantLoader.getConstant(t);
        spawnX = constant.getSpawnX();
        spawnY = constant.getSpawnY();
        cp = constant.getCP();
        this.count = count;
    }
    
    /**
     * Constructs a new building with the given count
     * @param t type of the building
     * @param count count of the building
     */
    public Building(EntityType t, int count)
    {
        super(t,-1,-1);
        this.count = count;
    }
    
    /**
     * Gets the x-spawn
     * @return x-spawn
     */
    public int getSpawnX()
    {
        return spawnX;
    }
    
    /**
     * Gets the y-spawn
     * @return y-spawn
     */
    public int getSpawnY()
    {
        return spawnY;
    }
    
    /**
     * Compares two objects
     * @param t object to compare
     * @return comparison
     */
    @Override
    public int compareTo(Object t)
    {
        if(t == this)
            return 0;
        if(t instanceof Building)
        {
            Building b = (Building)t;
            if(getType() == b.getType())
            {
                return count - b.getCount();
            }
            else
                return getType().compareTo(b.getType());
        }
        return -1;
    }
    
    /**
     * Gets the count of the building
     * @return count of the building
     */
    public int getCount()
    {
        return count;
    }
    
    /**
     * Converts object to a string
     * @return string version of the object
     */
    public String toString()
    {
        return getType()+"_"+count;
    }
    
    /**
     * Gets the command points supplemented by this building
     * @return command points
     */
    public int getCP()
    {
        return cp;
    }
}
