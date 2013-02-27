/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * Entity.java
 * Base for both buildings and units
 */

package finalproject.framework.entity;

public class Entity
{
    private int height,width,health,price, maxHealth;
    private int x,y;
    
    private EntityType type;
    
    /**
     * Constructs an entity with the given type and location
     * @param t entity type
     * @param x x-position
     * @param y y-position
     */
    public Entity(EntityType t, int x, int y)
    {
        Constant constant = ConstantLoader.getConstant(t);
        height = constant.getHeight();
        width = constant.getWidth();
        health = constant.getHealth();
        price = constant.getPrice();
        maxHealth = constant.getMaxHealth();
        this.type = t;
        this.x = x;
        this.y = y;
    }
    
    /**
     * Constructs an entity of the given type
     * @param t the entity type
     */
    public Entity(EntityType t)
    {
        Constant constant = ConstantLoader.getConstant(t);
        height = constant.getHeight();
        width = constant.getWidth();
        health = constant.getHealth();
        price = constant.getPrice();
        maxHealth = constant.getMaxHealth();
        this.type = t;
        this.x = -1;
        this.y = -1;
    }
    
    /**
     * Gets the type of the entity
     * @return type of the entity
     */
    public EntityType getType()
    {
        return type;
    }
    
    /**
     * Gets the height of the entity
     * @return height of the entity
     */
    public int getHeight()
    {
        return height;
    }
    
    /**
     * Gets the width of the entity
     * @return width of the entity
     */
    public int getWidth()
    {
        return width;
    }
    
    /**
     * Gets the health of the unit
     * @return health of the unit
     */
    public int getHealth()
    {
        return health;
    }
    
    /**
     * Damages the entity
     * @param decrease amount of damage
     */
    public void damageHealth(int decrease)
    {
        health -= decrease;
    }
    
    /**
     * Gets the x-position
     * @return x-position
     */
    public int getX()
    {
        return x;
    }
    
    /**
     * Gets the y-position
     * @return y-position
     */
    public int getY()
    {
        return y;
    }
    
    /**
     * Sets the position of the entity
     * @param x x-position
     * @param y y-position
     */
    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gets the price of the entity
     * @return price of the entity
     */
    public int getPrice()
    {
        return price;
    }
    
    /**
     * Gets the maximum health of the unit
     * @return maximum health of the unit
     */
    public int getMaxHealth()
    {
        return maxHealth;
    }
    
    /**
     * Determines whether two entities are the same
     * @param o object to compare
     * @return whether they are the same
     */
    public boolean equals(Object o)
    {
        if(!(o instanceof Entity))
            return false;
        Entity other = (Entity)o;
        return (getType() == other.getType()) && (getX() == other.getX()) && (getY() == other.getY());
    }
}
