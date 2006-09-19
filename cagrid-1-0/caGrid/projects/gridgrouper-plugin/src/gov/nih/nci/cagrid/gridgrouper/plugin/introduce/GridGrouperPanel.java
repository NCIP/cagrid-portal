package gov.nih.nci.cagrid.gridgrouper.plugin.introduce;

import gov.nih.nci.cagrid.introduce.beans.extension.AuthorizationExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.AbstractAuthorizationPanel;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

public class GridGrouperPanel extends AbstractAuthorizationPanel {

	private static final long serialVersionUID = 1L;
	private JLabel gridGrouper = null;

	public GridGrouperPanel(AuthorizationExtensionDescriptionType authDesc,
			ServiceInformation serviceInfo) {
		super(authDesc, serviceInfo);
		initialize();
	}

	public AuthorizationExtensionDescriptionType getAuthorizationExtensionDescriptionType() {
		// TODO Auto-generated method stub
		return super.getAuthorizationExtensionDescriptionType();
	}

	public ServiceInformation getServiceInformation() {
		// TODO Auto-generated method stub
		return super.getServiceInformation();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		gridGrouper = new JLabel();
		gridGrouper.setText("Grid Grouper");
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(gridGrouper, new GridBagConstraints());
	}

}
