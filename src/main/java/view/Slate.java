package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

import model.Point;
import model.Position;

public class Slate extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private Window win;
	private JRadioButtonMenuItem[] formButton, colorButton, sizeButton, toolsButton;
	private List<Point> draw = new ArrayList<Point>();
	private Point cursor;
	private int[] oldSelection = new int[2], selection = new int[2];
	private Image lastSlate;
	private ImageContainer imgC;
	private int spaceCoin = 6;
	private boolean coinPressed = false;
	
	private Stack<Object> objects = new Stack<Object>(); // ImageContainer, Point, Rectangle...
	private Stack<Object> redoObjects = new Stack<Object>();

	public Slate(Window win, Dimension dim, JRadioButtonMenuItem[] formButton, JRadioButtonMenuItem[] colorButton, JRadioButtonMenuItem[] sizeButton, JRadioButtonMenuItem[] toolsButton)
	{
		//lastSlate = new ImageIcon("lastSlate.jpg").getImage();
		this.setLayout(null);
		this.formButton = formButton;
		this.colorButton = colorButton;
		this.sizeButton = sizeButton;
		this.toolsButton = toolsButton;
		this.cursor = new Point(getBrushColor(), getBrushSize(), getBrushForm(), new Position(-100,-100));
		
		
		this.setBackground(Color.WHITE);
		
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) 
			{
				if(isOnCoin(e.getX(), e.getY()))
				{
					coinPressed = true;
					selection[0] = getWidth();
					selection[1] = getHeight();
					setCursor(null);
				}
				if(getSelectionButtonState() || isOnCoin(e.getX(), e.getY()))
				{
					oldSelection[0] = e.getX();
					oldSelection[1] = e.getY();
					selection[0] = e.getX();
					selection[1] = e.getY();
				}
				else if(getBrushButtonState())
					addPoint(e);
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) 
			{
				if(getBrushButtonState())
				{
					objects.push(draw);
					draw = new ArrayList<Point>();
					redoObjects.clear();
				}
				
				if(coinPressed)
				{
					coinPressed = false;
					win.setCursorState(true);
				}
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) 
			{
				cursor.setPosition(-999,-999);
				repaint();
			}
		});
		
		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) 
			{
				if(isOnCoin(e.getX(), e.getY()))
				{
					setCursor(null);
					cursor.setPosition(-999, -999);
					repaint();
					return;
				}
				cursor = new Point(getBrushColor(), getBrushSize(), getBrushForm(), new Position(e.getX(), e.getY()));
				
				if(getSelectionButtonState())
				{
					cursor.setForm(2);
				}
				
				repaint();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) 
			{
				if(coinPressed)
				{
					int w = (e.getX() - oldSelection[0]);
					int h = (e.getY() - oldSelection[1]);
					setSize(selection[0]+w, selection[1]+h);
				}
				else if(getBrushButtonState())
					addPoint(e);
				else if(getSelectionButtonState())
				{
					selection[0] = e.getX();
					selection[1] = e.getY();
					repaint();
				}
			}
		});
		
		this.addKeyListener(new KeyAdapter() {
			
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if(imgC != null)
					{
						objects.push(imgC);
						redoObjects.clear();
						removeAll();
						repaint();
					}
				}
				else if(e.getKeyCode() == KeyEvent.VK_DELETE)
				{
					if(selection[0] - oldSelection[0] > 0 &&
					   selection[1] - oldSelection[1] > 0) {
						addRectangle(Color.WHITE, new Position(oldSelection[0], oldSelection[1]),
						new Dimension(selection[0] - oldSelection[0], selection[1] - oldSelection [1]));
						selection[0] = 0;
						selection[1] = 0;
						oldSelection[0] = 0;
						oldSelection[1] = 0;
						revalidate();
						repaint();
					}
				}
			}
			
		});
		
		this.setFocusable(true);
		this.requestFocus();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// Il vaut mieux faire un fichier special et enregistrer la liste object.
		if(lastSlate != null)
			g.drawImage(lastSlate, 0, 0, lastSlate.getWidth(null), lastSlate.getHeight(null), null);

		for(Object obj : objects)
		{
			if(obj instanceof DeleteZone) { // When you delete a zone
				g.setColor(((DeleteZone)obj).getColor());
				((Graphics2D)g).fill(((DeleteZone)obj));
			}
			else if(obj instanceof ImageContainer) {
				ImageContainer imgCo = (ImageContainer)obj;
				g.drawImage(imgCo.getImage(), imgCo.getPosition().getX(),
							imgCo.getPosition().getY(), imgCo.getImage().getWidth(null),
							imgCo.getImage().getHeight(null), null);
			}
			else if(obj instanceof ArrayList) {
				//@SuppressWarnings(“unchecked”)
				ArrayList<Point> points = (ArrayList<Point>)obj;
				for(Point p : points) {
					p.paint(g);
				}
			}
		}

		for(Point d : draw)
			d.paint(g);

		if(getSelectionButtonState())
		{
			float opacity = 0.5f;
			g.setColor(new Color(255, 0, 0,(int)(255f*opacity)));
			g.fillRect(oldSelection[0], oldSelection[1], selection[0]-oldSelection[0], selection[1]-oldSelection[1]);
		}
		
		g.setColor(Color.GRAY);
		g.fillRect(this.getWidth()-spaceCoin, this.getHeight()-spaceCoin, spaceCoin, spaceCoin);
		
		cursor.paint(g);
		this.requestFocus();
	}
	
	public void addRectangle(Color color, Position pos, Dimension dim) {
		DeleteZone zone = new DeleteZone(color, pos, dim);
		objects.add(zone);
	}

	public void addPoint(MouseEvent e)
	{
		Point p = new Point(getBrushColor(), getBrushSize(), getBrushForm(), new Position(e.getX(), e.getY()));
		if(draw.contains(p))
		{
			//System.out.println("EXISTE DEJA");
		}
		else
		{
			draw.add(p);
			//System.out.println("NOUVEAU POINT");
			repaint();
		}
	}
	
	public void addImage()
	{
		JFileChooser choice = new JFileChooser();
		int var = choice.showOpenDialog(this);
		if(var==JFileChooser.APPROVE_OPTION)
		{
		   //choice.getSelectedFile().getName();
		   imgC = new ImageContainer(this, choice.getSelectedFile().getAbsolutePath());
		   this.add(imgC);
		   //BufferedImage outputImg = new BufferedImage();
		   
		}
	}
	
    public static void resize(String inputImagePath, String outputImagePath, int scaledWidth, int scaledHeight) throws IOException 
    {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);
 
        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }
	
	public void undo()
	{
		if(!objects.empty())
			redoObjects.push(objects.pop());
		repaint();
	}
	
	public void redo()
	{
		if(!redoObjects.empty())
			objects.push(redoObjects.pop());
		repaint();
	}
	
	public int getBrushSize()
	{
		for(JRadioButtonMenuItem s : sizeButton)
		{
			if(s.isSelected())
			{
				return Integer.parseInt(s.getText());
			}
		}
		
		return 10;
	}
	
	public int getBrushForm()
	{
		for(JRadioButtonMenuItem f : formButton)
		{
			if(f.isSelected())
			{
				switch(f.getText())
				{
				case "Circle":
					return 0;
				case "Square":
					return 1;
				default:
					return 0;
				}
			}
		}
		
		return 0;
	}
	
	public Color getBrushColor()
	{
		for(JRadioButtonMenuItem c : colorButton)
		{
			if(c.isSelected())
			{
				switch(c.getText())
				{
				case "Black":
					return Color.BLACK;
				case "Red":
					return Color.RED;
				case "Green":
					return Color.GREEN;
				case "Blue":
					return Color.BLUE;
				default:
					return Color.BLACK;
				}
			}
		}
		
		return Color.BLACK;
	}
		
	public BufferedImage getScreenshot()
	{
		BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		this.paint(img.getGraphics());
		return img;
	}
	
	public void saveImage(String name)
	{
		File output = new File(name);
		BufferedImage img = getScreenshot();
		try {
			ImageIO.write(img, "JPG", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void clean()
	{
		objects.clear();
		redoObjects.clear();
		draw.clear();
		lastSlate = null;
		(new File("lastSlate.jpg")).delete();
		repaint();
	}
	
	public boolean getBrushButtonState()
	{
		return toolsButton[0].isSelected();
	}
	
	public boolean getSelectionButtonState()
	{
		return toolsButton[1].isSelected();
	}
	
	public boolean isOnCoin(int x, int y)
	{
		if((x >= this.getWidth()-spaceCoin && x <= this.getWidth())
			&& (y >= this.getHeight()-spaceCoin && y <= this.getHeight()))
			return true;
		return false;
	}

	public Window getWin() 
	{
		return win;
	}
	
	public void addImageAt(String path, int x, int y)
	{
		ImageContainer imgCo = new ImageContainer(this, path);
		imgCo.setLocationForImage(x, y);
		objects.add(imgCo);
	}
}
