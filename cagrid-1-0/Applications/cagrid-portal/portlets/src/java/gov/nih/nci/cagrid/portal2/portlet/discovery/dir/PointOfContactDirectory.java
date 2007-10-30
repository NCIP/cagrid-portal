/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.dir;

import gov.nih.nci.cagrid.portal2.dao.PointOfContactDao;

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

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.portlet.discovery.dir.DiscoveryDirectory#getObjects()
	 */
	@Override
	public List getObjects() {
		return getPointOfContactDao().getAll();
	}

	public PointOfContactDao getPointOfContactDao() {
		return pointOfContactDao;
	}

	public void setPointOfContactDao(PointOfContactDao pointOfContactDao) {
		this.pointOfContactDao = pointOfContactDao;
	}

}
