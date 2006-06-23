package gov.nih.nci.cagrid.installer.ui.panels.progress;

import gov.nih.nci.cagrid.installer.ui.panels.GridPanel;
import gov.nih.nci.cagrid.installer.ui.panels.common.TopPanel;
import gov.nih.nci.cagrid.installer.ui.panels.welcome.WelcomeButtonPanel;
import gov.nih.nci.cagrid.installer.ui.panels.welcome.WelcomeTextPanel;


import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class ProgressPanel extends GridPanel{

	private static final long serialVersionUID = 1L;
	private ProgressBarPanel pbp;
	private ProgressButtonPanel buttonPanel;

	public ProgressPanel() {
		
		buildGUI();
		// TODO Auto-generated constructor stub
	}

	private void buildGUI(){
		//this.setSize(500,200);
		
		
		this.setBackground(Color.BLUE);
		this.setLayout(new BorderLayout());
		Border brd = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		this.setBorder(brd);
		
		TopPanel top = new TopPanel();

		this.add(top,BorderLayout.PAGE_START);
		
		
		pbp = new ProgressBarPanel();
		

		this.add(pbp,BorderLayout.CENTER);
		 buttonPanel = new ProgressButtonPanel();
		
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		
		this.add(buttonPanel,BorderLayout.PAGE_END);
		
		
	}

	@Override
	public void synch() {
		// TODO Auto-generated method stub
		
	}
	public void install(){
		
		pbp.installComponents();
		//buttonPanel.activateFinish();
		
	}
	

}

