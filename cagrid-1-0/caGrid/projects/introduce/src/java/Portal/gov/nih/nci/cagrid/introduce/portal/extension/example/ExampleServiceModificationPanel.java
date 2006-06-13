package gov.nih.nci.cagrid.introduce.portal.extension.example;

import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.ServiceModificationUIPanel;

import javax.swing.JLabel;

public class ExampleServiceModificationPanel extends ServiceModificationUIPanel {

	private JLabel jLabel = null;

	public ExampleServiceModificationPanel(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		super(desc, info);
		initialize();
	}
	

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        jLabel = new JLabel();
        jLabel.setText("Example Editor");
        this.add(jLabel, null);
			
	}

}
