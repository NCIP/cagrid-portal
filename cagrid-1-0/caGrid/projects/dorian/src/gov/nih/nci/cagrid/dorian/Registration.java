package gov.nih.nci.cagrid.gums;

import gov.nih.nci.cagrid.gums.bean.AttributeDescriptor;
import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.GUMSException;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: Registration.java,v 1.2 2005-09-28 20:19:09 langella Exp $
 */
public interface Registration {
	public AttributeDescriptor[] getRequiredUserAttributes() throws GUMSInternalFault, GUMSException;

}
