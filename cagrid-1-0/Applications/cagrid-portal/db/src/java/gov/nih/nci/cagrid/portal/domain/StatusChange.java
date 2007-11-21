/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

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

}
