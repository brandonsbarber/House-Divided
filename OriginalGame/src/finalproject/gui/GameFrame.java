/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * GameFrame.java
 * Frame that displays for gameplay
 */

package finalproject.gui;

import finalproject.framework.entity.ConstantLoader;
import finalproject.framework.entity.Entity;
import finalproject.framework.entity.EntityType;
import finalproject.framework.entity.Unit;
import finalproject.framework.gameplay.Command;
import finalproject.framework.gameplay.CommandType;
import finalproject.framework.gameplay.Game;
import finalproject.framework.gameplay.Player;
import finalproject.net.ClientCom;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.Socket;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class GameFrame extends JFrame implements ActionListener, MouseListener, MouseMotionListener, WindowListener
{
    private Player player;
    private Dimension dim;
    public final String[] UNION_UNITS = {"Army of the Potomac","20th Maine","Light Cavalry","Heavy Cavalry","Light Artillery","Heavy Artillery"};
    public final String[] BUILDINGS = {"Coal Mine", "Conscription Center","Tent","Mess Tent","Blacksmith","Artillery Range","Stables"};
    public final String[] CONFED_UNITS = {"Army of Virginia","Stonewall Brigade","Light Cavalry","Heavy Cavalry","Light Artillery","Heavy Artillery"};
    
    private ArrayList<JButton> unitButtons;
    private ArrayList<JButton> buildingButtons;
    
    private Game game;
    
    private MapLocation mapLoc;
    private EntityType buildingType;
    
    private Display disp;
    private int SCROLL_TOLERANCE = 100;
    private JPanel menuPane;
    
    private Cursor normalCursor, attackCursor, unitCursor;
    
    private Socket socket;
    private ClientCom client;
    private Map map;
    private boolean scroll;
    private boolean running = true;
    private boolean winning = false;
    private InfoPanel info;
    private MiniMap mini;
    private String[] units;
    private boolean building;
    
    /**
     * Constructs a new GameFrame
     * @param game game object
     * @param m map to use
     * @param s socket for communication
     */
    public GameFrame(Game game, Map m, Socket s)
    {
        this.game = game;
        this.player = game.getPlayer();
        map = m;
        menuPane = createMenuBar();
        try
        {
            socket = s;
            client = new ClientCom(socket);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Network error!");
        }
        
        try
        {
            Image normal = ImageIO.read(getClass().getResource("/normalCursor.png"));
            int xPoint = normal.getHeight(this)/2;
            int yPoint = normal.getHeight(this)/2;
            Point p = new Point(xPoint, yPoint);
            
            normalCursor = Toolkit.getDefaultToolkit().createCustomCursor(normal, p, null);
            Image attack = ImageIO.read(getClass().getResource("/attackCursor.png"));
            attackCursor = Toolkit.getDefaultToolkit().createCustomCursor(attack, p, null);
            
            Image good = ImageIO.read(getClass().getResource("/unitCursor.png"));
            unitCursor = Toolkit.getDefaultToolkit().createCustomCursor(good,p,null);
            setCursor(normalCursor);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        
        setLayout(new BorderLayout());
        add(menuPane, BorderLayout.EAST);
        
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dim);
        
        mapLoc = new MapLocation(dim,menuPane.getSize(),map.getDimension());
        disp = new Display(game,mapLoc, m);
        disp.addMouseListener(this);
        disp.addMouseMotionListener(this);
        add(disp);
        
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
    
    /**
     * Starts the game running
     */
    public void run()
    {
        setVisible(true);
    }
    
    /**
     * Disposes of the game elements
     */
    @Override
    public void dispose()
    {
        try
        {
            socket.close();
        }
        catch(Exception e)
        {
            
        }
        super.dispose();
    }

    private void gameOver()
    {
        running = false;
    }
    
    /**
     * Checks whether a game is running
     * @return if game is running
     */
    public boolean running()
    {
        return running;
    }
    
    /**
     * Sets whether or not the player won
     * @param b if player won
     */
    public void setWinner(boolean b)
    {
        winning = b;
    }
    
    /**
     * Returns if player won
     * @return if player won
     */
    public boolean getWinner()
    {
        return winning;
    }
    
    private JPanel createMenuBar()
    {
        JPanel menu = new JPanel();
        JPanel miniPanel = new JPanel();
        mini = new MiniMap(game,map,mapLoc);
        miniPanel.setLayout(new BorderLayout());
        miniPanel.add(new JPanel(),BorderLayout.WEST);
        miniPanel.add(new JPanel(),BorderLayout.EAST);
        miniPanel.add(mini);
        mini.addMouseListener(this);
        miniPanel.setBounds(0,0,map.getDimension()+50,map.getDimension());
        
        menu.setLayout(new GridLayout(3,1,0,0));
        menu.add(miniPanel);

        info = new InfoPanel(game);
        menu.add(info);
        
        JTabbedPane tabs = new JTabbedPane();
        JPanel unitTab = new JPanel();
        unitTab.setLayout(new GridLayout(0,2,30,30));
        
        JPanel buildingsTab = new JPanel();
        buildingsTab.setLayout(new GridLayout(0,2,30,30));
        
        units = new String[0];
        unitButtons = new ArrayList<JButton>();
        buildingButtons = new ArrayList<JButton>();
        
        switch(player)
        {
            case UNION: units = UNION_UNITS;break;
            case CONFEDERATE: units = CONFED_UNITS;break;
        }
        
        for(String name: units)
        {
            JButton but = new JButton(name);
            but.addActionListener(this);
            unitTab.add(but);
            unitButtons.add(but);
        }
        
        for(String build : BUILDINGS)
        {
            JButton but = new JButton(build);
            but.addActionListener(this);
            buildingsTab.add(but);
            buildingButtons.add(but);
        }
        
        tabs.addTab("Buildings",buildingsTab);
        tabs.addTab("Units",unitTab);
        
        menu.add(tabs);
        return menu;
    }
    
    /**
     * Performs actions based on what occurs
     * @param e action event
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() instanceof JButton)
        {
            JButton button = (JButton)e.getSource();
            
            String command = "";
            
            for(int index = 0; index < units.length; index++)
            {
                if(button.getText().equals(units[index]))
                {
                    game.recruit(EntityType.valueOf(index));
                    return;
                }
            }
            for(int index = 0; index < buildingButtons.size(); index++)
            {
                JButton but = buildingButtons.get(index);
                if(e.getSource() == but)
                {
                    building = true;
                    buildingType = EntityType.valueOf(index+EntityType.COAL.ordinal());
                    disp.setBuildingType(buildingType);
                }
            }
        }
    }
    
    /**
     * Repaints the game panel
     */
    @Override
    public void repaint()
    {
        if(!game.isStarting() && game.getBuildings().isEmpty() && game.getPlayerUnits().isEmpty())
        {
            gameOver();
            setWinner(false);
            client.writeToServer(new Command(CommandType.LOSS,new Entity(EntityType.LART,-1,-1),new String[0]));
        }
        if(!game.isStarting() && game.getEnemyBuildings().isEmpty() && game.getEnemyUnits().isEmpty())
        {
            gameOver();
            setWinner(true);
        }
        if(!mapLoc.hasMenuSize())
            mapLoc.setMenuSize(menuPane.getSize());
        if(mini.nullMap())
            mini.setMapLoc(mapLoc);
        if(scroll)
        {
            if(mapLoc.getMouseX() <= SCROLL_TOLERANCE)
                mapLoc.moveLeft();
            else if(mapLoc.getMouseX() >= mapLoc.getXSize()-SCROLL_TOLERANCE)
                mapLoc.moveRight();
            if(mapLoc.getMouseY() <= SCROLL_TOLERANCE)
                mapLoc.moveUp();
            else if(mapLoc.getMouseY() >= mapLoc.getYSize() - SCROLL_TOLERANCE)
                mapLoc.moveDown();
        }
        game.update();
        info.update();
        if(game.hasCommand())
            client.writeToServer(game.getCommand());
        Command c = client.readFromServer();
        if(c != null)
        {
                game.giveCommand(c);
        }
        updateButtons();
        super.repaint();
    }
    
    private void updateButtons()
    {
        for(int index = 0; index < unitButtons.size(); index++)
        {
            if(index <= 1)
            {
                unitButtons.get(index).setEnabled(game.getInfSpawn() != null && game.getMoney() >= new Unit(EntityType.valueOf(index)).getPrice() && game.getCP() + new Unit(EntityType.valueOf(index)).getCP() <= game.getMaxCP());
            }
            else if(index <= 3)
            {
                unitButtons.get(index).setEnabled(game.getCavSpawn() != null && game.getCP() + new Unit(EntityType.valueOf(index)).getCP() <= game.getMaxCP());
            }
            else if(index <= 5)
            {
                unitButtons.get(index).setEnabled(game.getArtSpawn() != null && game.getCP() + new Unit(EntityType.valueOf(index)).getCP() <= game.getMaxCP());
            }
        }
        for(int index = 0; index < buildingButtons.size() ; index++)
        {
            buildingButtons.get(index).setEnabled(ConstantLoader.getConstant(EntityType.valueOf(index)).getPrice() <= game.getMoney());
        }
    }
    
    /**
     * Processes a mouse click event
     * @param e mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(e.getSource() instanceof MiniMap)
        {
            mapLoc.setLocationFromMini(e.getX(),e.getY());
            return;
        }
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            if(!building)
            {
                game.select(e.getX()+mapLoc.getScreenX()*Display.SQUARE_SIZE,e.getY()+mapLoc.getScreenY()*Display.SQUARE_SIZE);
            }
            else
            {
                building = game.placeBuilding(buildingType, e.getX()/Display.SQUARE_SIZE*Display.SQUARE_SIZE+mapLoc.getScreenX()*Display.SQUARE_SIZE,e.getY()/Display.SQUARE_SIZE*Display.SQUARE_SIZE+mapLoc.getScreenY()*Display.SQUARE_SIZE);
            }
        }
        else if(e.getButton() == MouseEvent.BUTTON3)
        {
            if(!building)
            {
                game.setDestination(e.getX()+mapLoc.getScreenX()*Display.SQUARE_SIZE,e.getY()+mapLoc.getScreenY()*Display.SQUARE_SIZE);
            }
            else
            {
                building = false;
            }
        }
        if(!building)
        {
            buildingType = null;
            disp.setBuildingType(buildingType);
        }
    }
    
     private void setCursorIcon()
    {
        if(game.hasSelected() && game.getSelected() instanceof Unit && game.overEnemyUnit(mapLoc.getMouseX()+mapLoc.getScreenX()*Display.SQUARE_SIZE,mapLoc.getMouseY()+mapLoc.getScreenY()*Display.SQUARE_SIZE))
        {
            setCursor(attackCursor);
        }
        else if(game.overPlayerUnit(mapLoc.getMouseX()+mapLoc.getScreenX()*Display.SQUARE_SIZE,mapLoc.getMouseY()+mapLoc.getScreenY()*Display.SQUARE_SIZE))
            setCursor(unitCursor);
        else
            setCursor(normalCursor);
    }
    
    /**
     * UNUSED METHOD
     * @param e 
     */
    @Override
    public void mousePressed(MouseEvent e) {}
    
    /**
     * UNUSED METHOD
     * @param e 
     */
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    /**
     * UNUSED METHOD
     * @param e 
     */
    @Override
    public void mouseEntered(MouseEvent e) {scroll = true;}
    
    /**
     * UNUSED METHOD
     * @param e 
     */
    @Override
    public void mouseExited(MouseEvent e) {scroll = false;}
    
    /**
     * UNUSED METHOD
     * @param e 
     */
    @Override
    public void mouseDragged(MouseEvent me) {}
    
    /**
     * Updates position based on mouse move
     * @param e mouse event
     */
    @Override
    public void mouseMoved(MouseEvent me)
    {
        mapLoc.updateMouse(me.getX(),me.getY());
        setCursorIcon();
    }
    
    /**
     * UNUSED METHOD
     * @param e 
     */
    @Override
    public void windowOpened(WindowEvent e) {}
    
    /**
     * Designed to keep people playing the game
     * @param e window event
     */
    @Override
    public void windowClosing(WindowEvent e) {JOptionPane.showMessageDialog(null,"Turn and fight like a man!");}
    
    /**
     * UNUSED METHOD
     * @param e 
     */
    @Override
    public void windowClosed(WindowEvent e) {}
    
    /**
     * UNUSED METHOD
     * @param e 
     */
    @Override
    public void windowIconified(WindowEvent e) {}
    
    /**
     * UNUSED METHOD
     * @param e 
     */
    @Override
    public void windowDeiconified(WindowEvent e) {}
    
    /**
     * UNUSED METHOD
     * @param e 
     */
    @Override
    public void windowActivated(WindowEvent e) {}
    
    /**
     * UNUSED METHOD
     * @param e 
     */
    @Override
    public void windowDeactivated(WindowEvent e) {}
}
