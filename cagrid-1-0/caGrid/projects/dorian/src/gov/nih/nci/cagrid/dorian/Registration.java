package gov.nih.nci.cagrid.gums;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.GUMSException;
import gov.nih.nci.cagrid.gums.ifs.bean.AttributeDescriptor;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidApplicationFault;
import gov.nih.nci.cagrid.gums.ifs.bean.UserApplication;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public interface Registration {
	public AttributeDescriptor[] getRequiredUserAttributes() throws GUMSInternalFault,GUMSException;
	public String registerUser(UserApplication application) throws InvalidApplicationFault, GUMSInternalFault,GUMSException;
}
