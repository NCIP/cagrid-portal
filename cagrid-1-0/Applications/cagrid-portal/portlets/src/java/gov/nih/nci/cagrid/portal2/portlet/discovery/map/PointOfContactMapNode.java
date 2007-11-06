/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.map;

import gov.nih.nci.cagrid.portal2.domain.Person;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.PointOfContact;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class PointOfContactMapNode extends MapNode {

	private List<Person> pointOfContacts = new ArrayList<Person>();
	
	/**
	 * 
	 */
	public PointOfContactMapNode() {

	}

	public List<Person> getPointOfContacts() {
		return pointOfContacts;
	}

	public void setPointOfContacts(List<Person> pointOfContacts) {
		this.pointOfContacts = pointOfContacts;
	}

}
