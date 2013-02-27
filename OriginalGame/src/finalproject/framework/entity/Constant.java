/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * Constant.java
 * Basic constant fieldss
 */

package finalproject.framework.entity;

public class Constant 
{
    private String name;
    private int height, width, health,price;
    private int maxHealth;
    
    /**
     * Constructs a constant
     * @param name name of the constant
     * @param width width of the entity
     * @param height height of the entity
     * @param health default health of the entity
     * @param price price of the entity
     */
    public Constant(String name,int width, int height, int health, int price)
    {
        this.name = name;
        this.height = height;
        this.width = width;
        this.health = health;
        this.maxHealth = health;
        this.price = price;
    }
    
    /**
     * Gets the name of the entity
     * @return name of the entity
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Gets the width of the entity
     * @return width
     */
    public int getWidth()
    {
        return width;
    }
    
    /**
     * Gets the height of the entity
     * @return height
     */
    public int getHeight()
    {
        return height;
    }
    
    /**
     * Gets the health of the entity
     * @return health
     */
    public int getHealth()
    {
        return health;
    }
    
    /**
     * Gets the price of the entity
     * @return price
     */
    public int getPrice()
    {
        return price;
    }
    
    /**
     * Gets the max health of the entity
     * @return max health
     */
    public int getMaxHealth()
    {
        return maxHealth;
    }
}
