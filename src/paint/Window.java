package paint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

public class Window extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private Slate slate;
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu file = new JMenu("File"),
			edit = new JMenu("Edit"),
			form = new JMenu("Brush shape"),
			color = new JMenu("Brush color"),
			size = new JMenu("Brush size"),
			tools = new JMenu("Tools");
	
	private JMenuItem openFile = new JMenuItem("Open File..."),
			clean = new JMenuItem("Clean"),
			quit = new JMenuItem("Quit"),
			undo = new JMenuItem("Undo"),
			redo = new JMenuItem("Redo"),
			screenshot = new JMenuItem("Screenshot");
	
	private ButtonGroup shapeGroup = new ButtonGroup(),
			colorGroup = new ButtonGroup(),
			sizeGroup = new ButtonGroup(),
			toolsGroup = new ButtonGroup();
	
	private JRadioButtonMenuItem[] formButton = {new JRadioButtonMenuItem("Circle"),
			new JRadioButtonMenuItem("Square")};
	
	private JRadioButtonMenuItem[] colorButton = {new JRadioButtonMenuItem("Black"),
			new JRadioButtonMenuItem("Red"),
			new JRadioButtonMenuItem("Green"),
			new JRadioButtonMenuItem("Blue")};
	
	private JRadioButtonMenuItem[] sizeButton = {new JRadioButtonMenuItem("10"),
			new JRadioButtonMenuItem("20"),
			new JRadioButtonMenuItem("30"),
			new JRadioButtonMenuItem("40")};
	
	private JRadioButtonMenuItem[] toolsButton = {new JRadioButtonMenuItem("Brush"),
			new JRadioButtonMenuItem("Selection")};
	
	private JToolBar toolBar = new JToolBar();
	private ToolBarListener TBListener = new ToolBarListener();
	
	private ButtonGroup toolBarGroup = new ButtonGroup();
	
	private JRadioButton brush = new JRadioButton(new ImageIcon(getClass().getClassLoader().getResource("brushIcon2.png"))),
			selection = new JRadioButton(new ImageIcon(getClass().getClassLoader().getResource("selection.png")));
	
	private JRadioButton lastButton = brush;
	
	private Color selectColor = new Color(235, 64, 52);
	
	//private JScrollPane scrollSlate = new JScrollPane(slate, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	
	private JDesktopPane layers = new JDesktopPane();
	
	// Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	
	public Window(int w, int h)
	{
		this.setTitle("Paint v1.0");
		this.setMinimumSize(new Dimension(w, h));
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.initToolBar();
		this.initMenu();
		
		slate = new Slate(this, new Dimension(600, 400), formButton, colorButton, sizeButton, toolsButton);

		slate.setCursor(slate.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), null));
		
		layers.add(new layersWin(getWidth(), getHeight()));
		
		
		this.getContentPane().add(toolBar, BorderLayout.NORTH);
		this.getContentPane().add(slate);
		//this.getContentPane().add(layers);
		this.setVisible(true);
	}

	class layersWin extends JInternalFrame
	{
		private static final long serialVersionUID = 1L;

		layersWin(int w, int h)
		{
		      this.setTitle("Layers");
		      this.setClosable(true);
		      this.setResizable(false);
		      this.setSize(150, 80);
		      this.setLocation(w-150,0);
		      this.setVisible(true);
		}
	}
	
	private void initToolBar()
	{
		brush.setBackground(selectColor);
		brush.setSelected(true);
		brush.setPreferredSize(new Dimension(30,30));
		brush.addActionListener(TBListener);
		toolBarGroup.add(brush);
		toolBar.add(brush);
		
		selection.setPreferredSize(new Dimension(30,30));
		selection.addActionListener(TBListener);
		toolBarGroup.add(selection);
		toolBar.add(selection);
		
		toolBar.setLocation(0, 0);
		
		toolBar.addSeparator();
	}
	
	private void initMenu()
	{
		this.setJMenuBar(menuBar);
		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(tools);
		
		file.add(openFile);
		file.add(clean);
		file.add(screenshot);
		file.add(quit);
		
		edit.add(undo);
		undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
		edit.add(redo);
		redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
		
		edit.addSeparator();
		
		edit.add(form);
		for(JRadioButtonMenuItem f : formButton)
		{
			form.add(f);
			shapeGroup.add(f);
		}
		
		edit.add(color);
		for(JRadioButtonMenuItem c : colorButton)
		{
			color.add(c);
			colorGroup.add(c);
		}
		
		edit.add(size);
		for(JRadioButtonMenuItem s : sizeButton)
		{
			size.add(s);
			sizeGroup.add(s);
		}

		for(JRadioButtonMenuItem t : toolsButton)
		{
			tools.add(t);
			toolsGroup.add(t);
		}
		
		formButton[0].setSelected(true);
		colorButton[0].setSelected(true);
		sizeButton[0].setSelected(true);
		toolsButton[0].setSelected(true);
		
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				slate.undo();
			}
		});
		
		redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				slate.redo();
			}
		});
		
		openFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				slate.addImage();
			}
		});
		
		clean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				slate.clean();
			}
		});
		
		screenshot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				slate.saveImage("Screenshot.jpg");
			}
		});
		
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				slate.saveImage("lastSlate.jpg");
				System.exit(0);
			}
		});
		
        this.addWindowListener(new WindowAdapter(){  
            public void windowClosing(WindowEvent e) {  
				slate.saveImage("lastSlate.jpg");
				System.exit(0);
            }  
        });  
		
	}
	
	  class ToolBarListener implements ActionListener{

		    public void actionPerformed(ActionEvent e) {

		      if(e.getSource().getClass().getName().equals("javax.swing.JRadioButtonMenuItem"));
		      else{
		        if(e.getSource() == brush){
		          toolsButton[0].doClick();
		        }
		        else if(e.getSource() == selection){
		          toolsButton[1].doClick();
		        }
		        
		        lastButton.setBackground(null);
		        lastButton = (JRadioButton) e.getSource();
		        lastButton.setBackground(selectColor);
		      }
		    }    
		  }
	  
	  public void setCursorState(boolean bool)
	  {
		  if(bool)
			  slate.setCursor(null);
		  else
			  slate.setCursor(slate.getToolkit().createCustomCursor(new BufferedImage(3, 3, 
					    BufferedImage.TYPE_INT_ARGB), new Point(0, 0), null));
	  }
	  
	  public Slate getSlate()
	  {
		  return slate;
	  }
	  
	  public JToolBar getToolBar()
	  {
		  return toolBar;
	  }
	  
}

