package gov.nih.nci.cagrid.gridgrouper.service.globus.resource;

import javax.xml.namespace.QName;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public interface ResourceConstants {
	public static final String SERVICE_NS = "http://cagrid.nci.nih.gov/GridGrouper";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "GridGrouperKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "GridGrouperResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
