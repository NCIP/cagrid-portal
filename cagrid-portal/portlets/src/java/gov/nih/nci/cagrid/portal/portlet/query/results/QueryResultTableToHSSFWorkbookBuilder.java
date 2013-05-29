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
package gov.nih.nci.cagrid.portal.portlet.query.results;

import java.util.List;

import gov.nih.nci.cagrid.portal.dao.QueryResultTableDao;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultCell;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultColumn;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultRow;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class QueryResultTableToHSSFWorkbookBuilder {
	
	private QueryResultTableDao queryResultTableDao;
	
	public HSSFWorkbook build(Integer tableId){
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("query_results");
		
		short rowIdx = 0;
		short colIdx = 0;
		
		QueryResultTable table = getQueryResultTableDao().getById(tableId);
		List<QueryResultColumn> cols = table.getColumns();
		List<QueryResultRow> rows = table.getRows();
		
		HSSFRow headerRow = sheet.createRow(rowIdx);
		for(QueryResultColumn col : cols){
			HSSFCell cell = headerRow.createCell(colIdx);
			cell.setCellValue(col.getName());
			colIdx++;
		}
		HSSFCell serviceUrlCol = headerRow.createCell(colIdx);
		serviceUrlCol.setCellValue(ResultConstants.DATA_SERVICE_URL_COL_NAME);
		
		rowIdx++;
		for(QueryResultRow row : rows){
			HSSFRow r = sheet.createRow(rowIdx);
			colIdx = 0;
			for(QueryResultColumn col : cols){
				String value = "";
				for(QueryResultCell cell : row.getCells()){
					if(cell.getColumn().getId().equals(col.getId())){
						value = cell.getValue();
						break;
					}
				}
				HSSFCell c = r.createCell(colIdx);
				c.setCellValue(value);
				colIdx++;
			}
			HSSFCell serviceUrlCell = r.createCell(colIdx);
			serviceUrlCell.setCellValue(row.getServiceUrl());
			
			rowIdx++;
		}
		
		return workbook;
	}

	public QueryResultTableDao getQueryResultTableDao() {
		return queryResultTableDao;
	}

	public void setQueryResultTableDao(QueryResultTableDao queryResultTableDao) {
		this.queryResultTableDao = queryResultTableDao;
	}

}
