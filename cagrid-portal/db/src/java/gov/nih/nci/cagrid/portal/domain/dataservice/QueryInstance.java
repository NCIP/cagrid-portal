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
package gov.nih.nci.cagrid.portal.domain.dataservice;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.ForceDiscriminator;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "query_instances")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_query_inst") })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "query_inst_type", discriminatorType = DiscriminatorType.STRING)
@ForceDiscriminator
public abstract class QueryInstance extends AbstractDomainObject {

	private PortalUser portalUser;
	private Query query;
	private QueryInstanceState state = QueryInstanceState.UNSCHEDULED;
	private String result;
	private String error;
	private Date createTime = new Date();
	private Date startTime;
	private Date finishTime;
	private QueryResultTable queryResultTable;
	
	/**
	 * 
	 */
	public QueryInstance() {
	}

	@ManyToOne
	@JoinColumn(name = "query_id")
	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	@Enumerated(EnumType.STRING)
	public QueryInstanceState getState() {
		return state;
	}

	public void setState(QueryInstanceState state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

    @Column(length = 4000)
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@Transient
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	

	@ManyToOne
	@JoinColumn(name = "portal_user_id")
	public PortalUser getPortalUser() {
		return portalUser;
	}

	public void setPortalUser(PortalUser portalUser) {
		this.portalUser = portalUser;
	}

	
	@Transient
	public abstract String getType();
	public abstract void setType(String type);

	@OneToOne(mappedBy="queryInstance")
	public QueryResultTable getQueryResultTable() {
		return queryResultTable;
	}

	public void setQueryResultTable(QueryResultTable queryResultTable) {
		this.queryResultTable = queryResultTable;
	}

}
