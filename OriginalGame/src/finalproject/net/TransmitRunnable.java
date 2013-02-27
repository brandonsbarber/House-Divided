/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * TransmitRunnable.java
 * Runnable for processing and divvying out commands on server side
 */

package finalproject.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JOptionPane;


public class TransmitRunnable implements Runnable
{

    private Socket socket;
    private LinkedList<String> commands;
    private GameServer server;
    
    private InputStream inStream;
    private OutputStream outStream;
    private Scanner in;
    private PrintWriter out;
    private boolean running = true;
    
    /**
     * Constructs a new TransmitRunnable
     * @param s socket to be used
     * @param c queue of commands
     * @param serve gameserver object
     */
    public TransmitRunnable(Socket s, LinkedList<String> c, GameServer serve)
    {
        socket = s;
        commands = c;
        server = serve;

        try
        {
            inStream = socket.getInputStream();
            outStream = socket.getOutputStream();
            in = new Scanner(inStream);
            out = new PrintWriter(outStream);
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
    }
    
    /**
     * Runs the communications program
     */
    @Override
    public void run()
    {
        try
        {
            while(running)
            {
                try
                {
                    readFromSocket();
                    writeToSocket();
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(null,e.getMessage());
                }
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
        finally
        {
            try
            {
                inStream.close();
                outStream.close();
            }
            catch(Exception exe)
            {
            }
            in.close();
            out.close();
        }
    }
    
    /**
     * Reads in a line from the socket
     * @throws IOException 
     */
    public void readFromSocket() throws IOException
    {
        if(inStream.available() <= 0)
            return;
        String line = in.nextLine();
	commands.add(line);
        server.addText(line);
        System.out.println(line);
    }
    
    /**
     * Writes a command line to the socket
     */
    public void writeToSocket()
    {
        String command;
        if(commands.size() > 0 && !(command = server.retrieveCommand(socket)).equals(""))
        {
            out.println(command);
            out.flush();
        }
    }
    
    /**
     * Gets the socket being used by the runnable
     * @return the socket
     */
    public Socket getSocket()
    {
        return socket;
    }
    
    /**
     * Stops the runnable
     */
    public void stop()
    {
        running = false;
    }
}
