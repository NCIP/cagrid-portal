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
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class Table {
	
	private List<String> headers = new ArrayList<String>();
	private List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();

	public Table(){
		
	}
	public Table(List<String> headers, List<Map<String,Object>> rows) {
		this.headers = headers;
		this.rows = rows;
	}
	public List<String> getHeaders() {
		return headers;
	}
	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}
	public List<Map<String,Object>> getRows() {
		return rows;
	}
	public void setRows(List<Map<String,Object>> rows) {
		this.rows = rows;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("HEADERS:\n");
		for(String header : getHeaders()){
			sb.append(header).append("\t");
		}
		sb.append("\n");
		sb.append("DATA:\n");
		for(Map<String,Object> row : getRows()){
			for(String header : getHeaders()){
				Object obj = row.get(header);
				String value = obj == null ? "" : obj.toString().trim();
				sb.append(header).append(":").append("'").append(value).append("'\t");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
