/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * MiniMap.java
 * Miniature map that displays during the game
 */

package finalproject.gui;

import finalproject.framework.entity.Building;
import finalproject.framework.entity.Unit;
import finalproject.framework.gameplay.Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;

public class MiniMap extends JComponent
{
	private static final long serialVersionUID = -640381280558913585L;
	private Game game;
    private Map map;
    private MapLocation mapLoc;
    private Image[] images = new Image[Map.NUM_TILE_TYPES];
    
    /**
     * Constructs a new minimap
     * @param g game object
     * @param m map object
     * @param mapLoc map location object
     */
    public MiniMap(Game g,Map m, MapLocation mapLoc)
    {
        game = g;
        map = m;
        this.mapLoc = mapLoc;
        
        ImageLoader loader = new ImageLoader();
        
        for(int imageNumber = 1; imageNumber <= Map.NUM_TILE_TYPES; imageNumber++)
        {
            images[imageNumber-1] = loader.loadImage("floorTiles"+String.format("%04d",imageNumber)+".jpg");
        }
    }
    
    /**
     * Paints the mini-map
     * @param g graphics to paint on
     */
    @Override
    public void paintComponent(Graphics g)
    {
        //g.setColor(Color.BLUE);
        //g.fillRect(0, 0, 300, 300);
        int x = 0;
        int y = 0;
        for(x = 0; x < map.getDimension(); x++)
        {
            for(y = 0; y < map.getDimension(); y++)
            {
                g.drawImage(images[map.getTile(new Coordinate(x,y))],x,y,this);
            }
        }
        g.setColor(Color.BLACK);
        g.fillRect(x,0,75,600);
        for(Building b:game.getBuildings())
        {
            for(x = b.getX()/Display.SQUARE_SIZE; x < b.getX()/Display.SQUARE_SIZE+b.getWidth()/Display.SQUARE_SIZE; x++)
            {
                for(y = b.getY()/Display.SQUARE_SIZE; y < b.getY()/Display.SQUARE_SIZE+b.getHeight()/Display.SQUARE_SIZE; y++)
                {
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
        for(Unit u : game.getPlayerUnits())
        {
            g.fillRect(u.getX()/Display.SQUARE_SIZE, u.getY()/Display.SQUARE_SIZE, u.getWidth()/Display.SQUARE_SIZE, u.getHeight()/Display.SQUARE_SIZE);
        }
        g.setColor(Color.RED);
        for(Building b : game.getEnemyBuildings())
        {
            for(x = b.getX()/Display.SQUARE_SIZE; x < b.getX()/Display.SQUARE_SIZE+b.getWidth()/Display.SQUARE_SIZE; x++)
            {
                for(y = b.getY()/Display.SQUARE_SIZE; y < b.getY()/Display.SQUARE_SIZE+b.getHeight()/Display.SQUARE_SIZE; y++)
                {
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
        for(Unit u : game.getEnemyUnits())
        {
            g.fillRect(u.getX()/Display.SQUARE_SIZE, u.getY()/Display.SQUARE_SIZE, u.getWidth()/Display.SQUARE_SIZE, u.getHeight()/Display.SQUARE_SIZE);
        }
        
        if(mapLoc != null)
        {
            g.setColor(Color.YELLOW);
            g.drawRect(mapLoc.getScreenX(),mapLoc.getScreenY(),mapLoc.getXSize()/Display.SQUARE_SIZE,mapLoc.getYSize()/Display.SQUARE_SIZE);
        }
    }
    
    /**
     * Check for null mapLocation
     * @return if mapLocation is null
     */
    public boolean nullMap()
    {
        return mapLoc == null;
    }
    
    /**
     * Sets map location object
     * @param m map location object
     */
    public void setMapLoc(MapLocation m)
    {
        mapLoc = m;
    }
}
