/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * GameServer.java
 * Serverside communications plus startup for host game
 */

package finalproject.net;

import finalproject.framework.entity.ConstantLoader;
import finalproject.framework.gameplay.Game;
import finalproject.framework.gameplay.MapName;
import finalproject.framework.gameplay.MapReader;
import finalproject.framework.gameplay.Player;
import finalproject.gui.FrameThreader;
import finalproject.gui.GameFrame;
import finalproject.gui.Map;
import finalproject.gui.StartupFrame;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JScrollPane;

public class GameServer implements ActionListener, WindowListener
{
    private JComboBox dropDown;
    private JButton confirm;
    private JFrame startFrame, waitingFrame;
    
    private ServerSocket server;
    private ArrayList<TransmitRunnable> runnables;
    private Socket lastRetrieved = null;
    private String command = "";
    private LinkedList<String> commands;
    private String text;
    private JTextArea textArea;
    private JScrollPane pane;
    private Lock lock = new ReentrantLock();
    private Condition cond = lock.newCondition();
    private MapName map;
    private StartupFrame start;
    
    public static final int PORT = 8888;
    
    /**
     * Constructs a new GameServer object
     * @param map mapName to be used
     * @param start initial frame to dispose of
     */
    public GameServer(MapName map,StartupFrame start)
    {
        this.map = map;
        this.start = start;
        startFrame = new JFrame();
        startFrame.setLayout(new GridLayout(3,1,15,15));
        startFrame.add(new JLabel("Please select a faction to play as:"));
        dropDown = new JComboBox();
        dropDown.addItem("UNION");
        dropDown.addItem("CONFEDERATE");
        startFrame.add(dropDown);
        confirm = new JButton("Confirm");
        confirm.addActionListener(this);
        startFrame.add(confirm);
        
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(300,200);
        startFrame.setLocation(100,100);
        startFrame.setAlwaysOnTop(true);
        
        try
        {
            server = new ServerSocket(PORT);
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null,"Error with starting server. Closing program.");
            System.exit(0);
        }
        
        runnables = new ArrayList<TransmitRunnable>();
        commands = new LinkedList<String>();
        waitingFrame = new JFrame();
        textArea = new JTextArea();
        waitingFrame.setLayout(new BorderLayout());
        waitingFrame.add(new JPanel(), BorderLayout.EAST);
        waitingFrame.add(new JPanel(), BorderLayout.SOUTH);
        waitingFrame.add(new JPanel(), BorderLayout.WEST);
        waitingFrame.add(new JLabel("Please tell your opponent this IP Address: "), BorderLayout.NORTH);
        waitingFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pane = new JScrollPane(textArea);
        waitingFrame.add(pane);
        textArea.setEditable(false);
        waitingFrame.setSize(400,200);
        waitingFrame.addWindowListener(this);
        
        try
        {
            text = InetAddress.getLocalHost().toString().substring(InetAddress.getLocalHost().toString().indexOf("/"))+"\n";
            JOptionPane.showMessageDialog(null,"Your server IP is: "+text);
        }
        catch (UnknownHostException ex)
        {
            text = "System Error: "+ex.getMessage()+"\n";
        }
        finally
        {
            textArea.setText(text);
        }
    }
    
    /**
     * Starts the first part of the frame as visible
     */
    public void startup()
    {
        startFrame.setVisible(true);
    }
    
    /**
     * Processes button presses
     * @param e action event
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == confirm)
        {
            Player player = Player.valueOf((String)dropDown.getSelectedItem());
            startFrame.setVisible(false);
            start.setVisible(false);
            Map m = MapReader.readMap(map);
            Socket s = null;
            try
            {
                s = new Socket("localhost",PORT);
            }
            catch (UnknownHostException ex)
            {
                JOptionPane.showMessageDialog(null,ex.getMessage());
            }
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(null,ex.getMessage());
            }
            try
            {
                ConstantLoader.read();
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(null,ex.getMessage());
            }
            GameFrame game = new GameFrame(new Game(player,m),m,s);
            FrameThreader threader = new FrameThreader(game);
            Thread t = new Thread(threader);
            t.start();
            runServer(player,map);
        }
    }
    
    /**
     * Connects sockets and gets server started
     * @param player player of host
     * @param map map being used
     */
    public void runServer(Player player, MapName map)
    {
        waitingFrame.setVisible(true);
        text += "Waiting for connections...\n";
        textArea.setText(text);
        waitingFrame.repaint();
	while(runnables.size() < 2)
	{
            Socket socket = null;
            try
            {
                socket = server.accept();
                System.out.println(socket);
                text+="Connection received from: "+socket.getInetAddress()+"\n";
                textArea.setText(text);
                waitingFrame.repaint();
            }
            catch (IOException ex)
            {
                text += "Error loading socket.\n";
            }
            if(socket != null)
            {
                TransmitRunnable transmit = new TransmitRunnable(socket, commands,this);
                PrintWriter out = null;
                try
                {
                    out = new PrintWriter(socket.getOutputStream());
                }
                catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }
                runnables.add(transmit);
                Thread thread = new Thread(transmit);
                thread.start();
                if(out != null)
                {
                    out.println(player);
                    out.flush();
                    out.println(map);
                    out.flush();
                }
            }
            try
            {
                Thread.sleep(10);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null,"Problem Sleeping...");
            }
	}
    }
    
    /**
     * UNUSED METHOD
     * @param e 
     */
    @Override
    public void windowOpened(WindowEvent e) {}
    
    /**
     * Confirms exiting of the frame
     * @param e window event
     */
    @Override
    public void windowClosing(WindowEvent e)
    {
        if(e.getSource() == waitingFrame)
        {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to close this? Gameplay will end as a result.");
            if(choice == JOptionPane.YES_OPTION)
                System.exit(0);
            waitingFrame.setVisible(true);
        }
    }
    
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
    
    /**
     * Gives the command to the TransmitRunnable based on if commands have been recovered
     * @param s socket pulling from
     * @return string line command
     */
    public String retrieveCommand(Socket s)
    {   
        try
        {
            if(!commands.isEmpty() && commands.peek().indexOf("LOSS") != -1)
            {
                for(TransmitRunnable run : runnables)
                {
                    run.stop();
                }
                try
                {
                    return "";
                }
                finally
                {
                    dispose();
                }

            }
        }
        catch(Exception e)
        {
            return "";
        }
        //if(runnables.size() == 1)
        //    return commands.remove();
        
           if(commands.size() <= 0)
           {
                text+= "Gave   to "+s+"\n";
                textArea.setText(text);
                waitingFrame.repaint();
                return "";
           }
            if(lastRetrieved == null)
            {
                text+= "Gave "+commands.peek()+"  to "+s+"\n";
                textArea.setText(text);
                waitingFrame.repaint();
               lastRetrieved = s;
               return commands.peek();
            }
            else if(lastRetrieved != s)
            {
                text+= "Gave "+commands.peek()+"  to "+s+"\n";
                textArea.setText(text);
                waitingFrame.repaint();
                String toRemove = commands.remove();
                lastRetrieved = null;
                return toRemove;
            }
            return "";
    }
    
    /**
     * Supplements text area in server window
     * @param line text to add
     */
    public void addText(String line)
    {
        text += line+"\n";
        textArea.setText(text);
        waitingFrame.repaint();
    }
    
    /**
     * Stops the server
     */
    public void dispose()
    {
        JOptionPane.showMessageDialog(null,"Killing server");
        waitingFrame.dispose();
        try
        {
            server.close();
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
    }
}
