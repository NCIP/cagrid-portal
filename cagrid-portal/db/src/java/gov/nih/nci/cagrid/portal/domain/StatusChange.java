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
package gov.nih.nci.cagrid.portal.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "status_change")
@GenericGenerator(name="id-generator", strategy = "native",
    parameters = {
        @Parameter(name="sequence", value="seq_status_change")
    }
)
  
public class StatusChange extends AbstractDomainObject {

	private ServiceStatus status;
	private Date time;
	private GridService service;
    private boolean archived = false;

	/**
	 * 
	 */
	public StatusChange() {
	}

	@ManyToOne
	@JoinColumn(name = "service_id")
	public GridService getService() {
		return service;
	}


	public void setService(GridService service) {
		this.service = service;
	}


	@Enumerated(EnumType.STRING)
	public ServiceStatus getStatus() {
		return status;
	}


	public void setStatus(ServiceStatus status) {
		this.status = status;
	}


	public Date getTime() {
		return time;
	}


	public void setTime(Date time) {
		this.time = time;
	}

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
