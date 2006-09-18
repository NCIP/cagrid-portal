package gov.nih.nci.cagrid.gridgrouper.plugin.introduce;

import gov.nih.nci.cagrid.introduce.beans.extension.AuthorizationExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.AbstractAuthorizationPanel;

import java.awt.GridBagLayout;

public class GridGrouperPanel extends AbstractAuthorizationPanel {

	private static final long serialVersionUID = 1L;

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
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
	}

}
