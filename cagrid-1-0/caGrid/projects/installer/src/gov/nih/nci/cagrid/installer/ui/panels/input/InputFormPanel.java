package gov.nih.nci.cagrid.installer.ui.panels.input;

import gov.nih.nci.cagrid.installer.ui.panels.common.TopPanel;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class InputFormPanel extends
		gov.nih.nci.cagrid.installer.ui.panels.GridPanel {

	private static final long serialVersionUID = 1L;

	private InputForm inputextPanel;

	public InputFormPanel() {

		buildGUI();
		// TODO Auto-generated constructor stub
	}

	private void buildGUI() {
		// this.setSize(500,200);

		this.setBackground(Color.BLUE);
		this.setLayout(new BorderLayout());
		Border brd = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		this.setBorder(brd);

		TopPanel top = new TopPanel();

		this.add(top, BorderLayout.PAGE_START);

		inputextPanel = new InputForm();

		this.add(inputextPanel, BorderLayout.CENTER);
		InputButtonPanel wbp = new InputButtonPanel();

		wbp.setBorder(BorderFactory.createEtchedBorder());

		this.add(wbp, BorderLayout.PAGE_END);

	}

	public void synch() {
		inputextPanel.synch();

	}

}
