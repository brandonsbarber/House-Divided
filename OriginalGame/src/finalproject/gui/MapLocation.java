/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * MapLocation.java
 * Object to keep track of where map is being displayed
 */

package finalproject.gui;

import java.awt.Dimension;

public class MapLocation
{
    private int globalX, globalY;
    
    private int xSize,ySize;
    private int dimension;
    private boolean menuBool = false;
    private int mouseX, mouseY;
    public static final int SCROLL_AMOUNT = 1;//Display.SQUARE_SIZE;
    
    /**
     * Constructs a new MapLocation object based on screen dimensions
     * @param screenSize size of screen
     * @param menuSize size of side menu
     * @param dimension dimension side of map
     */
    public MapLocation(Dimension screenSize, Dimension menuSize, int dimension)
    {
        xSize = screenSize.width-menuSize.width;
        ySize = screenSize.height;
        this.dimension = dimension;
    }
    
    /**
     * Redetermines menu size
     * @param menuSize new menu size
     */
    public void setMenuSize(Dimension menuSize)
    {
        xSize -= menuSize.width;
        menuBool = true;
    }
    
    /**
     * Returns if mapLoc has menu size
     * @return if has menu size
     */
    public boolean hasMenuSize()
    {
        return menuBool;
    }
    
    /**
     * Gets the x position to be displayed in top left corner
     * @return x position to be displayed
     */
    public int getScreenX()
    {
        return globalX;
    }
    
    /**
     * Gets the y position to be displayed in top left corner
     * @return y position to be displayed
     */
    public int getScreenY()
    {
        return globalY;
    }
    
    /**
     * Gets width of the main display
     * @return width of the main display
     */
    public int getXSize()
    {
        return xSize;
    }
    
    /**
     * Gets height of the main display
     * @return height of the main display
     */
    public int getYSize()
    {
        return ySize;
    }
    
    /**
     * Gets current x position of mouse
     * @return current x position of mouse
     */
    public int getMouseX()
    {
        return mouseX;
    }
    
    /**
     * Gets current y position of mouse
     * @return current y position of mouse
     */
    public int getMouseY()
    {
        return mouseY;
    }
    
    /**
     * Updates mouse position
     * @param x new x
     * @param y new y
     */
    public void updateMouse(int x, int y)
    {
        mouseX = x;
        mouseY = y;
    }
    
    /**
     * Moves screen to the right
     */
    public void moveRight()
    {
        if(globalX+xSize/Display.SQUARE_SIZE < dimension)
            globalX += SCROLL_AMOUNT;
    }
    
    /**
     * Moves screen to the left
     */
    public void moveLeft()
    {
        if(globalX != 0)
            globalX -= SCROLL_AMOUNT;
    }
    
    /**
     * Moves screen up
     */
    public void moveUp()
    {
        if(globalY != 0)
            globalY -= SCROLL_AMOUNT;
    }
    
    /**
     * Moves screen down
     */
    public void moveDown()
    {
        if(globalY+ySize/Display.SQUARE_SIZE < dimension)
            globalY += SCROLL_AMOUNT;
    }
    
    /**
     * Repositions based on click in minimap
     * @param x new x
     * @param y new y
     */
    public void setLocationFromMini(int x, int y)
    {
        if(x+xSize/Display.SQUARE_SIZE > dimension)
            x = dimension - xSize/Display.SQUARE_SIZE;
        if(y+ySize/Display.SQUARE_SIZE > dimension)
            y = dimension - ySize/Display.SQUARE_SIZE;
        globalX = x;
        globalY = y;
    }
}
