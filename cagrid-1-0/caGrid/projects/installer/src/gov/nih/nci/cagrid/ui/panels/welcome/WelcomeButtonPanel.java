package gov.nih.nci.cagrid.ui.panels.welcome;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import gov.nih.nci.cagrid.installer.utils.ImageScaler;
import gov.nih.nci.cagrid.installer.workers.Installer;
import gov.nih.nci.cagrid.ui.panels.GridPanel;


public class WelcomeButtonPanel extends GridPanel implements ActionListener{

   /**
	 * 
	 */
	private static final long serialVersionUID = 168768689L;

public WelcomeButtonPanel() {
		
		this.setBackground(Color.BLUE);
		addButtons();
		// TODO Auto-generated constructor stub
	}
 private void addButtons(){
	 next = new JButton("Next");
	 quit = new JButton("Quit");
	 next.addActionListener(this);
	 quit.addActionListener(this);
	 this.add(next);
	 this.add(quit);
 }
 
 public void actionPerformed(ActionEvent evt)
 {  Object source = evt.getSource();
    
    if (source == next){
    	System.out.println("next");
    	Installer is = Installer.getInstance();
    	is.next();
    }
    if(source == quit){
    	System.out.println("quit");
    	Installer is = Installer.getInstance();
    	is.quit();
    }
 }
 
 public void paintComponent(Graphics g)
 {  super.paintComponent(g);
   this.setForeground(Color.BLUE);

    
    
    super.paintComponent(g);
    ImageScaler is = new ImageScaler();
		ImageIcon icon = new ImageIcon(is.getScaledInstance("images/control.jpg",800,75));
		Image image = icon.getImage();
    if(image != null) g.drawImage(image, 0,0,800,75,this);
    

    
 }
	private JButton next;
	private JButton quit;

	@Override
	public void synch() {
		// TODO Auto-generated method stub
		
	}

}
