package launcher;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import view.Window;

public class Launcher
{

	public Launcher(String[] args)
	{
		Window win = new Window(600,600);
		if(args.length == 0)
			return;
		try {
			Image img = ImageIO.read(new File(args[0]));
			win.getSlate().addImageAt(args[0], 0, 0);
			win.getSlate().setSize(img.getWidth(null), img.getHeight(null));
			win.getSlate().revalidate();
			win.getSlate().repaint();
		} catch (IOException e) {}
	}
	
	public static void main(String[] args) 
	{
		new Launcher(args);
	}
	
}
