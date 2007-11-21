/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain;

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
@Table(name = "participation")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_participation") })
public class Participation extends AbstractDomainObject {

	private Workspace workspace;
	private ParticipantStatus status;
	private Participant participant;
	
	/**
	 * 
	 */
	public Participation() {
	}

	@ManyToOne
	@JoinColumn(name = "participant_id")
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	@Enumerated(EnumType.STRING)
	public ParticipantStatus getStatus() {
		return status;
	}

	public void setStatus(ParticipantStatus status) {
		this.status = status;
	}

	@ManyToOne
	@JoinColumn(name = "workspace_id")
	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

}
