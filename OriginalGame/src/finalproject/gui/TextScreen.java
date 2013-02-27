/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * TextScreen.java
 * Screen for displaying a block of text
 */

package finalproject.gui;

import java.awt.BorderLayout;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Text screen for displaying a block of text
 * @author Brandon.barber.1
 */
public class TextScreen extends JFrame
{
    
    private JTextArea text;
    private String s;
    
    /**Constructs a TextScreen with text from the given file.
     * 
     * @param fileName the name of the file to be loaded
     */
    public TextScreen(String fileName)
    {
        Scanner in = null;
        text = new JTextArea();
        text.setEditable(false);
        text.setLineWrap(true);
        
        s = "";
        try
        {
            in = new Scanner(getClass().getResourceAsStream("/"+fileName));
        }
        catch(Exception e)
        {
            s = "Problem loading the file. "+fileName;
        }
        
        if(in != null)
        {
            while(in.hasNextLine())
            {
                s+=in.nextLine()+"\n";
            }
        }
        
        text.setText(s);
        
        setLayout(new BorderLayout());
        
        add(text, BorderLayout.CENTER);
        
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.SOUTH);
        
        setSize(400,400);
        setTitle(fileName.substring(0,fileName.indexOf(".")));
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setLocation(100, 100);
    }
}
