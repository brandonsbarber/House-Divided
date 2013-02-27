/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * Player.java
 * Enum for which side of the war the player is on
 */

package finalproject.framework.gameplay;

public enum Player
{
    UNION,
    CONFEDERATE;
    
    /**
     * Reverses the player from one side to the other
     * @param player player to flip
     * @return flipped player
     */
    public static Player flip(Player player)
    {
        if(player == UNION)
            return CONFEDERATE;
        return UNION;
    }
}
