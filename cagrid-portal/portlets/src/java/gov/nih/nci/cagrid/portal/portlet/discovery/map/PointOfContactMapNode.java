/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.map;

import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact;

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
