/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.table;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "query_result_table")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_qr_table") })
public class QueryResultTable extends AbstractDomainObject {
	
	private QueryInstance queryInstance;
	private QueryResultData data;
	private List<QueryResultRow> rows = new ArrayList<QueryResultRow>();
	private List<QueryResultColumn> columns = new ArrayList<QueryResultColumn>();
	
	@OneToOne
	@JoinColumn(name="instance_id")
	public QueryInstance getQueryInstance() {
		return queryInstance;
	}
	public void setQueryInstance(QueryInstance queryInstance) {
		this.queryInstance = queryInstance;
	}
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="table")
	public List<QueryResultRow> getRows() {
		return rows;
	}
	public void setRows(List<QueryResultRow> rows) {
		this.rows = rows;
	}
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="table")
	public List<QueryResultColumn> getColumns() {
		return columns;
	}
	public void setColumns(List<QueryResultColumn> columns) {
		this.columns = columns;
	}
	
	@OneToOne (fetch= FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "data_id")
	public QueryResultData getData() {
		return data;
	}
	public void setData(QueryResultData data) {
		this.data = data;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("DATA:\n").append(new String(getData().getData())).append("\n");
		sb.append("COLUMNS:\n");
		for(QueryResultColumn col : getColumns()){
			sb.append(col.getName()).append("\t");
		}
		sb.append("\n");
		sb.append("ROWS:\n");
		for(QueryResultRow row : getRows()){
			for(QueryResultCell cell : row.getCells()){
				sb.append("'").append(cell.getValue()).append("'\t");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
