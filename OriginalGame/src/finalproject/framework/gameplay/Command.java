/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * Command.java
 * Command telling something in the game to do something
 */

package finalproject.framework.gameplay;

import finalproject.framework.entity.Building;
import finalproject.framework.entity.Entity;
import finalproject.framework.entity.EntityType;
import finalproject.framework.entity.Unit;

public class Command
{
    private String toReturn;
    private CommandType label;
    private Entity u;
    private String[] args;
    private Player player;
    
    /**
     * Constructs a command
     * @param label command label
     * @param u entity affected
     * @param args arguments
     */
    public Command(CommandType label, Entity u, String[] args)
    {
        toReturn = "";
        toReturn+=label+" "+u+" ";
        for(int i = 0; i < args.length; i++)
        {
            toReturn+=args[i];
            if(i != args.length-1)
            {
                toReturn += " ";
            }
        }
    }
    
    /**
     * Converts a string into a command
     * @param str string to command
     */
    public Command(String str)
    {
        toReturn = str;
        label = CommandType.valueOf(str.substring(0,str.indexOf(" ")));
        str = str.substring(str.indexOf(" ")+1);
        EntityType type = EntityType.valueOf(str.substring(0, str.indexOf("_")));
        int count = Integer.parseInt(str.substring(str.indexOf("_")+1, str.indexOf(" ")));
        str = str.substring(str.indexOf(" ")+1);
        args = str.split(" ");
        
        if(label == CommandType.SPAWN)
        {
            Unit unit = new Unit(type,Integer.parseInt(args[1]),Integer.parseInt(args[2]), count);
            player = Player.valueOf(args[0]);
            unit.setDestination(unit.getX(), unit.getY());
            u = unit;
        }
        else if(label == CommandType.MOVE)
        {
            Unit unit = new Unit(type,count);
            player = Player.valueOf(args[0]);
            u = unit;
        }
        else if(label == CommandType.BUILD)
        {
            Building build = new Building(type,Integer.parseInt(args[1]),Integer.parseInt(args[2]),count);
            player = Player.valueOf(args[0]);
            u = build;
        }
        else if(label == CommandType.ATTACK)
        {
            player = Player.valueOf(args[0]);
            Unit unit = new Unit(type,count);
            u = unit;
        }
    }
    
    /**
     * String of command
     * @return string of command
     */
    @Override
    public String toString()
    {
        return toReturn;
    
    }
    
    /**
     * Gets the player sending the command
     * @return player
     */
    public Player getPlayer()
    {
        return player;
    }
    
    /**
     * Gets the commandLabel
     * @return commandLabel
     */
    public CommandType getLabel()
    {
        return label;
    }
    
    /**
     * Gets the entity influenced
     * @return entity
     */
    public Entity getEntity()
    {
        return u;
    }
    
    /**
     * Gets the arguments
     * @return the arguments
     */
    public String[] getArgs()
    {
        return args;
    }
}
