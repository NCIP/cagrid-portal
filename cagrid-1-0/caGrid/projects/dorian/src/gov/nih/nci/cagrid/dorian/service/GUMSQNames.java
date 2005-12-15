package gov.nih.nci.cagrid.gums.service;

import javax.xml.namespace.QName;
/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: GUMSQNames.java,v 1.1 2005-12-15 15:06:46 langella Exp $
 */
public interface GUMSQNames {
	public static final String NS = "http://cagrid.nci.nih.gov/gums";

	public static final QName RP_DESCRIPTION = new QName(NS, "description");


	public static final QName RESOURCE_PROPERTIES = new QName(NS,
			"GUMSResourceProperties");
}
