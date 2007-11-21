/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery;

import gov.nih.nci.cagrid.portal.domain.DomainObject;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DiscoveryResults {
	
	private DiscoveryType type;
	private Date time;
	private List<DomainObject> objects;
	private String id;

	/**
	 * 
	 */
	public DiscoveryResults() {
		setTime(new Date());
		setId(String.valueOf(getTime().getTime()));
	}

	
	public DiscoveryType getType() {
		return type;
	}

	public void setType(DiscoveryType type) {
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public List<DomainObject> getObjects() {
		return objects;
	}

	public void setObjects(List<DomainObject> objects) {
		this.objects = objects;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

}
