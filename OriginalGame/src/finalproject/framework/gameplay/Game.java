/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * Game.java
 * Class that contains all information for gameplay
 */

package finalproject.framework.gameplay;

import finalproject.framework.entity.Building;
import finalproject.framework.entity.Entity;
import finalproject.framework.entity.EntityType;
import finalproject.framework.entity.Unit;
import finalproject.gui.Coordinate;
import finalproject.gui.Display;
import finalproject.gui.Map;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;
import java.util.TreeSet;


public class Game
{
    private TreeSet<Unit> playerUnits;
    private TreeSet<Unit> enemyUnits;
    private Player player;
    private int resourceTick;
    
    private Queue<Unit> spawn;
    
    private long startTime;
    private long currentSecond;
    
    private long money;
    
    public static final int RESOURCE_TIME = 5;
    
    private TreeSet<Building> coals;
    private TreeSet<Building> buildings;
    private TreeSet<Building> enemyBuildings;
    
    public static final int MONEY_AMOUNT = 50;
    public static final int STARTING_AMOUNT = 1500;
    
    private Building infSpawn,cavSpawn,artSpawn;
    private Entity selected;
    public TreeMap<Integer, ArrayList<Integer>> invalids = new TreeMap<Integer, ArrayList<Integer>>();
    private Map map;
    
    private boolean starting = true;
    private Queue<Command> commands;
    
    private int cp,maxCP;
    public static TreeMap<Integer,ArrayList<Integer>> occupied = new TreeMap<Integer,ArrayList<Integer>>();
    
    /**
     * Constructs a game
     * @param player player being used
     * @param m map with terrain
     */
    public Game(Player player, Map m)
    {
        commands = new LinkedList<Command>();
        playerUnits = new TreeSet<Unit>();
        enemyUnits = new TreeSet<Unit>();
        coals = new TreeSet<Building>();
        this.player = player;
        startTime = System.currentTimeMillis();
        currentSecond = 0;
        resourceTick = 0;
        money = STARTING_AMOUNT;
        commands = new LinkedList<Command>();
        spawn = new LinkedList<Unit>();
        buildings = new TreeSet<Building>();
        enemyBuildings = new TreeSet<Building>();
        
        map = m;
        
        for(int x = 0; x < m.getDimension(); x++)
        {
            for(int y = 0; y < m.getDimension(); y++)
            {
                if(m.getTile(new Coordinate(x,y)) == Map.WATER)
                {
                    if(!occupied.containsKey(x))
                        occupied.put(x, new ArrayList<Integer>());
                    occupied.get(x).add(y);
                }
            }
        }
    }
    
    /**
     * Updates all units
     */
    public void update()
    {
        ArrayList<Entity> dead = new ArrayList<Entity>();
        for(Unit u : playerUnits)
        {
            if(u.getHealth() <= 0)
                dead.add(u);
        }
        for(Building b : buildings)
        {
            if(b.getHealth() <= 0)
                dead.add(b);
        }
        for(Building b : coals)
        {
            if(b.getHealth() <= 0)
                dead.add(b);
        }
        for(Entity e : dead)
        {
            if(e instanceof Unit)
            {
               Unit u = (Unit)e;
               playerUnits.remove(u);
               cp -= u.getCP();
            }
            else
            {
                Building b = (Building)e;
                buildings.remove(b);
                if(b == infSpawn)
                {
                    boolean found = false;
                    for(Building bl : buildings)
                    {
                        if(bl.getType() == EntityType.CONSCRIPT)
                        {
                            infSpawn = bl;
                            found = true;
                            break;
                        }
                    }
                    if(!found)
                        infSpawn = null;
                }
                else if(b == cavSpawn)
                {
                    boolean found = false;
                    for(Building bl : buildings)
                    {
                        if(bl.getType() == EntityType.STABLES)
                        {
                            cavSpawn = bl;
                            found = true;
                            break;
                        }
                    }
                    if(!found)
                        infSpawn = null;
                }
                else if(b == artSpawn)
                {
                    boolean found = false;
                    for(Building bl : buildings)
                    {
                        if(bl.getType() == EntityType.BLACKSMITH)
                        {
                            artSpawn = bl;
                            found = true;
                            break;
                        }
                    }
                    if(!found)
                        infSpawn = null;
                }
                coals.remove(b);
                for(int x1 = b.getX()/Display.SQUARE_SIZE; x1 < b.getX()/Display.SQUARE_SIZE+b.getWidth()/Display.SQUARE_SIZE; x1 ++ )
                {
                    for(int y1 = b.getY()/Display.SQUARE_SIZE; y1 < b.getY()/Display.SQUARE_SIZE+b.getHeight()/Display.SQUARE_SIZE; y1 ++ )
                    {
                        occupied.get(x1).remove(new Integer(y1)) ;
                        if(occupied.get(x1).isEmpty())
                            occupied.remove(x1);
                    }
                }
                maxCP-=b.getCP();
            }
        }
        
        dead.clear();
        
        for(Unit u : enemyUnits)
        {
            if(u.getHealth() <= 0)
                dead.add(u);
        }
        for(Building b : enemyBuildings)
        {
            if(b.getHealth() <= 0)
                dead.add(b);
        }
        for(Entity e : dead)
        {
            if(e instanceof Unit)
            {
               Unit u = (Unit)e;
               enemyUnits.remove(u);
            }
            else
            {
                Building b = (Building)e;
                enemyBuildings.remove(b);
                
                for(int x1 = b.getX()/Display.SQUARE_SIZE; x1 < b.getX()/Display.SQUARE_SIZE+b.getWidth()/Display.SQUARE_SIZE; x1 ++ )
                {
                    for(int y1 = b.getY()/Display.SQUARE_SIZE; y1 < b.getY()/Display.SQUARE_SIZE+b.getHeight()/Display.SQUARE_SIZE; y1 ++ )
                    {
                        occupied.get(x1).remove(new Integer(y1)) ;
                        if(occupied.get(x1).isEmpty())
                            occupied.remove(x1);
                    }
                }
            }
        }
        
        for(Unit u : playerUnits)
        {
            if(u.getHealth() > 0)
                u.move(currentSecond);
        }
        for(Unit u : enemyUnits)
        {
            if(u.getHealth() > 0)
                u.move(currentSecond);
        }
        if(hasTimeMoved())
        {
            if(resourceTick == 0)
            {
                money += coals.size()*MONEY_AMOUNT;
            }

            if(!spawn.isEmpty())
            {
                if(spawn.peek().getSpawnTime() > 0)
                {
                    spawn.peek().decreaseTime();
                }
                else
                {
                    Unit u = spawn.remove();
                    String[] args = {player.toString(),""+u.getX(),""+u.getY()};
                    sendCommand(new Command(CommandType.SPAWN,u, args));
                }
            }
        }
    }
    
    /**
     * Gets the time in seconds
     * @return time in seconds
     */
    public long getTime()
    {
        return (System.currentTimeMillis() - startTime)/1000;
    }
       
    /**
     * Determines if time has moved in seconds
     * @return whether time has moved
     */
    public boolean hasTimeMoved()
    {
        long currentTime = (System.currentTimeMillis() - startTime)/1000;
        if(currentTime == currentSecond)
        {
            return false;
        }
        else
        {
            resourceTick++;
            if(resourceTick == RESOURCE_TIME)
            {
                resourceTick = 0;
            }
            currentSecond = currentTime;
            return true;
        }
    }
    
    /**
     * Gets the player of the game
     * @return the player
     */
    public Player getPlayer()
    {
        return player;
    }
    
    /**
     * Gets money held by the player
     * @return money the player has
     */
    public long getMoney()
    {
        return money;
    }
    
    /**
     * Deducts the given amount of money
     * @param cost cost of the item
     */
    public void deduct(int cost)
    {
        money -= cost;
    }
    
    /**
     * Returns the number of troops owned by the player
     * @return number of troops
     */
    public int getNumTroops()
    {
        return playerUnits.size();
    }
    
    /**
     * Recruits a unit of the given entityType
     * @param entityType type of unit to be used
     */
    public void recruit(EntityType entityType)
    {        
        if(entityType.ordinal() <= EntityType.HINF.ordinal())
        {
            spawn.add(new Unit(entityType,infSpawn));
        }
        else if(entityType.ordinal() <= EntityType.HCAV.ordinal())
        {
            spawn.add(new Unit(entityType,cavSpawn));
        }
        else if(entityType.ordinal() <= EntityType.HART.ordinal())
        {
            spawn.add(new Unit(entityType,artSpawn));
        }
        deduct(new Unit(entityType).getPrice());
        cp+=(new Unit(entityType)).getCP();
    }
    
    /**
     * Gets where infantry spawns
     * @return where infantry spawns
     */
    public Building getInfSpawn()
    {
        return infSpawn;
    }
    
    /**
     * Gets where artillery spawns
     * @return where artillery spawns
     */
    public Building getArtSpawn()
    {
        return artSpawn;
    }
    
    /**
     * Gets where cavalry spawns
     * @return where cavalry spawns
     */
    public Building getCavSpawn()
    {
        return cavSpawn;
    }
    
    /**
     * Prepares command to be sent (adds it to sending queue)
     * @param c command to be sent
     */
    public void sendCommand(Command c)
    {
        commands.add(c);
    }
    
    /**
     * Retrieves the oldest command to be sent
     * @return command to be sent
     */
    public Command getCommand()
    {
        return commands.remove();
    }
    
    /**
     * Returns if there is a command to send
     * @return if there is a command to send
     */
    public boolean hasCommand()
    {
        return commands.size() != 0;
    }
    
    /**
     * Gets all player owned buildings
     * @return player-owned buildings
     */
    public TreeSet<Building> getBuildings()
    {
        TreeSet<Building> temp = new TreeSet<Building>();
        temp.addAll(buildings);
        temp.addAll(coals);
        return temp;
    }
    
    /**
     * Gets all enemy owned buildings
     * @return enemy-owned buildings
     */
    public TreeSet<Building> getEnemyBuildings()
    {
        return enemyBuildings;
    }
    
    /**
     * Gets all units held by the player
     * @return units held by the player
     */
    public TreeSet<Unit> getPlayerUnits()
    {
        return playerUnits;
    }
    
     /**
     * Gets all enemy units
     * @return enemy units
     */
    public TreeSet<Unit> getEnemyUnits()
    {
        return enemyUnits;
    }
    
    /**
     * Gets what type of unit is being spawn
     * @return type of unit being spawned
     */
    public EntityType getSpawnUnitType()
    {
        if(spawn.size() == 0)
            return null;
        return spawn.peek().getType();
    }
    
    /**
     * Gets the selected entity
     * @return selected entity
     */
    public Entity getSelected()
    {
        return selected;
    }
    
    /**
     * Gets if the user currently has something selected
     * @return if the user currently has something selected
     */
    public boolean hasSelected()
    {
        return selected != null;
    }
    
    /**
     * Attempts to select a unit based on x and y position
     * @param x x-position
     * @param y y-position
     */
    public void select(int x, int y)
    {
        for(Entity unit: playerUnits)
        {
            if(unit.getX() <= x && unit.getX()+unit.getWidth() >= x && unit.getY() <= y && unit.getY()+unit.getHeight() >= y)
            {
                selected = unit;
                return;
            }
        }
        for(Entity building : buildings)
        {
            if(building.getX() <= x && building.getX()+building.getWidth() >= x && building.getY() <= y && building.getY()+building.getHeight() >= y)
            {
                selected = building;
                return;
            }
        }
        for(Entity building : coals)
        {
            if(building.getX() <= x && building.getX()+building.getWidth() >= x && building.getY() <= y && building.getY()+building.getHeight() >= y)
            {
                selected = building;
                return;
            }
        }
        selected = null;
    }
    
    /**
     * Sets the destination of a selected unit. Sends a command.
     * @param x new x
     * @param y new y
     */
    public void setDestination(int x, int y)
    {
        if(hasSelected() && selected instanceof Unit)
        {
            for(Entity unit: enemyUnits)
            {
                if(unit.getX() <= x && unit.getX()+unit.getWidth() >= x && unit.getY() <= y && unit.getY()+unit.getHeight() >= y)
                {
                    String[] args = {player.toString(),unit.toString()};
                    Command c = new Command(CommandType.ATTACK,(Unit)selected,args);
                    commands.add(c);
                    return;
                }
            }
            for(Entity building : enemyBuildings)
            {
                if(building.getX() <= x && building.getX()+building.getWidth() >= x && building.getY() <= y && building.getY()+building.getHeight() >= y)
                {
                    String[] args = {player.toString(),building.toString()};
                    Command c = new Command(CommandType.ATTACK,(Unit)selected,args);
                    commands.add(c);
                    return;
                }
            }
            String[] args = {player.toString(), ""+x,""+y};
            Command command = new Command(CommandType.MOVE,(Unit)selected,args);
            commands.add(command);
        }
    }
    
    /**
     * Places a building (if valid) at the given location. Sends a command.
     * @param t type of building
     * @param x x-position
     * @param y y-position
     * @return whether building will continue or not
     */
    public boolean placeBuilding(EntityType t, int x, int y)
    {
        Building b = new Building(t, -1,-1);
        TreeMap<Integer,ArrayList<Integer>> spawnD;
        if(getPlayer() == Player.UNION)
            spawnD = map.getSpawn1();
        else
            spawnD = map.getSpawn2();
        
        for(int x1 = x/Display.SQUARE_SIZE; x1 < x/Display.SQUARE_SIZE+b.getWidth()/Display.SQUARE_SIZE; x1 ++ )
        {
            for(int y1 = y/Display.SQUARE_SIZE; y1 < y/Display.SQUARE_SIZE+b.getHeight()/Display.SQUARE_SIZE; y1 ++ )
            {
                if(occupied.containsKey(x1) && occupied.get(x1).contains(new Integer(y1)) || !spawnD.containsKey(new Integer(x1)) || !spawnD.get(x1).contains(new Integer(y1)))
                {
                    return true;
                }
            }
        }
        b.setPosition((x/Display.SQUARE_SIZE)*Display.SQUARE_SIZE,(y/Display.SQUARE_SIZE)*Display.SQUARE_SIZE);
        
        for(int x1 = x/Display.SQUARE_SIZE; x1 < x/Display.SQUARE_SIZE+b.getWidth()/Display.SQUARE_SIZE; x1 ++ )
        {
            for(int y1 = y/Display.SQUARE_SIZE; y1 < y/Display.SQUARE_SIZE+b.getHeight()/Display.SQUARE_SIZE; y1 ++ )
            {
                if(!occupied.containsKey(x1))
                    occupied.put(x1, new ArrayList<Integer>());
                occupied.get(x1).add(y1);
            }
        }
        String[] args = {player.toString(),""+x,""+y};
        Command c = new Command(CommandType.BUILD, b, args);
        commands.add(c);
        return false;
    }
    
    /**
     * Gets all squares occupied by water or buildings
     * @return occupied locations
     */
    public TreeMap<Integer,ArrayList<Integer>> getOccupied()
    {
        return occupied;
    }
    
    /**
     * Determines if cursor is over enemy unit
     * @param x x-position
     * @param y y-position
     * @return if cursor is over enemy unit
     */
    public boolean overEnemyUnit(int x, int y)
    {
        for(Entity unit: enemyUnits)
        {
            if(unit.getX() <= x && unit.getX()+unit.getWidth() >= x && unit.getY() <= y && unit.getY()+unit.getHeight() >= y)
            {
                return true;
            }
        }
        for(Entity building : enemyBuildings)
        {
            if(building.getX() <= x && building.getX()+building.getWidth() >= x && building.getY() <= y && building.getY()+building.getHeight() >= y)
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Determines if cursor is over player unit
     * @param x x-position
     * @param y y-position
     * @return if cursor is over player unit
     */
    public boolean overPlayerUnit(int x, int y)
    {
        for(Entity unit: playerUnits)
        {
            if(unit.getX() <= x && unit.getX()+unit.getWidth() >= x && unit.getY() <= y && unit.getY()+unit.getHeight() >= y)
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Processes a command based on what it contains
     * @param c command to process
     */
    public void giveCommand(Command c)
    {
        if(c.getLabel() == CommandType.SPAWN)
        {
            if(c.getPlayer() == player)
            {
                playerUnits.add((Unit)c.getEntity());
            }
            else
            {
                enemyUnits.add((Unit)c.getEntity());
            }
        }
        else if(c.getLabel() == CommandType.MOVE)
        {
            if(c.getPlayer() == player)
            {
                for(Unit u:playerUnits)
                {
                    if(u.compareTo((Unit)c.getEntity()) == 0)
                    {
                        u.setDestination(Integer.parseInt(c.getArgs()[1]), Integer.parseInt(c.getArgs()[2]));
                    }
                }
            }
            else
            {
                for(Unit u:enemyUnits)
                {
                    if(u.compareTo((Unit)c.getEntity()) == 0)
                    {
                        u.setDestination(Integer.parseInt(c.getArgs()[1]), Integer.parseInt(c.getArgs()[2]));
                    }
                }
            }
        }
        else if(c.getLabel() == CommandType.BUILD)
        {
            Building build = (Building)c.getEntity();
            if(c.getPlayer() == player)
            {
                deduct(build.getPrice());
                maxCP+=build.getCP();
                if(build.getType() == EntityType.COAL)
                {
                    coals.add(build);
                }
                else
                {
                    buildings.add(build);
                    if(build.getType() == EntityType.CONSCRIPT && infSpawn == null)
                    {
                        infSpawn = build;
                    }
                    else if(build.getType() == EntityType.STABLES && cavSpawn == null)
                    {
                        cavSpawn = build;
                    }
                    else if(build.getType() == EntityType.BLACKSMITH && artSpawn == null)
                    {
                        artSpawn = build;
                    }
                }
            }
            else
            {
                enemyBuildings.add(build);
            }
        }
        else if(c.getLabel() == CommandType.ATTACK)
        {
            Unit unit = (Unit)c.getEntity();
            String line = c.getArgs()[1];
            EntityType enemyT = EntityType.valueOf(line.substring(0,line.indexOf("_")));
            int eCount = Integer.parseInt(line.substring(line.indexOf("_")+1));
            
            if(c.getPlayer() == player)
            {
                for(Unit u : playerUnits)
                {
                    if(u.compareTo(unit) == 0)
                    {
                        if(EntityType.isUnit(enemyT))
                        {
                            Unit enemy = new Unit(enemyT, eCount);
                            for(Unit e : enemyUnits)
                            {
                                if(e.compareTo(enemy) == 0)
                                {
                                    u.setTarget(e);
                                }
                            }
                        }
                        else
                        {
                            Building enemy = new Building(enemyT, eCount);
                            for(Building e : enemyBuildings)
                            {
                                if(e.compareTo(enemy) == 0)
                                {
                                    u.setTarget(e);
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                for(Unit u : enemyUnits)
                {
                    if(u.compareTo(unit) == 0)
                    {
                        if(EntityType.isUnit(enemyT))
                        {
                            Unit enemy = new Unit(enemyT, eCount);
                            for(Unit e : playerUnits)
                            {
                                if(e.compareTo(enemy) == 0)
                                {
                                    u.setTarget(e);
                                }
                            }
                        }
                        else
                        {
                            Building enemy = new Building(enemyT, eCount);
                            for(Building e : buildings)
                            {
                                if(e.compareTo(enemy) == 0)
                                {
                                    u.setTarget(e);
                                }
                            }
                            for(Building e : coals)
                            {
                                if(e.compareTo(enemy) == 0)
                                {
                                    u.setTarget(e);
                                }
                            }
                        }
                    }
                }
            }
        }
        starting = starting && (getBuildings().isEmpty() || getEnemyBuildings().isEmpty());
    }
    
    /**
     * Returns if the game is just beginning (one side at least has not placed buildings)
     * @return if the game is just beginning
     */
    public boolean isStarting()
    {
        return starting;
    }
    
    /**
     * Maximum amount of CP possible
     * @return max amount of CP
     */
    public int getMaxCP()
    {
        return maxCP;
    }
    
    /**
     * Gets the current amount of command points used up
     * @return command points
     */
    public int getCP()
    {
        return cp;
    }
}