/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * StartupFrame.java
 * Frame that displays on startup
 */

package finalproject.gui;

import finalproject.framework.entity.ConstantLoader;
import finalproject.framework.gameplay.Game;
import finalproject.framework.gameplay.MapName;
import finalproject.framework.gameplay.MapReader;
import finalproject.framework.gameplay.Player;
import finalproject.net.GameServer;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

public class StartupFrame extends JFrame implements MouseListener
{
    
    /**
     * Constructs a new StartupFrame
     * @throws Exception 
     */
    public StartupFrame() throws Exception
    {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screen);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Image title = ImageIO.read(getClass().getResource("/houseText.png"));
        JLabel label = new JLabel(new ImageIcon(title));
        label.setBounds((int)(screen.getWidth()/2-title.getWidth(this)/2),15,title.getWidth(this), title.getHeight(this));
        
        
        Image gameSplash = ImageIO.read(getClass().getResource("/gameSplash.jpg"));
        JLabel imageLabel = new JLabel(new ImageIcon(gameSplash));
        imageLabel.setBounds((int)(screen.getWidth()/2-gameSplash.getWidth(this)/2),(int)(screen.getHeight()/2-gameSplash.getHeight(this)/2),gameSplash.getWidth(this), gameSplash.getHeight(this));
        
        Image woodenBack = ImageIO.read(getClass().getResource("/woodBack.jpg"));
        JLabel woodLabel = new JLabel(new ImageIcon(woodenBack));
        woodLabel.setBounds(0,0,woodenBack.getWidth(this),woodenBack.getHeight(this));
        
        Image on = ImageIO.read(getClass().getResource("/LabelOn.gif"));
        Image off = ImageIO.read(getClass().getResource("/LabelOff.gif"));
        
        int midScreen = (int)(screen.getHeight()/2);
        
        ImageLabel labe = new ImageLabel(on,off, "New Game");
        labe.setBounds((int)(screen.getWidth()/2-on.getWidth(this)/2),midScreen-3*on.getHeight(this),on.getWidth(this),on.getHeight(this));
        labe.addMouseListener(this);
        
        ImageLabel labe2 = new ImageLabel(on,off, "Join Game");
        labe2.setBounds((int)(screen.getWidth()/2-on.getWidth(this)/2),midScreen-1*on.getHeight(this),on.getWidth(this),on.getHeight(this));
        labe2.addMouseListener(this);
        
        ImageLabel labe3 = new ImageLabel(on,off, "About");
        labe3.setBounds((int)(screen.getWidth()/2-on.getWidth(this)/2),midScreen+1*on.getHeight(this),on.getWidth(this),on.getHeight(this));
        labe3.addMouseListener(this);
        
        ImageLabel labe4 = new ImageLabel(on,off, "Help");
        labe4.setBounds((int)(screen.getWidth()/2-on.getWidth(this)/2),midScreen+3*on.getHeight(this),on.getWidth(this),on.getHeight(this));
        labe4.addMouseListener(this);
        
        ImageLabel labe5 = new ImageLabel(on,off,"Quit");
        labe5.setBounds(0,0,on.getWidth(this),on.getHeight(this));
        labe5.addMouseListener(this);
        
        JLayeredPane layers = new JLayeredPane();
        layers.add(imageLabel, new Integer(1));
        layers.add(woodLabel, new Integer(0));
        layers.add(labe, new Integer(2));
        layers.add(labe2, new Integer(2));
        layers.add(labe3, new Integer(2));
        layers.add(labe4, new Integer(2));
        layers.add(labe5, new Integer(2));
        layers.add(label,new Integer(3));
        
        add(layers);
    }
    
    /**
     * Sets the startup frame to visible
     */
    public void launch()
    {
        setVisible(true);
    }
    
    /**
     * Processes a mouse click to determine actions
     * @param me 
     */
    @Override
    public void mouseClicked(MouseEvent me)
    {
        if(me.getSource() instanceof ImageLabel)
        {
            ImageLabel label = ((ImageLabel)me.getSource());
            if(label.getText().equals("Join Game"))
            {
                String ip = JOptionPane.showInputDialog("Please input IP address to connect to:");
                try
                {
                    ConstantLoader.read();
                    Socket socket = new Socket(ip,GameServer.PORT);
                    InputStream inStream = socket.getInputStream();
                    Scanner in = new Scanner(inStream);
                    Player player = null;
                    String mapName = null;
                    MapName map = null;
                    Map m = null;
                    if(inStream.available() > 0)
                    {
                        player = Player.flip(Player.valueOf(in.nextLine()));
                        mapName = in.nextLine();
                        map = MapName.valueOf(mapName);
                        m = MapReader.readMap(map);
                    }
                    GameFrame frame = new GameFrame(new Game(player,m),m,socket);
                    FrameThreader threader = new FrameThreader(frame);
                    Thread t = new Thread(threader);
                    frame.setVisible(true);
                    t.start();
                    dispose();
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(null,"Error: "+e.getMessage());
                }
            }
            else if(label.getText().equals("New Game"))
            {
                JOptionPane.showMessageDialog(null,"Please select a map to play on.");
                MapSelectFrame frame = new MapSelectFrame(this);
                frame.setVisible(true);
            }
            else if(label.getText().equals("About"))
            {
                TextScreen text = new TextScreen("About.txt");
                text.setVisible(true);
            }
            else if(label.getText().equals("Quit"))
            {
                int choice = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?");
                if(choice == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
            else
            {
                TextScreen text = new TextScreen("Help.txt");
                text.setVisible(true);
            }
        }
    }
    
    /**
     * UNUSED METHOD
     * @param me 
     */
    @Override
    public void mousePressed(MouseEvent me) {}
    
    /**
     * UNUSED METHOD
     * @param me 
     */
    @Override
    public void mouseReleased(MouseEvent me) {}
    
    /**
     * Modifies buttons based on mouse entering
     * @param me mouse event
     */
    @Override
    public void mouseEntered(MouseEvent me)
    {
        if(me.getSource() instanceof ImageLabel)
        {
            ((ImageLabel)me.getSource()).setOn();
        }
        repaint();
    }
    
    /**
     * Modifies buttons based on mouse exiting
     * @param me mouse event
     */
    @Override
    public void mouseExited(MouseEvent me)
    {
        if(me.getSource() instanceof ImageLabel)
        {
            ((ImageLabel)me.getSource()).setOff();
        }
        repaint();
    }
}
