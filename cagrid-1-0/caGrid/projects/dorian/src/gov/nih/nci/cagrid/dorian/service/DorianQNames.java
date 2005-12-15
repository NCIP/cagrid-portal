package gov.nih.nci.cagrid.dorian.service;

import javax.xml.namespace.QName;
/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: DorianQNames.java,v 1.1 2005-12-15 19:29:33 langella Exp $
 */
public interface DorianQNames {
	public static final String NS = "http://cagrid.nci.nih.gov/dorian";

	public static final QName RP_DESCRIPTION = new QName(NS, "description");


	public static final QName RESOURCE_PROPERTIES = new QName(NS,
			"DorianResourceProperties");
}
