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

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "shared_cql")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_shared_cql") })
public class SharedCQLQuery extends AbstractDomainObject {

	private CQLQuery cqlQuery;
	private PortalUser owner;
	private GridDataService targetService;
	private UMLClass targetClass;
	private Date shareDate = new Date();
	private String description;
	private String name;
	
	/**
	 * 
	 */
	public SharedCQLQuery() {

	}

	@ManyToOne
	@JoinColumn(name = "query_id")
	public CQLQuery getCqlQuery() {
		return cqlQuery;
	}

	public void setCqlQuery(CQLQuery cqlQuery) {
		this.cqlQuery = cqlQuery;
	}

	@ManyToOne
	@JoinColumn(name = "owner_id")
	public PortalUser getOwner() {
		return owner;
	}

	public void setOwner(PortalUser owner) {
		this.owner = owner;
	}

	@ManyToOne
	@JoinColumn(name = "service_id")
	public GridDataService getTargetService() {
		return targetService;
	}

	
	public void setTargetService(GridDataService targetService) {
		this.targetService = targetService;
	}

	@ManyToOne
	@JoinColumn(name = "class_id")
	public UMLClass getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(UMLClass targetClass) {
		this.targetClass = targetClass;
	}

	public Date getShareDate() {
		return shareDate;
	}

	public void setShareDate(Date shareDate) {
		this.shareDate = shareDate;
	}

	@Lob
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
