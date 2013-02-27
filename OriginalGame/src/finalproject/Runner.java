/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * Runs the game
 */

package finalproject;

import finalproject.gui.StartupFrame;
import javax.swing.JOptionPane;

public class Runner {

    /**
     * Runner for the project
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            StartupFrame frame = new StartupFrame();
            frame.launch();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }
}
