package gov.nih.nci.cagrid.installer.ui.panels.welcome;

import gov.nih.nci.cagrid.installer.ui.panels.common.TopPanel;

import java.awt.Color;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.border.*;

public class WelcomePanel extends gov.nih.nci.cagrid.installer.ui.panels.GridPanel{

	private static final long serialVersionUID = 1L;
	
	

	public WelcomePanel() {
		
		buildGUI();
		// TODO Auto-generated constructor stub
	}

	private void buildGUI(){
		//this.setSize(500,200);
		
		this.setBackground(Color.BLUE);
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		Border brd = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		this.setBorder(brd);
		
		TopPanel top = new TopPanel();
        top.setBorder(BorderFactory.createEtchedBorder());
		this.add(top,BorderLayout.PAGE_START);
		
		
		WelcomeTextPanel textPanel = new WelcomeTextPanel();
		//textPanel.setBorder(BorderFactory.createEtchedBorder());
		//textPanel.setOpaque(false);
		

		this.add(textPanel,BorderLayout.CENTER);
		WelcomeButtonPanel wbp = new WelcomeButtonPanel();
	
		
		wbp.setBorder(BorderFactory.createEtchedBorder());
		
		this.add(wbp,BorderLayout.PAGE_END);
		
		
	}

	@Override
	public void synch() {
		// TODO Auto-generated method stub
		
	}
	
	

}
