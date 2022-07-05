package paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImageContainer extends JPanel
{
	private static final long serialVersionUID = 1L;

	private Image img;
	private int[] initialPos = new int[] {0, 0};
	private int[] initialSize = new int[] {0, 0};
	private int k = 4, s1 = 4;
	private String zone;
	
	public ImageContainer(Slate slate, String url)
	{
		super();
		this.img = new ImageIcon(url).getImage();
		if(img == null)
			return;
		this.setOpaque(false);
		this.setBounds(slate.getWidth()/2 - img.getWidth(null)/2, slate.getHeight()/2 - img.getHeight(null)/2, 
						img.getWidth(null), img.getHeight(null));
		
		this.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e)
			{
				initialPos[0] = e.getX();
				initialPos[1] = e.getY();
				initialSize[0] = getWidth();
				initialSize[1] = getHeight();
				zone = getZone(e.getX(), e.getY());
			}
			
			public void mouseEntered(MouseEvent e)
			{
				slate.setCursor(null);
			}
			
			public void mouseExited(MouseEvent e)
			{
				slate.setCursor(slate.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), null));
			}
			
		});
		
		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = getX() + (e.getX() - initialPos[0]);
				int y = getY() + (e.getY() - initialPos[1]);
				int w = (e.getX() - initialPos[0]);
				int h = (e.getY() - initialPos[1]);
				
				switch(zone)
				{
				case "TL":
					setBounds(x, y, getWidth()+(-1)*w, getHeight()+(-1)*h);
					break;
				case "TR":
					setBounds(getX(),  y, initialSize[0]+w, getHeight()+(-1)*h);
					break;
				case "BR":
					setBounds(getX(),  getY(), initialSize[0]+w, initialSize[1]+h);
					break;
				case "BL":
					setBounds(x, getY(), getWidth()+(-1)*w, initialSize[1]+h);
					break;
				default:
					setLocation(getX() + (e.getX() - initialPos[0]), getY() + (e.getY() - initialPos[1]));
					break;
				}
			}
		});
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(img == null)
			return;
		g.drawImage(img, s1, s1, this.getWidth()-2*s1, this.getHeight()-2*s1, null);
		g.setColor(Color.BLUE);
		g.fillRect(s1, s1, k, this.getHeight()-2*s1);
		g.fillRect(this.getWidth()-k-s1, s1, k, this.getHeight()-2*s1);
		g.fillRect(s1, s1, this.getWidth()-2*s1, k);
		g.fillRect(s1, this.getHeight()-k-s1, this.getWidth()-2*s1, k);
		
		g.fillOval(0, 0, s1+k+s1, s1+k+s1);
		g.fillOval(this.getWidth()-s1*2-k, 0, s1+k+s1, s1+k+s1);
		g.fillOval(this.getWidth()-s1*2-k, this.getHeight()-s1*2-k, s1+k+s1, s1+k+s1);
		g.fillOval(0, this.getHeight()-s1*2-k, s1+k+s1, s1+k+s1);
	}
	
	public String getZone(int x, int y)
	{
		if((x >= 0 && x<= s1*2+k) && (y >= 0 && y<= s1*2+k)) // TOP-LEFT
			return "TL";
		else if((x >= this.getWidth()-s1*2-k && x<= this.getWidth()) // TOP-RIGHT
				&& (y >= 0 && y<= s1*2+k))
			return "TR";
		else if((x >= this.getWidth()-s1*2-k && x<= this.getWidth()) 
				&& (y >= this.getHeight()-s1*2-k && y<= this.getWidth())) // BOTTOM-RIGHT
			return "BR";
		else if((x >= 0 && x<= s1*2+k) 
				&& (y >= this.getHeight()-s1*2-k && y<= this.getHeight())) // BOTTOM-LEFT
			return "BL";
			
		return "";
	}
	
	public BufferedImage getImage()
	{
		return resize(img, this.getWidth()-2*s1, this.getHeight()-2*s1);
	}
	
	public Position getPosition()
	{
		return new Position(getX()+s1, getY()+s1);
	}
	
    public static BufferedImage resize(Image image, int scaledWidth, int scaledHeight)
    {
    	BufferedImage outputImage = new BufferedImage(scaledWidth,
	    scaledHeight, BufferedImage.TYPE_INT_ARGB);
	 
	    Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
	
	    return outputImage;
    }
	
}