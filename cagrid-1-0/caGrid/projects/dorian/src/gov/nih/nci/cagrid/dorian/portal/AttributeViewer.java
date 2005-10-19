
package gov.nih.nci.cagrid.gums.portal;

import org.projectmobius.portal.GridPortalComponent;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public interface AttributeViewer {
	
	public GridPortalComponent getGumsPortalInternalFrame();
	public String toXML() throws Exception;
	public void fromXML(String xml) throws Exception;
	public String getXMLNamespace();
	public String getXMLName();
}
