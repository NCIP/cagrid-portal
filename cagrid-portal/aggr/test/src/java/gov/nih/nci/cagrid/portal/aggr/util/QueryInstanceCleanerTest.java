package gov.nih.nci.cagrid.portal.aggr.util;

import gov.nih.nci.cagrid.portal.DBIntegrationTestBase;
import gov.nih.nci.cagrid.portal.dao.QueryInstanceDao;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class QueryInstanceCleanerTest extends DBIntegrationTestBase {


    private QueryInstanceDao queryInstanceDao;
    private QueryInstanceCleaner queryInstanceCleaner;

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath:applicationContext-cleanup-utils.xml",
                "classpath:applicationContext-service.xml",
                "classpath:applicationContext-db.xml"


        };
    }

    public void testClean() {
        QueryInstance inst = new CQLQueryInstance();
        getQueryInstanceDao().save(inst);
        QueryInstance dcqlInst = new DCQLQueryInstance();
        getQueryInstanceDao().save(dcqlInst);

        assertEquals(2, getQueryInstanceDao().getAll().size());
        queryInstanceCleaner.clean();
        assertEquals(2, getQueryInstanceDao().getAll().size());

        Calendar cal = new GregorianCalendar();
        cal.set(1900, 1, 12);
        inst.setCreateTime(new Date(1, 1, 1));
        getQueryInstanceDao().save(inst);

        queryInstanceCleaner.clean();
        assertEquals(1, getQueryInstanceDao().getAll().size());
    }


    public QueryInstanceDao getQueryInstanceDao() {
        return queryInstanceDao;
    }

    public void setQueryInstanceDao(QueryInstanceDao queryInstanceDao) {
        this.queryInstanceDao = queryInstanceDao;
    }

    public QueryInstanceCleaner getQueryInstanceCleaner() {
        return queryInstanceCleaner;
    }

    public void setQueryInstanceCleaner(QueryInstanceCleaner queryInstanceCleaner) {
        this.queryInstanceCleaner = queryInstanceCleaner;
    }
}
