/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.map;

import gov.nih.nci.cagrid.portal2.domain.metadata.common.PointOfContact;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class PointOfContactMapNode extends MapNode {

	private List<PointOfContact> pointOfContacts = new ArrayList<PointOfContact>();
	
	/**
	 * 
	 */
	public PointOfContactMapNode() {

	}

	public List<PointOfContact> getPointOfContacts() {
		return pointOfContacts;
	}

	public void setPointOfContacts(List<PointOfContact> pointOfContacts) {
		this.pointOfContacts = pointOfContacts;
	}

}
