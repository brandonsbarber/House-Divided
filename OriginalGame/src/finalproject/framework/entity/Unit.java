/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * Unit.java
 * Class for a unit in the game
 */

package finalproject.framework.entity;

import finalproject.framework.gameplay.Game;
import finalproject.gui.Display;
import java.util.ArrayList;
import java.util.TreeMap;

public class Unit extends Entity implements Comparable
{
    public static int UnitCount = 1;
    
    private int damage, speed, spawnTime, count, radius, attackRate,cp;
    private int dx,dy;
    
    private long currentTick;
    private Entity target = null;
    
    /**
     * Constructs a new Unit at the specified building spawn point
     * @param t type of unit
     * @param b building at which to spawn
     */
    public Unit(EntityType t, Building b)
    {
        super(t,b.getSpawnX()+b.getX(),b.getSpawnY()+b.getY());
        UnitConstant constant = (UnitConstant)ConstantLoader.getConstant(t);
        count = UnitCount++;
        damage = constant.getDamage();
        speed = constant.getSpeed();
        dx = getX();
        dy = getY();
        spawnTime = 5;
        radius = constant.getRadius();
        attackRate = constant.getRate();
        cp = constant.getCP();
    }
    
    /**
     * Constructs a Unit of the specified type at an invalid location
     * @param t type of the unit
     */
    public Unit(EntityType t)
    {
        super(t,-1,-1);
        UnitConstant constant = (UnitConstant)ConstantLoader.getConstant(t);
        damage = constant.getDamage();
        speed = constant.getSpeed();
        dx = getX();
        dy = getY();
        spawnTime = 5;
        radius = constant.getRadius();
        attackRate = constant.getRate();
        cp = constant.getCP();
    }
    
    /**
     * Constructs a Unit of the specified type with the given count
     * @param t type of the unit
     * @param count numbering of the unit
     */
    public Unit(EntityType t, int count)
    {
        super(t,-1,-1);
        UnitConstant constant = (UnitConstant)ConstantLoader.getConstant(t);
        this.count = count;
        cp = constant.getCP();
    }
    
    /**
     * Constructs a Unit of the specified type with the given count at the specified x and y
     * @param t type of the unit
     * @param x x-position
     * @param y y-position
     * @param count numbering of the unit
     */
    public Unit(EntityType t, int x, int y, int count)
    {
        super(t,x,y);
        UnitConstant constant = (UnitConstant)ConstantLoader.getConstant(t);
        this.count = count;
        damage = constant.getDamage();
        speed = constant.getSpeed();
        dx = x;
        dy = y;
        spawnTime = 5;
        radius = constant.getRadius();
        attackRate = constant.getRate();
        cp = constant.getCP();
    }
    
    /**
     * Changes the destination of the Unit to the given x and y
     * @param x x-destination
     * @param y y-destination
     */
    public void setDestination(int x, int y)
    {
        dx = x;
        dy = y;
        target = null;
    }
    
    /**
     * Gets the radius of attack
     * @return the radius of attack
     */
    public int getRadius()
    {
        return radius;
    }
    
    /**
     * Moves the unit based on the current time
     * @param tick the current time
     */
    public void move(long tick)
    {
        int x = getX();
        int y = getY();
        TreeMap<Integer,ArrayList<Integer>> invalids = Game.occupied;
        if(target != null)
        {
            dx = target.getX();
            dy = target.getY();
        }
        double xLength = (1.0)*(dx-x)/10;
	double yLength = (1.0)*(dy-y)/10;
        
        if(target != null && Math.pow(xLength,2) + Math.pow(yLength, 2) <= Math.pow((1.0)*radius/10, 2))
        {
            if(currentTick != tick && tick % attackRate == 0)
            {
                damage(target);
                currentTick = tick;
                if(target.getHealth() <= 0)
                    target = null;
            }
        }
        else
        {
            double angleToMove = Math.atan(yLength/xLength);
            if((xLength < 0))
                angleToMove+=Math.PI;

            double xMove = speed*Math.cos(angleToMove);
            double yMove = speed*Math.sin(angleToMove);

            if((x > dx && x+xMove < dx) || (x < dx && x+xMove > dx))
                x = dx;
            else
                if(!invalids.containsKey((x+(int)xMove)/Display.SQUARE_SIZE) || !invalids.get((x+(int)xMove)/Display.SQUARE_SIZE).contains(new Integer(((int)y/Display.SQUARE_SIZE))))
                    x += (int)xMove;

            if((y > dy && y+(int)yMove < dy) || (y < dy && y+(int)yMove > dy))
            {
                y = dy;
            }
            else
                if(!invalids.containsKey(x/Display.SQUARE_SIZE) || !invalids.get((x)/Display.SQUARE_SIZE).contains(new Integer((y+(int)yMove)/Display.SQUARE_SIZE)))
                    y += (int)yMove;

            setPosition(x,y);
        }
    }
    
    /**
     * Gets damage dealt by the unit
     * @return damage dealt by the unit
     */
    public int getDamage()
    {
        return damage;
    }
    
    /**
     * Gets the speed of the unit
     * @return the speed of the unit
     */
    public int getSpeed()
    {
        return speed;
    }
    
    /**
     * Damages the given entity
     * @param ent the entity to damage
     */
    public void damage(Entity ent)
    {
        ent.damageHealth(damage);
    }
    
    /**
     * Gets the amount of time left until spawn (in seconds)
     * @return time until spawn (in seconds)
     */
    public int getSpawnTime()
    {
        
        return spawnTime;
    }
    
    /**
     * Decreases spawn time
     */
    public void decreaseTime()
    {
        spawnTime--;
    }
    
    /**
     * Compares two Units
     * @param u unit to compare with
     * @return numerical comparison
     */
    public int compareTo(Unit u)
    {
        if(getType() == u.getType())
        {
            return count - u.getCount();
        }
        else
        {
            return getType().compareTo(u.getType());
        }
    }
    
    /**
     * Gets the numeric count of the unit
     * @return numeric count of the unit
     */
    public int getCount()
    {
        return count;
    }
    
    /**
     * Compares two objects
     * @param t the object to compare
     * @return numeric comparison
     */
    @Override
    public int compareTo(Object t)
    {
        if(!(t instanceof Unit))
            return 0;
        Unit u = (Unit)t;
        if(getType() == u.getType())
        {
            return getCount()-u.getCount();
        }
        else
        {
            return getType().compareTo(u.getType());
        }
    }
    
    /**
     * String form of the unit
     * @return String form of the unit
     */
    public String toString()
    {
        return getType()+"_"+count;
    }
    
    /**
     * Sets the target to the given entity
     * @param ent new target
     */
    public void setTarget(Entity ent)
    {
        target = ent;
        dx = target.getX();
        dy = target.getY();
    }
    
    /**
     * Gets the command point value
     * @return command point value
     */
    public int getCP()
    {
        return cp;
    }
}
