/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.portal2.domain.GridService;

import java.util.List;

import org.dbunit.operation.DatabaseOperation;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class GridServiceDaoTest extends AbstractDaoTest {

	/**
	 * 
	 */
	public GridServiceDaoTest() {

	}

	/**
	 * @param arg0
	 */
	public GridServiceDaoTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.dao.AbstractDaoTest#getDataSetFileName()
	 */
	@Override
	protected String getDataSetFileName() {
		return "db/test/data/GridServiceDaoTest.xml";
	}
	
	public void testGetGridServicesForIndexService(){
		GridServiceDao dao = (GridServiceDao)getApplicationContext().getBean("gridServiceDao");
		List<GridService> svcsFor1 = dao.getByIndexServiceUrl("http://index1"); 
		assertTrue(svcsFor1.size() == 3);
	}

	
	protected DatabaseOperation getTearDownOperation() throws Exception {
		return DatabaseOperation.DELETE_ALL;
	}
}
