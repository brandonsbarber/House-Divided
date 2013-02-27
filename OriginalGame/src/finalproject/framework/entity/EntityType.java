/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * EntityType.java
 * Enum used for the various types of entities present in the game
 */

package finalproject.framework.entity;

public enum EntityType
{
    LINF,
    HINF,
    LCAV,
    HCAV,
    LART,
    HART,
    HQ,//DEPRECATED
    COAL,
    CONSCRIPT,
    TENT,
    MESS,
    BLACKSMITH,
    ARTILLERY,
    STABLES;
    
    /**
     * Returns if the given EntityType is a unit type
     * @param t type to evaluate
     * @return if it is a unit
     */
    public static boolean isUnit(EntityType t)
    {
        return t.ordinal() >= LINF.ordinal() && t.ordinal() <= HART.ordinal();
    }
    
    /**
     * Converts an int into a type
     * @param i index
     * @return entity type
     */
    public static EntityType valueOf(int i)
    {
        for(EntityType t : values())
        {
            if(t.ordinal() == i)
                return t;
        }
        return null;
    }
    
    /**
     * Gets the number of unit types there are
     * @return number of unit types
     */
    public static int numUnits()
    {
        int num = 0;
        for(EntityType t:values())
        {
            if(isUnit(t))
                num++;
        }
        return num;
    }
}
