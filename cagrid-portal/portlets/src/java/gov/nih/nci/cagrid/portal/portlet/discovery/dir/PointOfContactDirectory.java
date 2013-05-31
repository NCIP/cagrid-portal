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
package gov.nih.nci.cagrid.portal.portlet.discovery.dir;

import gov.nih.nci.cagrid.portal.dao.PointOfContactDao;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;

import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class PointOfContactDirectory extends DiscoveryDirectory {
	
	private PointOfContactDao pointOfContactDao;

	/**
	 * 
	 */
	public PointOfContactDirectory() {
		setType(DiscoveryType.POC);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.discovery.dir.DiscoveryDirectory#getObjects()
	 */
	@Override
	public List getObjects() {
		return getPointOfContactDao().getAllPointOfContactPersons();
	}

	public PointOfContactDao getPointOfContactDao() {
		return pointOfContactDao;
	}

	public void setPointOfContactDao(PointOfContactDao pointOfContactDao) {
		this.pointOfContactDao = pointOfContactDao;
	}

}
