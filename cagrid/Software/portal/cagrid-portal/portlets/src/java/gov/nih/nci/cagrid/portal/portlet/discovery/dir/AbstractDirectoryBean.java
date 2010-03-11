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
