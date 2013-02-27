/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * UnitConstant.java
 * Constant fields used for units
 */

package finalproject.framework.entity;

public class UnitConstant extends Constant
{
    private int speed,damage, rate, radius,cp;
    
    /**
     * Creates a new Unit Constant
     * @param name name of the unit type
     * @param width width of the unit
     * @param height height of the unit
     * @param health default starting health of the unit
     * @param price price of the unit
     * @param speed speed of the unit
     * @param damage damage dealt by the unit
     * @param radius damage radius of the unit
     * @param rate time between attacks
     * @param cp command point value
     */
    public UnitConstant(String name,int width, int height, int health, int price, int speed, int damage, int radius, int rate, int cp)
    {
        super(name,width,height,health,price);
        this.speed = speed;
        this.damage = damage;
        this.radius = radius;
        this.rate = rate;
        this.cp = cp;
    }
    
    /**
     * Gets the damage dealt by the unit
     * @return damage dealt by the unit
     */
    public int getDamage()
    {
        return damage;
    }
    
    /**
     * Gets the speed of the unit
     * @return speed of the unit
     */
    public int getSpeed()
    {
        return speed;
    }
    
    /**
     * Gets the attack radius of the unit
     * @return attack radius of the unit
     */
    public int getRadius()
    {
        return radius;
        
    }
    
    /**
     * Gets the attack rate of the unit
     * @return attack rate of the unit
     */
    public int getRate()
    {
        return rate;
    }
    
    /**
     * Gets the command point of a unit
     * @return command point value
     */
    public int getCP()
    {
        return cp;
    }
    
    /**
     * Returns a String format of the constant
     * @return String format of the constant
     */
    @Override
    public String toString()
    {
        return getName()+" "+getWidth()+" "+getHeight()+" "+speed+" "+getHealth()+" "+damage+" "+rate+" "+radius;
    }
}
