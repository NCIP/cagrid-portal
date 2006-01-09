package gov.nih.nci.cagrid.dorian.service;

import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceProperties;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;
import org.globus.wsrf.impl.ReflectionResourceProperty;
import org.globus.wsrf.impl.SimpleResourcePropertySet;
/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: DorianResource.java,v 1.2 2006-01-09 20:21:42 langella Exp $
 */
public class DorianResource implements Resource, ResourceProperties {

	/* Resource Property set */
	private ResourcePropertySet propSet;

	/* Resource properties */


	private String description;

	/* Initializes RPs */
	public void initialize() throws Exception {
		this.propSet = new SimpleResourcePropertySet(
				DorianQNames.RESOURCE_PROPERTIES);

		try {
			ResourceProperty valueRP = new ReflectionResourceProperty(
					DorianQNames.RP_DESCRIPTION, "Description", this);
			this.propSet.add(valueRP);
			setDescription("Start");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/* Get/Setters for the RPs */
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description ;
	}

	/* Required by interface ResourceProperties */
	public ResourcePropertySet getResourcePropertySet() {
		return this.propSet;
	}
	
	
}