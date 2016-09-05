/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * Display.java
 * Main display for gameplay
 */

package finalproject.gui;

import finalproject.framework.entity.Building;
import finalproject.framework.entity.Entity;
import finalproject.framework.entity.EntityType;
import finalproject.framework.entity.Unit;
import finalproject.framework.gameplay.Game;
import finalproject.framework.gameplay.Player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.JComponent;

public class Display extends JComponent
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -233092302702704454L;
	private Game game;
    private MapLocation loc;
    private Map map;
    
    private Image[] images = new Image[Map.NUM_TILE_TYPES];
    private Image[][] unitImages = new Image[2][];
    
    public static final int SQUARE_SIZE = 25;
    private EntityType bType;
    private TreeMap<Integer, ArrayList<Integer>> occupied;
    
    /**
     * Creates a new Display
     * @param g game to display
     * @param l location on map
     * @param m map to render
     */
    public Display(Game g, MapLocation l, Map m)
    {
        ImageLoader loader = new ImageLoader();
        
        for(int imageNumber = 1; imageNumber <= Map.NUM_TILE_TYPES; imageNumber++)
        {
            images[imageNumber-1] = loader.loadImage("floorTiles"+String.format("%04d",imageNumber)+".jpg");
        }
        Image[] union = new Image[EntityType.numUnits()];
        for(int number = 1; number <= union.length; number++)
        {
            union[number-1] = loader.loadImage("union"+number+".png");
        }
        Image[] conf = new Image[EntityType.numUnits()];
        for(int number = 1; number <= conf.length; number++)
        {
            conf[number-1] = loader.loadImage("conf"+number+".png");
        }
        unitImages[0] = union;
        unitImages[1] = conf;
        
        
        game = g;
        loc = l;
        map = m;
        occupied = g.getOccupied();
    }
    
    /**
     * Paints the game
     * @param g graphics on which to paint
     */
    @Override
    public void paintComponent(Graphics g)
    {
        int screenX = loc.getScreenX();
        int screenY = loc.getScreenY();
        int mouseX = loc.getMouseX();
        int mouseY = loc.getMouseY();
        
        for(int x = 0; x <= loc.getXSize() && x+screenX < map.getDimension(); x++)
        {
            for(int y = 0; y <= loc.getYSize() && y+screenY < map.getDimension(); y++)
            {
                g.drawImage(images[map.getTile(new Coordinate(x+screenX,y+screenY))], x*SQUARE_SIZE, y*SQUARE_SIZE, this);
                //g.drawImage(images[map.getTile(new Coordinate(x,y))], x*SQUARE_SIZE, y*SQUARE_SIZE, this);
            }
        }
        
        TreeMap<Integer,ArrayList<Integer>> spawn1 = map.getSpawn1();
        g.setColor(Color.BLUE.brighter());
        for(int x:spawn1.keySet())
        {
            for(int y:spawn1.get(x))
            {
                g.drawRect(x*SQUARE_SIZE-screenX*SQUARE_SIZE, y*SQUARE_SIZE-screenY*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
        
        TreeMap<Integer,ArrayList<Integer>> spawn2 = map.getSpawn2();
        g.setColor(Color.GRAY);
        for(int x:spawn2.keySet())
        {
            for(int y:spawn2.get(x))
            {
                g.drawRect(x*SQUARE_SIZE-screenX*SQUARE_SIZE, y*SQUARE_SIZE-screenY*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
        
        g.setColor(Color.BLACK);
        for(Building building: game.getBuildings())
        {
            g.fillRect(building.getX()-screenX*SQUARE_SIZE, building.getY()-screenY*SQUARE_SIZE, building.getWidth(), building.getHeight());
        }
        
        g.setColor(Color.BLACK);
        for(Building building: game.getEnemyBuildings())
        {
            drawHealth(building,g,screenX,screenY);
            g.fillRect(building.getX()-screenX*SQUARE_SIZE, building.getY()-screenY*SQUARE_SIZE, building.getWidth(), building.getHeight());
        }     
        
        if(bType != null)
        {
            Building b = new Building(bType, -1,-1,0);
            TreeMap<Integer,ArrayList<Integer>> spawnD;
            if(game.getPlayer() == Player.UNION)
                spawnD = spawn1;
            else
                spawnD = spawn2;
            for(int x = (mouseX/SQUARE_SIZE); x < b.getWidth()/SQUARE_SIZE+(mouseX/SQUARE_SIZE); x++)
            {
                for(int y = (mouseY/SQUARE_SIZE); y < b.getHeight()/SQUARE_SIZE+(mouseY/SQUARE_SIZE); y++)
                {
                    if((occupied.containsKey(x+screenX) && occupied.get(x+screenX).contains(new Integer(y+screenY))) || !spawnD.containsKey(new Integer(x+screenX)) || !spawnD.get(x+screenX).contains(new Integer(y+screenY)))
                    {
                        g.setColor(Color.RED);
                    }
                    else
                    {
                        g.setColor(Color.GREEN);
                    }
                    g.fillRect(x*SQUARE_SIZE,y*SQUARE_SIZE,SQUARE_SIZE,SQUARE_SIZE);
                }
            }
        }
        
        g.setColor(Color.GRAY);
        for(Unit unit : game.getPlayerUnits())
        {       
            g.drawImage(unitImages[game.getPlayer().ordinal()][unit.getType().ordinal()],unit.getX()-screenX*SQUARE_SIZE,unit.getY()-screenY*SQUARE_SIZE,this);
            //g.fillRect(unit.getX()-screenX*SQUARE_SIZE,unit.getY()-screenY*SQUARE_SIZE,unit.getWidth(),unit.getHeight());
        }
        int enemyPlayer = 0;
        if(game.getPlayer() == Player.UNION)
            enemyPlayer = 1;
        for(Unit unit : game.getEnemyUnits())
        {
            g.drawImage(unitImages[enemyPlayer][unit.getType().ordinal()],unit.getX()-screenX*SQUARE_SIZE,unit.getY()-screenY*SQUARE_SIZE,this);
            //g.fillRect(unit.getX()-screenX*SQUARE_SIZE,unit.getY()-screenY*SQUARE_SIZE,unit.getWidth(),unit.getHeight());
        }
        if(game.hasSelected())
        {
            drawHealth(game.getSelected(),g,screenX,screenY);
        }
        for(Unit unit : game.getEnemyUnits())
        {
            drawHealth(unit,g,screenX,screenY);
        }
    }
    
    /**
     * Sets the type of building to be built
     * @param b building type
     */
    public void setBuildingType(EntityType b)
    {
        bType = b;
    }

    private void drawHealth(Entity entity, Graphics g, int screenX, int screenY)
    {
        Color temp = g.getColor();
        Entity selected = entity;
        g.setColor(Color.RED);
        g.fillRect(selected.getX()-screenX*SQUARE_SIZE, selected.getY()-15-screenY*SQUARE_SIZE, selected.getWidth(), 10);
        g.setColor(Color.GREEN);
        int amount = (int)(selected.getWidth()*(1.0*selected.getHealth()/selected.getMaxHealth()));
        g.fillRect(selected.getX()-screenX*SQUARE_SIZE, selected.getY()-15-screenY*SQUARE_SIZE, amount, 10);
        g.setColor(temp);
    }
}
