/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * ClientCom.java
 * ClientSide communications class
 */

package finalproject.net;

import finalproject.framework.gameplay.Command;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class ClientCom
{
    private Socket socket;
    private InputStream inStream;
    private OutputStream outStream;
    private Scanner in;
    private PrintWriter out;
    
    /**
     * Client side communicator
     * @param s socket to be used for communication
     */
    public ClientCom(Socket s)
    {
        socket = s;
        
        try
        {
            inStream = s.getInputStream();
            outStream = s.getOutputStream();
            in = new Scanner(inStream);
            out = new PrintWriter(outStream);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Issue with ClientCom!"); 
        }
    }
    
    /**
     * Reads in a command from a server by parsing it
     * @return command
     */
    public Command readFromServer()
    {
        try
        {
            if(!socket.isClosed() && inStream.available() > 0)
            {
                String line = in.nextLine();
                return new Command(line);
            }
        }
        catch(Exception e) {return null;}
        return null;
    }
    
    /**
     * Gives a command to the server
     * @param c command to be given
     */
    public void writeToServer(Command c)
    {
        out.println(c.toString());
        out.flush();
    }
}
