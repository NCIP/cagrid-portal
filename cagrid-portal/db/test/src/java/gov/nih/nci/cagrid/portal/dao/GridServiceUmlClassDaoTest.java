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
package gov.nih.nci.cagrid.portal.dao;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.cagrid.portal.DBTestBase;
import gov.nih.nci.cagrid.portal.domain.GridServiceUmlClass;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class GridServiceUmlClassDaoTest extends DBTestBase<GridServiceUmlClassDao> {
	
	@Test
	public void testGetAll() {
		List<GridServiceUmlClass> l = this.getDao().getAll();
		assertEquals(l.size(),2);
	}
	
	@Test
	public void testGetByGridServiceAndUmlClass() {
		GridServiceUmlClass obj = this.getDao().getByGridServiceAndUmlClass(-1, -1);
		assertEquals(obj.getObjectCount(),6);
		
		obj = this.getDao().getByGridServiceAndUmlClass(-2, -1);
		assertEquals(obj.getObjectCount(),5);
	}
	
	@Test
	public void testGetUniqueCaptions() {
		List list = this.getDao().getUniqueCaptions();
		assertEquals(list.size(),2);
	}
	
	@Test
	public void testGetAggregatedClassCountByCaption() {
		Map map = this.getDao().getAggregatedClassCountByCaption("caArray");

	}
	


}
