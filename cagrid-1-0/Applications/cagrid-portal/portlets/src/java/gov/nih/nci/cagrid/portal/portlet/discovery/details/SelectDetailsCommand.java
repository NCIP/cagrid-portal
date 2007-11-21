/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.details;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectDetailsCommand {

	private Integer selectedId;
	
	/**
	 * 
	 */
	public SelectDetailsCommand() {

	}

	public Integer getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(Integer serviceId) {
		this.selectedId = serviceId;
	}

}
