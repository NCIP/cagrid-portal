package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DCQLQueryInstanceDao extends AbstractDao<DCQLQueryInstance> {

    /**
     *
     */
    public DCQLQueryInstanceDao() {

    }

    /* (non-Javadoc)
      * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
      */
    @Override
    public Class domainClass() {
        return DCQLQueryInstance.class;
    }


}
