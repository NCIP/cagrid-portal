package gov.nih.nci.cagrid.introduce.extension.example;

import gov.nih.nci.cagrid.introduce.extension.ServiceModificationUIPanel;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import javax.swing.JLabel;

public class ExampleServiceModificationPanel extends ServiceModificationUIPanel {

	private JLabel jLabel = null;

	public ExampleServiceModificationPanel(ServiceInformation info) {
		super(info);
		initialize();
		// TODO Auto-generated constructor stub
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
