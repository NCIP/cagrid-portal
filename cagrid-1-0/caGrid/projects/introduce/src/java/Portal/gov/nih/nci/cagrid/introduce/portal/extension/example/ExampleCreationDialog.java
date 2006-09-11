package gov.nih.nci.cagrid.introduce.portal.extension.example;

import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.CreationExtensionUIDialog;

import java.awt.Frame;

import javax.swing.JButton;

public class ExampleCreationDialog extends CreationExtensionUIDialog {

	private JButton jButton = null;

	/**
	 * This method initializes 
	 * 
	 */
	public ExampleCreationDialog(Frame f, ServiceExtensionDescriptionType desc, ServiceInformation info) {
		super(f, desc, info);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new java.awt.Dimension(308,214));
        this.setContentPane(getJButton());
			
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("DONE");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return jButton;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
