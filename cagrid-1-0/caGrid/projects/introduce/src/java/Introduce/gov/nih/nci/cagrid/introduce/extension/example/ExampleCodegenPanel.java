package gov.nih.nci.cagrid.introduce.extension.example;

import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionUIPanel;
import javax.swing.JLabel;

public class ExampleCodegenPanel extends CodegenExtensionUIPanel {

	private JLabel jLabel = null;

	public ExampleCodegenPanel(ServiceInformation info) {
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
