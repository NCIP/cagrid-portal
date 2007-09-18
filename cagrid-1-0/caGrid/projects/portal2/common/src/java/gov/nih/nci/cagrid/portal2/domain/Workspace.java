/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "workspaces")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_workspaces") })
public class Workspace extends AbstractDomainObject {

	private String name;
	private String abbreviation;
	private String description;
	private String homepageUrl;
	private String logoUrl;
	private List<Participant> participants = new ArrayList<Participant>();
	/**
	 * 
	 */
	public Workspace() {

	}
	

	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

	public String getHomepageUrl() {
		return homepageUrl;
	}
	public void setHomepageUrl(String homepageUrl) {
		this.homepageUrl = homepageUrl;
	}
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToMany
	@JoinTable(
			name = "wrkspc_partic", 
			joinColumns = 
				@JoinColumn(name = "workspace_id"), 
			inverseJoinColumns = 
				@JoinColumn(name = "participant_id"), 
			uniqueConstraints = 
				@UniqueConstraint(columnNames = 
					{"workspace_id", "participant_id" }))
	public List<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}


}
