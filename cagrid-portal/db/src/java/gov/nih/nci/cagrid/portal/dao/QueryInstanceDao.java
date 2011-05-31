package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class QueryInstanceDao extends AbstractDao<QueryInstance> {

    public QueryInstanceDao() {
    }

    public Class domainClass() {
        return QueryInstance.class;
    }
}
