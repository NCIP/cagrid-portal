package gov.nih.nci.cagrid.ui.panels.info;

import gov.nih.nci.cagrid.ui.panels.GridPanel;
import gov.nih.nci.cagrid.ui.panels.common.TopPanel;


import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class InfoPanel extends GridPanel{

	private static final long serialVersionUID = 1L;
	private InfoButtonPanel ibp;
	

	public InfoPanel() {
		
		buildGUI();
		// TODO Auto-generated constructor stub
	}

	private void buildGUI(){
		//this.setSize(500,200);
		this.setBackground(Color.BLUE);
		this.setLayout(new BorderLayout());
		Border bevel = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		this.setBorder(bevel);
		
		TopPanel top = new TopPanel();

		
		//this.add(top,c);
		this.add(top,BorderLayout.PAGE_START);
		
		
		InfoTextPanel textPanel = new InfoTextPanel();
		

		this.add(textPanel,BorderLayout.CENTER);
		ibp = new InfoButtonPanel();
		
		ibp.setBorder(BorderFactory.createEtchedBorder());
		
		this.add(ibp,BorderLayout.PAGE_END);
		
		
	}

	@Override
	public void synch() {
		// TODO Auto-generated method stub
		//System.out.println("shit");
		ibp.synch();
	}
	
	

}
