package gov.nih.nci.cagrid.ui.panels.welcome;


import gov.nih.nci.cagrid.installer.utils.ImageScaler;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WelcomeTextPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 	private Font f;
	   
	   private FontMetrics fm;
	   
	   public WelcomeTextPanel(){
		   this.setBackground(Color.BLUE);
		   //this.setLayout(new BorderLayout());
		   //this.add(this.getBackgroundImage());
	   }
	
	public void setFonts(Graphics g)
	   {  if (f != null) return;
	      f = new Font("SansSerif", Font.BOLD, 36);
	      
	      fm = g.getFontMetrics(f);
	      
	   }

	public void paintComponent(Graphics g)
	   {  
		/**
		 
		super.paintComponent(g);
	     this.setForeground(Color.BLUE);

	      setFonts(g);
	      String s1 = "caGrid 1.0 Installer";
	      
	      int w1 = fm.stringWidth(s1);
	      

	      Dimension d = getSize();
	      int cx = (d.width - w1) / 2;
	      int cy = (d.height - fm.getHeight()) / 2 
	         + fm.getAscent();
	      
	      g.setFont(f);
	      g.drawString(s1, cx, cy);
	      */
	      super.paintComponent(g);
	      ImageScaler is = new ImageScaler();
			ImageIcon icon = new ImageIcon(is.getScaledInstance("images/brownbackground.jpg",601,240));
			Image image = icon.getImage();
	      if(image != null) g.drawImage(image, 0,0,601,240,this);
	      //g.drawString(s1, cx, cy);

	      
	   }
	
	private JLabel getBackgroundImage(){
		ImageScaler is = new ImageScaler();
		ImageIcon icon = new ImageIcon(is.getScaledInstance("background.jpg",600,300));
		JLabel background = new JLabel(icon);
		background.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
		
		this.add(background, new Integer(Integer.MIN_VALUE));
		return background;
	}


}
