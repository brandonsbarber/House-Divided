/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * BuildingConstant.java
 * Constant fields used for buildings
 */

package finalproject.framework.entity;

public class BuildingConstant extends Constant
{
    private String name;
    private int spawnX, spawnY,cp;
    
    /**
     * COnstant for buildings
     * @param name name of the building
     * @param width width of the building
     * @param height height of the building
     * @param health health of the building
     * @param price price of the building
     * @param spawnX x-spawn position
     * @param spawnY y-spawn position
     * @param cp the command points added by this building
     */
    public BuildingConstant(String name, int width, int height, int health,int price, int spawnX, int spawnY, int cp)
    {
        super(name,width,height,health,price);
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.cp = cp;
    }
    
    /**
     * Gets the x-spawn position
     * @return x-spawn
     */
    public int getSpawnX()
    {
        return spawnX;
    }
    
    /**
     * Gets the y-spawn position
     * @return y-spawn
     */
    public int getSpawnY()
    {
        return spawnY;
    }
    
    /**
     * Returns a string format of the building
     * @return 
     */
    @Override
    public String toString()
    {
        return getName()+" "+getWidth()+" "+getHeight()+" "+getHealth();
    }
    
    public int getCP()
    {
        return cp;
    }
}
