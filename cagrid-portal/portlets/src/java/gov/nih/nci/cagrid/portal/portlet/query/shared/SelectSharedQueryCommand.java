/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.shared;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectSharedQueryCommand {
	
	private Integer queryId;

	/**
	 * 
	 */
	public SelectSharedQueryCommand() {

	}

	public Integer getQueryId() {
		return queryId;
	}

	public void setQueryId(Integer queryId) {
		this.queryId = queryId;
	}

}
