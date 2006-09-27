package gov.nih.nci.cagrid.gridgrouper.plugin.introduce;

import gov.nih.nci.cagrid.gridgrouper.bean.MembershipExpression;
import gov.nih.nci.cagrid.gridgrouper.plugin.ui.GridGrouperExpressionBuilder;
import gov.nih.nci.cagrid.introduce.beans.extension.AuthorizationExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.PropertiesProperty;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.AbstractServiceAuthorizationPanel;
import gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.xml.namespace.QName;

import org.apache.axis.message.MessageElement;

public class GridGrouperServicePanel extends AbstractServiceAuthorizationPanel {

	private static final String LOAD_ON_STARTUP = "loadOnStartup"; // @jve:decl-index=0:

	private static final String GRID_GROUPER_URL = "gridGrouperURL"; // @jve:decl-index=0:

	private static final long serialVersionUID = 1L;

	private JPanel expressionBuilder = null;

	public GridGrouperServicePanel(
			AuthorizationExtensionDescriptionType authDesc,
			ServiceInformation serviceInfo, ServiceType service) {
		super(authDesc, serviceInfo, service);
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
			boolean loadOnStartup = false;
			List groupers = new ArrayList();
			PropertiesProperty[] props = getAuthorizationExtensionDescriptionType()
					.getProperties().getProperty();
			if (props != null) {
				for (int i = 0; i < props.length; i++) {
					if (props[i].getKey().equals(LOAD_ON_STARTUP)) {
						if (props[i].getValue().equalsIgnoreCase("true")) {
							loadOnStartup = true;
						}
					} else if (props[i].getKey().equals(GRID_GROUPER_URL)) {
						groupers.add(props[i].getValue());
					}
				}
			}
			expressionBuilder = new GridGrouperExpressionBuilder(groupers,
					loadOnStartup);
		}
		return expressionBuilder;
	}

	public void save() throws Exception {
		MembershipExpression exp = ((GridGrouperExpressionBuilder) getExpressionBuilder())
				.getMembershipExpression();

		// TODO: Validate the expression

		ExtensionType ext = ExtensionTools.getAddServiceExtension(
				"gridgrouper", getServiceInformation());
		ExtensionTypeExtensionData data = ext.getExtensionData();
		GridGrouperPlugin plugin = null;
		MessageElement[] mes = data.get_any();
		if (mes == null) {
			mes = new MessageElement[1];
			data.set_any(mes);
		}

		if (mes[0] == null) {
			plugin = new GridGrouperPlugin();
			mes[0] = new MessageElement(new QName(
					"http://cagrid.nci.nih.gov/1/gridgrouper-plugin",
					"GridGrouperPlugin"), plugin);
		} else {
			plugin = (GridGrouperPlugin) mes[0].getObjectValue();
		}

		plugin.setServiceMembershipExpression(exp);
		
		//TODO: Clean up old expressions
	}

}
