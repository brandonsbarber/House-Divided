/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * MapSelectFrame.java
 * Frame for selecting your desired map
 */

package finalproject.gui;

import finalproject.framework.gameplay.MapName;
import finalproject.net.GameServer;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Upstairs
 */
class MapSelectFrame extends JFrame implements ActionListener
{
    private JComboBox maps;
    private StartupFrame start;
    
    /**
     * Constructs a MapSelectFrame
     * @param start startup frame
     */
    public MapSelectFrame(StartupFrame start)
    {
        setSize(400,400);
        setLayout(new BorderLayout());
        add(new JPanel(),BorderLayout.NORTH);
        add(new JPanel(),BorderLayout.EAST);
        add(new JPanel(),BorderLayout.WEST);
        maps = new JComboBox(MapName.values());
        JButton but = new JButton("Continue...");
        but.addActionListener(this);
        add(but,BorderLayout.SOUTH);
        this.start = start;
        add(maps);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * Performs actions based on what occurs
     * @param e action occurred
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        MapName map = getMap();
        setVisible(false);
        GameServer server = new GameServer(map,start);
        server.startup();
    }
    
    /**
     * Returns the selected map name
     * @return map name
     */
    public MapName getMap()
    {
        return (MapName)maps.getSelectedItem();
    }
}
