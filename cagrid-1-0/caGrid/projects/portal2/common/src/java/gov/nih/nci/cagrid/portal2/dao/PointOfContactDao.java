/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.portal2.domain.metadata.common.PointOfContact;





/**
 * @author <a href="mailto:parmarv@mail.nih.gov">Vijay Parmar</a>
 *
 */
public class PointOfContactDao extends AbstractDao<PointOfContact> {
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return PointOfContact.class;
	}

}
