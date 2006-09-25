package gov.nih.nci.cagrid.gridgrouper.plugin.introduce;

import gov.nih.nci.cagrid.gridgrouper.plugin.ui.GridGrouperExpressionBuilder;
import gov.nih.nci.cagrid.introduce.beans.extension.AuthorizationExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.AbstractAuthorizationPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class GridGrouperPanel extends AbstractAuthorizationPanel {

	private static final long serialVersionUID = 1L;
	private JPanel expressionBuilder = null;

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
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.weighty = 1.0D;
		gridBagConstraints.gridy = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getExpressionBuilder(), gridBagConstraints);
	}

	/**
	 * This method initializes expressionBuilder	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getExpressionBuilder() {
		if (expressionBuilder == null) {
			List groupers = new ArrayList();
			groupers.add("https://140.254.80.109:8443/wsrf/services/cagrid/GridGrouper");
			expressionBuilder = new GridGrouperExpressionBuilder(groupers,false);
		}
		return expressionBuilder;
	}

}
