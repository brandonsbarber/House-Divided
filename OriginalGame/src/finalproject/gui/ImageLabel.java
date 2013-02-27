/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * ImageLabel.java
 * JLabel button that changes based on whether cursor is over it or not
 */

package finalproject.gui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JLabel;

public class ImageLabel extends JLabel
{
    private Image on,off;
    private String t;
    
    private boolean act;
    
    private Font f;
    
    /**
     * Button based off of two images
     * @param on on image
     * @param off off image
     * @param t text to be used
     */
    public ImageLabel(Image on, Image off, String t)
    {
        this.on = on;
        this.off = off;
        this.t = t;
        act = false;
        f = new Font("Charlemagne Std",Font.PLAIN,36);
    }
    
    /**
     * Paints the button
     * @param g graphics on which to paint
     */
    @Override
    public void paintComponent(Graphics g)
    {
        if(act)
        {
            g.drawImage(on, 0, 0, this);
        }
        else
        {
            g.drawImage(off,0,0,this);
        }
        
        g.setFont(f);
        int length = t.length()*28;
        
        g.drawString(t, on.getWidth(this)/2-length/2, on.getHeight(this)/2+14);
    }
    
    /**
     * Sets the button to on
     */
    public void setOn()
    {
        act = true;
    }
    
    /**
     * Sets the button to off
     */
    public void setOff()
    {
        act = false;
    }
    
    /**
     * Gets the text of the button
     * @return text
     */
    @Override
    public String getText()
    {
        return t;
    }
    
    
    
    
}
