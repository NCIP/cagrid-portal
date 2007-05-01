package org.cagrid.installer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sun.net.www.protocol.systemresource.SystemResourceURLConnection;

public class CaGridOverviewPanel extends JPanel {

	private JLabel jLabel = null;

	public CaGridOverviewPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        jLabel = new JLabel(new ImageIcon(getClass().getResource("/images/caGrid.jpg")));
        this.add(jLabel, null);
			
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
