package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.DBTestBase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class QueryInstanceDaoTestBase<T extends AbstractDao> extends DBTestBase<T> {
 

    @Override
    protected String getDataSet() throws Exception {
        return "test/data/QueryInstanceDaoTest.xml";
    }


}
