package gov.nih.nci.cagrid.ui.panels.input;

import gov.nih.nci.cagrid.ui.panels.GridPanel;
import gov.nih.nci.cagrid.ui.panels.common.TopPanel;
import gov.nih.nci.cagrid.ui.panels.welcome.WelcomeButtonPanel;
import gov.nih.nci.cagrid.ui.panels.welcome.WelcomeTextPanel;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class InputFormPanel extends GridPanel{

	private static final long serialVersionUID = 1L;
	private InputForm inputextPanel;
	

	public InputFormPanel() {
		
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
		
		
		inputextPanel = new InputForm();
		

		this.add(inputextPanel,BorderLayout.CENTER);
		InputButtonPanel wbp = new InputButtonPanel();
		
		wbp.setBorder(BorderFactory.createEtchedBorder());
		
		this.add(wbp,BorderLayout.PAGE_END);
		
		
	}

	@Override
	public void synch() {
		inputextPanel.synch();
		
	}
	
	

}
