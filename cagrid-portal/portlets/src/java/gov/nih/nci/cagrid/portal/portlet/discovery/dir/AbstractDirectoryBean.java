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

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractDirectoryBean {
	
	private String selectedDirectory;
	
	private String selectedResults;

	public String getSelectedDirectory() {
		return selectedDirectory;
	}

	public void setSelectedDirectory(String selectedDirectory) {
		this.selectedDirectory = selectedDirectory;
	}

	public String getSelectedResults() {
		return selectedResults;
	}

	public void setSelectedResults(String selectedResults) {
		this.selectedResults = selectedResults;
	}	

}
