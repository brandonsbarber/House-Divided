/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * ConstantLoader.java
 * Class for loading in constant values from XML file
 */

package finalproject.framework.entity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ConstantLoader
{
    public static ArrayList<UnitConstant> units;
    public static ArrayList<BuildingConstant> buildings;
    
    /**
     * Reads in a file of constants
     * @throws XPathException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     */
    public static void read() throws XPathException, ParserConfigurationException, SAXException, IOException
    {
        ConstantLoader loader = new ConstantLoader();
        
        InputStream file = null;
            JOptionPane.showMessageDialog(null,"Executing?");
            file = loader.getClass().getResourceAsStream("/EntityConstants.xml");
            JOptionPane.showMessageDialog(null,"Executing2?");
        
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
	XPath path = XPathFactory.newInstance().newXPath();
        
        units = new ArrayList<UnitConstant>();
        buildings = new ArrayList<BuildingConstant>();
        
        int numUnits = Integer.parseInt(path.evaluate("count(/constants/unit)",doc));
        
        for(int i = 1; i <= numUnits; i++)
        {
            int width = Integer.parseInt(path.evaluate("/constants/unit["+i+"]/width",doc));
            int height = Integer.parseInt(path.evaluate("/constants/unit["+i+"]/height",doc));
            int speed = Integer.parseInt(path.evaluate("/constants/unit["+i+"]/speed",doc));
            int rate = Integer.parseInt(path.evaluate("/constants/unit["+i+"]/rate",doc));
            
            int numTypes = Integer.parseInt(path.evaluate("count(/constants/unit["+i+"]/types/type)",doc));
            for(int t = 1; t <= numTypes; t++)
            {
                String name = path.evaluate("/constants/unit["+i+"]/types/type["+t+"]/name",doc);
                int health = Integer.parseInt(path.evaluate("/constants/unit["+i+"]/types/type["+t+"]/health",doc));
                int damage = Integer.parseInt(path.evaluate("/constants/unit["+i+"]/types/type["+t+"]/damage",doc));
                int price = Integer.parseInt(path.evaluate("/constants/unit["+i+"]/types/type["+t+"]/price",doc));
                int radius = Integer.parseInt(path.evaluate("/constants/unit["+i+"]/types/type["+t+"]/range",doc));
                int cp = Integer.parseInt(path.evaluate("/constants/unit["+i+"]/types/type["+t+"]/cp",doc));
                units.add(new UnitConstant(name,width,height,health,price,speed,damage,radius,rate,cp));
            }
        }
        
        int numBuildings = Integer.parseInt(path.evaluate("count(/constants/building)",doc));
        
        for(int b = 1; b <= numBuildings; b++)
        {
            String name = path.evaluate("constants/building["+b+"]/name", doc);
            int width = Integer.parseInt(path.evaluate("constants/building["+b+"]/width", doc));
            int height = Integer.parseInt(path.evaluate("constants/building["+b+"]/height", doc));
            int health = Integer.parseInt(path.evaluate("constants/building["+b+"]/health", doc));
            int price = Integer.parseInt(path.evaluate("constants/building["+b+"]/price", doc));
            int spawnX = Integer.parseInt(path.evaluate("constants/building["+b+"]/spawn/x",doc));
            int spawnY = Integer.parseInt(path.evaluate("constants/building["+b+"]/spawn/y",doc));
            int cp = Integer.parseInt(path.evaluate("constants/building["+b+"]/cp",doc));
            buildings.add(new BuildingConstant(name,width,height,health,price, spawnX, spawnY,cp));
        }
    }
    
    /**
     * Gets the constant for the given entity type
     * @param type type's constant to look for
     * @return constant with corresponding type
     */
    public static Constant getConstant(EntityType type)
    {
        if(type.ordinal() >= EntityType.LINF.ordinal() && type.ordinal() <= EntityType.HART.ordinal())
        {
            return (Constant)units.get(type.ordinal());
        }
        else if(type.ordinal() >= EntityType.HQ.ordinal() && type.ordinal() <= EntityType.STABLES.ordinal())
        {
            return (Constant)buildings.get(type.ordinal()-EntityType.HQ.ordinal());
        }
        return null;
    }
}
