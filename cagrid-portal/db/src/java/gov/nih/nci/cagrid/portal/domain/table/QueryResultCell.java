/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.table;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Entity
@Table(name = "query_result_cell")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_qr_cell") })
public class QueryResultCell extends AbstractDomainObject {
	
	private String value;
	private QueryResultColumn column;
	private QueryResultRow row;

	@Lob
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@ManyToOne
	@JoinColumn(name = "row_id")
	public QueryResultRow getRow() {
		return row;
	}

	public void setRow(QueryResultRow row) {
		this.row = row;
	}

	@ManyToOne
	@JoinColumn(name="column_id")
	public QueryResultColumn getColumn() {
		return column;
	}

	public void setColumn(QueryResultColumn column) {
		this.column = column;
	}

	

}
