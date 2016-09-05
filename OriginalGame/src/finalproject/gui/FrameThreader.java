/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * FrameThreader.java
 * Designed to continuously repaint frame
 */

package finalproject.gui;

import javax.swing.JOptionPane;

public class FrameThreader implements Runnable
{
    private GameFrame game;
    public static final int FRAME_RATE = 18;
    /**
     * Constructs a thread runnable for continuous repainting
     * @param game gameframe to be painted
     */
    public FrameThreader(GameFrame game)
    {
        this.game = game;
    }
    
    /**
     * Repaints the game while game is going
     */
    @Override
    public void run()
    {
        while(game.running())
        {
            game.repaint();
            try
            {
                Thread.sleep(1000/FRAME_RATE);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null,e.getMessage());
            }
        }
        
        if(!game.getWinner())
            JOptionPane.showMessageDialog(null,"Well, old chap, it appears as though you have lost the Civil War.");
        else
            JOptionPane.showMessageDialog(null,"Congratulations! You have won the Civil War!");
        game.dispose();
        try
        {
            StartupFrame frame = new StartupFrame();
            frame.launch();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Problem with startup.");
        }
    }
}
