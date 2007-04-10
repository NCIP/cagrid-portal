/**
 * 
 */
package org.cagrid.rav;

import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.ServiceModificationUIPanel;

/**
 * @author madduri
 *
 */
public class ApplicationServiceModificationPanel extends
		ServiceModificationUIPanel {

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ApplicationServiceModificationPanel(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		super(desc, info);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.introduce.portal.extension.ServiceModificationUIPanel#resetGUI()
	 */
	protected void resetGUI() {
		// TODO Auto-generated method stub
		
	}

}
