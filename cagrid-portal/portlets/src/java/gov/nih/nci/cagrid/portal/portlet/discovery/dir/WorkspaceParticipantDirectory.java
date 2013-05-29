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
