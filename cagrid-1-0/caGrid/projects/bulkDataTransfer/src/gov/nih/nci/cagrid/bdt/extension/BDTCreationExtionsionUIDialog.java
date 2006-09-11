package gov.nih.nci.cagrid.bdt.extension;

import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.CreationExtensionUIDialog;

import java.awt.Frame;


/**
 * BDTCreationExtionsionUIDialog TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * 
 * @created Aug 29, 2006
 */
public class BDTCreationExtionsionUIDialog extends CreationExtensionUIDialog {

	public BDTCreationExtionsionUIDialog(Frame f, ServiceExtensionDescriptionType arg0, ServiceInformation arg1) {
		super(f, arg0, arg1);
	}

}
