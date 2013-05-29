/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.Term;

import java.util.List;

import javax.persistence.NonUniqueResultException;

/**
 * User: kherm
 * 
 * @author kherm manav.kher@semanticbits.com
 */
public class TermDao extends AbstractDao<Term> {

	public TermDao() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return Term.class;
	}

	public Term getByIdentifier(String identifier) {
		Term term = null;
		List l = getHibernateTemplate().find("from Term where identifier = ?",
				identifier);
		if (l.size() > 1) {
			throw new NonUniqueResultException(
					"More than one Term found for identifier= " + identifier);
		}
		if (l.size() == 1) {
			term = (Term) l.iterator().next();
		}
		return term;
	}
}
