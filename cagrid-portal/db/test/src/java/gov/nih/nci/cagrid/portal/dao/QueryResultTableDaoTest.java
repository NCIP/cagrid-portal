/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao;

import static org.junit.Assert.*;
import gov.nih.nci.cagrid.portal.AbstractDBTestBase;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultCell;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultColumn;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultData;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultRow;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class QueryResultTableDaoTest extends AbstractDBTestBase {
	
	@Test
	public void testCreate(){
		HibernateTemplate templ = (HibernateTemplate) getApplicationContext().getBean("hibernateTemplate");
		
		String serviceUrl = "http://some.service";
		
		QueryResultData data = new QueryResultData();
		data.setData("lots and lots and lots and lots of data".getBytes());
		templ.save(data);
		
		QueryResultTable table = new QueryResultTable();
		table.setData(data);
		
		int numRows = 10;
		int numCols = 10;
		
		List<QueryResultColumn> cols = new ArrayList<QueryResultColumn>();
		for(int i = 0; i < numCols; i++){
			QueryResultColumn col = new QueryResultColumn();
			col.setName("col" + i);
			col.setTable(table);
			templ.save(col);
			cols.add(col);
		}
		table.setColumns(cols);
		
		List<QueryResultRow> rows = new ArrayList<QueryResultRow>();
		for(int i = 0; i < numRows; i++){
			QueryResultRow row = new QueryResultRow();
			row.setTable(table);
			row.setServiceUrl(serviceUrl + "/" + i);
			for(int j = 0; j < numCols; j++){
				QueryResultCell cell = new QueryResultCell();
				cell.setColumn(cols.get(j));
				cell.setRow(row);
				cell.setValue("[" + i + "][" + j + "]");
				templ.save(cell);
				row.getCells().add(cell);
			}
			templ.save(row);
			rows.add(row);
		}
		table.setRows(rows);
		
		templ.save(table);
		templ.flush();
		
		table = (QueryResultTable) templ.get(QueryResultTable.class, table.getId());

		System.out.println(table);
		assertEquals(10, table.getRows().size());
		
		QueryResultTableDao dao = (QueryResultTableDao)getApplicationContext().getBean("queryResultTableDao");
		
		List<QueryResultRow> l = dao.getRows(table.getId(), 3, 4);
		
		int idx = 0;
		for(QueryResultRow r : l){
			if(idx == 0){
				System.out.print("\t");
				for(QueryResultColumn col : r.getTable().getColumns()){
					System.out.print(col.getName() + "\t");
				}
				System.out.println();
			}
			System.out.print(idx + ": \t");
			for(QueryResultCell cell : r.getCells()){
				System.out.print(cell.getValue() + "\t");
			}
			System.out.println();
			idx++;
		}
		assertEquals(4, l.size());
		
		dao.delete(table);
	}

}
