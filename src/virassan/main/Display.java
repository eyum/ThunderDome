package virassan.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import javax.swing.JFrame;

/**
 * Creates the main Window
 * @author Virassan
 *
 */
public class Display {

	private JFrame frame;
	private Canvas canvas;
	private String title;
	private DisplayMode dm;
	//public static int WIDTH = 1280, HEIGHT = (int)(WIDTH * (9.0f / 16.0f));
	private int width, height;
	private int windowWidth, windowHeight;
	private int displayWidth, displayHeight;
	
	private GraphicsDevice vc;
	//public int WIDTH = 1280, HEIGHT = WIDTH / 16 * 9;
	
	public Display(String title)
	{
		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = genv.getDefaultScreenDevice();
		windowWidth = 1240;
		windowHeight = 900;
		height = windowHeight;
		width = windowWidth;
		displayWidth = vc.getDisplayMode().getWidth();
		displayHeight = vc.getDisplayMode().getHeight();
		dm = new DisplayMode(displayWidth, displayHeight, DisplayMode.BIT_DEPTH_MULTI, DisplayMode.REFRESH_RATE_UNKNOWN);
		this.title = title;
		createDisplay();
	}
	
	public void toggleFullScreen(){
		System.out.println("Update Message: Display_toggleFullScreen b is " + isFullScreen());
		if(!isFullScreen()){
			setFullScreen();
		}else{
			restoreScreen();
		}
	}
	
	private void setFullScreen(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			w.dispose();
		}
		vc.setFullScreenWindow(frame);
		if(dm != null && vc.isDisplayChangeSupported()){
			try{
				vc.setDisplayMode(dm);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		width = displayWidth;
		height = displayHeight;
		frame.setVisible(true);
	}
	
	private void restoreScreen(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			w.dispose();
		}
		vc.setFullScreenWindow(null);
		frame.setVisible(true);
		width = windowWidth;
		height = windowHeight;
	}
	
	public void removeFullScreen(){
		Window w = vc.getFullScreenWindow();
		if(w != null){
			w.dispose();
		}
		vc.setFullScreenWindow(null);
	}
	
	private void createDisplay(){
		frame = new JFrame(title);
		frame.setUndecorated(true);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setFocusable(false);
		
		frame.add(canvas);
		frame.pack();	
	}
	
	
	// GETTERS AND SETTERS
	
	public boolean isFullScreen(){
		if(vc.getFullScreenWindow() == null){
			return false;
		}
		return true;
	}
	
	public JFrame getFrame(){
		return frame;
	}
	
	public Canvas getCanvas(){
		return canvas;
	}
	
	public Window getFullScreenWindow(){
		return vc.getFullScreenWindow();
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
}
