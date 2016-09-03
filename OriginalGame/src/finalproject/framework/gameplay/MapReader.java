/*
 * Brandon Barber
 * Gallatin - 2
 * 5/23/12
 * MapReader.java
 * Class to convert files into maps
 */

package finalproject.framework.gameplay;

import finalproject.gui.Map;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import javax.swing.JOptionPane;

public class MapReader
{
    
    /**
     * Reads a map from the given MapName enum
     * @param s MapName to read in
     * @return read-in map
     */
    public static Map readMap(MapName s)
    {
        Map map = new Map(1);
        try
        {
            MapReader run = new MapReader();
            System.out.println(s.toString());
            Scanner mapReader = new Scanner(run.getClass().getResourceAsStream("/maps/"+s.toString()+".jwmap"));
            ArrayList<String[]> numberLines = new ArrayList<String[]>();
            int dim = mapReader.nextInt();
            mapReader.nextLine();
            for(int gh = 0 ; gh < dim; gh++)
            {
                String[] nums = mapReader.nextLine().split(" ");
                numberLines.add(nums);
            }
            int[][] newMap = new int[numberLines.size()][numberLines.size()];
            
            for(int i = 0; i < numberLines.size();i++)
            {
                for(int h = 0; h < numberLines.get(i).length;h++)
                {
                    newMap[i][h] = Integer.parseInt(numberLines.get(i)[h]);
                }
            }
            map.loadMap(newMap);
            
            TreeMap<Integer,ArrayList<Integer>> spawn1 = map.getSpawn1();
            TreeMap<Integer,ArrayList<Integer>> spawn2 = map.getSpawn2();
            
            String line = "";
            while(mapReader.hasNextLine() && (line = mapReader.nextLine()).contains("spawn1"))
            {
                Scanner in = new Scanner(line);
                in.next();
                int x = in.nextInt();
                spawn1.put(x, new ArrayList<Integer>());
                while(in.hasNextInt())
                {
                    spawn1.get(x).add(in.nextInt());
                }
            }
            
            if(!line.equals(""))
            {
                Scanner in = new Scanner(line);
                in.next();
                int x = in.nextInt();
                spawn2.put(x, new ArrayList<Integer>());
                while(in.hasNextInt())
                {
                    spawn2.get(x).add(in.nextInt());
                }
            }
            while(mapReader.hasNextLine() && (line = mapReader.nextLine()).contains("spawn2"))
            {
                Scanner in = new Scanner(line);
                in.next();
                int x = in.nextInt();
                spawn2.put(x, new ArrayList<Integer>());
                while(in.hasNextInt())
                {
                    spawn2.get(x).add(in.nextInt());
                }
            }
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Corrupted file!");
        }
        return map;
    }
    
    /**
     * UNUSED METHOD: Useful for reading in custom maps, but functionality does not exist yet
     * @param f file to read from
     * @return read-in map
     */
    public static Map readMap(File f)
    {
        Map map = new Map(1);
        try
        {
            MapReader run = new MapReader();
            Scanner mapReader = new Scanner(f);
            ArrayList<String[]> numberLines = new ArrayList<String[]>();
            int dim = mapReader.nextInt();
            mapReader.nextLine();
            for(int gh = 0 ; gh < dim; gh++)
            {
                String[] nums = mapReader.nextLine().split(" ");
                numberLines.add(nums);
            }
            int[][] newMap = new int[numberLines.size()][numberLines.size()];
            
            for(int i = 0; i < numberLines.size();i++)
            {
                for(int h = 0; h < numberLines.get(i).length;h++)
                {
                    newMap[i][h] = Integer.parseInt(numberLines.get(i)[h]);
                }
            }
            map.loadMap(newMap);
            
            TreeMap<Integer,ArrayList<Integer>> spawn1 = map.getSpawn1();
            TreeMap<Integer,ArrayList<Integer>> spawn2 = map.getSpawn2();
            
            String line = "";
            while(mapReader.hasNextLine() && (line = mapReader.nextLine()).contains("spawn1"))
            {
                Scanner in = new Scanner(line);
                in.next();
                int x = in.nextInt();
                spawn1.put(x, new ArrayList<Integer>());
                while(in.hasNextInt())
                {
                    spawn1.get(x).add(in.nextInt());
                }
            }
            
            if(!line.equals(""))
            {
                Scanner in = new Scanner(line);
                in.next();
                int x = in.nextInt();
                spawn2.put(x, new ArrayList<Integer>());
                while(in.hasNextInt())
                {
                    spawn2.get(x).add(in.nextInt());
                }
            }
            while(mapReader.hasNextLine() && (line = mapReader.nextLine()).contains("spawn2"))
            {
                Scanner in = new Scanner(line);
                in.next();
                int x = in.nextInt();
                spawn2.put(x, new ArrayList<Integer>());
                while(in.hasNextInt())
                {
                    spawn2.get(x).add(in.nextInt());
                }
            }
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Corrupted file!");
        }
        return map;
    }
}
