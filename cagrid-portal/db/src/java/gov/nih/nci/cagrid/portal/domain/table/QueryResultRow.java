/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.table;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "query_result_row")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_qr_row") })
public class QueryResultRow extends AbstractDomainObject {
	
	private String serviceUrl;
	private QueryResultTable table;
	private List<QueryResultCell> cells = new ArrayList<QueryResultCell>();

	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="row")
	public List<QueryResultCell> getCells() {
		return cells;
	}
	public void setCells(List<QueryResultCell> cells) {
		this.cells = cells;
	}
	public String getServiceUrl() {
		return serviceUrl;
	}
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	
	@ManyToOne
	@JoinColumn(name = "table_id")
	public QueryResultTable getTable() {
		return table;
	}
	public void setTable(QueryResultTable table) {
		this.table = table;
	}
	

	

}
