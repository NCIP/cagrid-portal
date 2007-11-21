/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.dir;

import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class DiscoveryDirectory {
	
	private String id;
	private DiscoveryType type;

	/**
	 * 
	 */
	public DiscoveryDirectory() {

	}
	
	public abstract List getObjects();

	@Required
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Required
	public DiscoveryType getType() {
		return type;
	}

	public void setType(DiscoveryType type) {
		this.type = type;
	}

}
