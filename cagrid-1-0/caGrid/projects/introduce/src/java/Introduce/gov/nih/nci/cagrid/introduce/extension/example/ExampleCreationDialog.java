package gov.nih.nci.cagrid.introduce.extension.example;

import gov.nih.nci.cagrid.introduce.extension.CreationExtensionUIDialog;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import javax.swing.JDialog;
import javax.swing.JButton;

public class ExampleCreationDialog extends CreationExtensionUIDialog {

	private JButton jButton = null;

	/**
	 * This method initializes 
	 * 
	 */
	public ExampleCreationDialog(ServiceInformation info) {
		super(info);
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
