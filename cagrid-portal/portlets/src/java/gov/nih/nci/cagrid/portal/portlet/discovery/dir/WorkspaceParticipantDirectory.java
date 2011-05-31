/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.dir;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class WorkspaceParticipantDirectory extends ParticipantDirectory implements InitializingBean {

	/**
	 * 
	 */
	public WorkspaceParticipantDirectory() {

	}

	public void afterPropertiesSet() throws Exception {
		setId(getParticipantDirectoryType().toString());
	}
	
}
