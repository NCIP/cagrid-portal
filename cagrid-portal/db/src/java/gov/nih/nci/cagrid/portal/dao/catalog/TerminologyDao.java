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
package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.Terminology;

import java.util.List;

import javax.persistence.NonUniqueResultException;

/**
 * User: kherm
 * 
 * @author kherm manav.kher@semanticbits.com
 */
public class TerminologyDao extends AbstractDao<Terminology> {

	public TerminologyDao() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return Terminology.class;
	}

	public Terminology getByIdentifier(String identifier) {
		Terminology terminology = null;
		List l = getHibernateTemplate().find(
				"from Terminology where identifier = ?", identifier);
		if (l.size() > 1) {
			throw new NonUniqueResultException(
					"More than one Terminology found for identifier= "
							+ identifier);
		}
		if (l.size() == 1) {
			terminology = (Terminology) l.iterator().next();
		}
		return terminology;
	}
}