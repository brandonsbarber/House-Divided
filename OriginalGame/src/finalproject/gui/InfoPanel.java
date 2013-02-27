/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * InfoPanel.java
 * Panel for displaying information to the player during the game
 */

package finalproject.gui;

import finalproject.framework.gameplay.Game;
import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InfoPanel extends JPanel
{
    private Game game;
    private JTextField money, troops,training,comP,maxP;
    
    /**
     * Side info panel in game menu
     * @param game game object
     */
    public InfoPanel(Game game)
    {
        this.game = game;
        JLabel playerText = new JLabel("Player: ");
        JLabel player = new JLabel(game.getPlayer().toString());
        
        JLabel moneyLabel = new JLabel("Money: ");
        money = new JTextField(15);
        money.setHorizontalAlignment(JTextField.RIGHT);
        money.setText(""+game.getMoney());
        money.setEditable(false);
        
        JLabel troopNumber = new JLabel("Troop Count: ");
        troops = new JTextField(4);
        troops.setHorizontalAlignment(JTextField.RIGHT);
        troops.setText(""+game.getNumTroops());
        troops.setEditable(false);
        
        JLabel currentSpawn = new JLabel("Training: ");
        training = new JTextField(15);
        training.setHorizontalAlignment(JTextField.RIGHT);
        training.setEditable(false);
        if(game.getSpawnUnitType() != null)
        {
            training.setText(game.getSpawnUnitType().toString());
        }
        else
        {
            training.setText("No units.");
        }
        
        JLabel cp = new JLabel("Command Points: ");
        comP = new JTextField(15);
        comP.setHorizontalAlignment(JTextField.RIGHT);
        comP.setEditable(false);
        comP.setText(""+game.getCP());
        
        JLabel maxp = new JLabel("Max CP: ");
        maxP = new JTextField(15);
        maxP.setHorizontalAlignment(JTextField.RIGHT);
        maxP.setEditable(false);
        maxP.setText(""+game.getMaxCP());
        
        setLayout(new GridLayout(0,2,10,0));
        add(playerText);
        add(player);
        add(moneyLabel);
        add(money);
        add(troopNumber);
        add(troops);
        add(currentSpawn);
        add(training);
        add(cp);
        add(comP);
        add(maxp);
        add(maxP);
    }
    
    /**
     * Updates all fields
     */
    public void update()
    {
        money.setText(""+game.getMoney());
        troops.setText(""+game.getNumTroops());
        if(game.getSpawnUnitType() != null)
        {
            training.setText(game.getSpawnUnitType().toString());
        }
        else
        {
            training.setText("No units.");
        }
        comP.setText(""+game.getCP());
        maxP.setText(""+game.getMaxCP());
    }
}
