/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.map;

import java.util.Iterator;

import gov.nih.nci.cagrid.portal2.domain.Participant;
import gov.nih.nci.cagrid.portal2.domain.Workspace;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ParticipantInfo {

	private String name;
	private String workspaces;
	private String homepageUrl;
	private String status;
	
	/**
	 * 
	 */
	public ParticipantInfo() {

	}
	
	public ParticipantInfo(Participant participant){
		setName(participant.getName());
		StringBuilder sb = new StringBuilder();
		for(Iterator i = participant.getWorkspaces().iterator(); i.hasNext();){
			Workspace workspace = (Workspace)i.next();
			sb.append(workspace.getAbbreviation());
			if(i.hasNext()){
				sb.append(",");
			}
		}
		setWorkspaces(sb.toString());
		setHomepageUrl(participant.getHomepageUrl());
		setStatus(participant.getStatus().toString());
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWorkspaces() {
		return workspaces;
	}

	public void setWorkspaces(String workspace) {
		this.workspaces = workspace;
	}

}
