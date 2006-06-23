package gov.nih.nci.cagrid.installer.ui.windows;




import gov.nih.nci.cagrid.installer.utils.ImageScaler;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;


import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import java.awt.Color;


public class InstallerWindow extends JWindow{
	
	public static final long serialVersionUID =233249223L;
	
	
	public InstallerWindow(){
		super();
		this.setSize(600,300);
		this.getContentPane().setBackground(Color.BLUE);
		
		//this.add(this.getBackgroundImage());
		//this.setContentPane(background);
		
		this.centerWindow();
	
		
	}
	
	public void centerWindow()
    {
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        Dimension frameSize = this.getSize();
        this.setLocation(center.x - frameSize.width / 2,
                center.y - frameSize.height / 2 - 10);
    }
	
	
	
	
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
