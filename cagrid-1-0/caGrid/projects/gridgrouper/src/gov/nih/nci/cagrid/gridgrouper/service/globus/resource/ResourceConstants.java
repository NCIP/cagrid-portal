package gov.nih.nci.cagrid.gridgrouper.service.globus.resource;

import javax.xml.namespace.QName;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A href="mailto:ervin@bmi.osu.edu">David Ervin </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public interface ResourceConstants {
	public static final String SERVICE_NS = "http://cagrid.nci.nih.gov/GridGrouper";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "GridGrouperKey");
	public static final QName RESOURCE_PROPERY_SET = new QName(SERVICE_NS, "GridGrouperResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
