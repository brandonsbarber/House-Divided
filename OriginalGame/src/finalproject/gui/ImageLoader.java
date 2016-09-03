/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * ImageLoader.java
 * Loads images from file based on name
 */

package finalproject.gui;

import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * Class for loading images
 * @author Brandon Barber
 */
public class ImageLoader
{
    
    /**
     * Loads image from given file name
     * @param imageName name of the image to be read
     * @return the image loaded
     */
    public Image loadImage(String imageName)
    {
        try
        {
            return ImageIO.read(getClass().getResource("/images/"+imageName));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Problem loading image! Exiting...");
            System.exit(0);
        }
        return null;
    }
    
}
