
package gov.nih.nci.cagrid.gums.portal;

import org.projectmobius.portal.GridPortalComponent;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: AttributeViewer.java,v 1.1 2005-09-27 20:09:50 langella Exp $
 */
public interface AttributeViewer {
	
	public GridPortalComponent getGumsPortalInternalFrame();
	public String toXML() throws Exception;
	public void fromXML(String xml) throws Exception;
	public String getXMLNamespace();
	public String getXMLName();
}
