import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UMLBuilder
{
	private static PrintWriter out;
	public static int lineCount = 0;

	public static void main(String[] args) throws Exception
	{
		JFileChooser c = new JFileChooser();
		c.showOpenDialog(null);
		File f = c.getSelectedFile();
		String fileExtension = f.getParent();
		while(!fileExtension.substring(fileExtension.lastIndexOf("\\")+1).equals("src"))
		{
			File file = new File(fileExtension);
			fileExtension = file.getParent();
		}
		c.showSaveDialog(null);
		out = new PrintWriter(c.getSelectedFile().getAbsolutePath()+".txt");
		scanDir(new File(fileExtension));
		out.println("LINE COUNT: "+lineCount);
		out.close();
	}

	public static void scanFile(File f) throws Exception
	{
		Scanner in = new Scanner(f);
		ArrayList<String> lines = new ArrayList<String>();
		while(in.hasNextLine())
		{
			lines.add(in.nextLine());
			lineCount++;
		}
		for(int i = 0; i < lines.size(); i++)
		{
			String line = lines.get(i);
			if((line.contains("public") || line.contains("private") || line.contains("package")))
			{
				if(line.contains("="))
				{
					line = line.substring(0,line.indexOf("="));
					String[] pieces = line.split(" ");
					lines.set(i,pieces[pieces.length-1]);
				}
				else if(line.contains(";"))
				{
					String[] pieces = line.split(" ");
					lines.set(i,pieces[pieces.length-1].substring(0,pieces[pieces.length-1].length()-1));
				}
				else
				{
					String[] pieces = line.split(" ");
					int index = 0;
					while(index < pieces.length && pieces[index].indexOf("(") == -1)
						index++;

					String str = "";
					for(int ind = index; ind < pieces.length; ind++)
					{
						str += pieces[ind]+" ";
					}
					lines.set(i,str);
				}
			}
			else
			{
				lines.remove(i);
				i--;
			}
		}
		for(String s:lines)
		{
			out.println(s);
		}
		out.println();
	}

	public static void scanDir(File file) throws Exception
	{
		File[] directories = file.listFiles();
		for(File f:directories)
		{
			if(f.isDirectory())
				scanDir(f);
			else if(f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf(".")).equals(".java"))
				scanFile(f);
		}
	}
}