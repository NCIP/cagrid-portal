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
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQuery;

import javax.persistence.NonUniqueResultException;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DCQLQueryDao extends AbstractDao<DCQLQuery> {

    /**
     *
     */
    public DCQLQueryDao() {

    }

    /* (non-Javadoc)
      * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
      */
    @Override
    public Class domainClass() {
        return DCQLQuery.class;
    }

    public DCQLQuery getByHash(String hash) {
        DCQLQuery query = null;

        List queries = getHibernateTemplate().find(
                "from DCQLQuery where hash = '" + hash + "'");

        if (queries.size() > 1) {
            throw new NonUniqueResultException("Found " + queries.size()
                    + " DCQLQuery objects for hash '" + hash + "'");
        }
        if (queries.size() == 1) {
            query = (DCQLQuery) queries.get(0);
        }
        return query;
    }

}
