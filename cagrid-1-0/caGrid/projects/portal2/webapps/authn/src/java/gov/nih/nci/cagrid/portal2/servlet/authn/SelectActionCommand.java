/**
 * 
 */
package gov.nih.nci.cagrid.portal2.servlet.authn;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectActionCommand {
	
	
	public static String GRID_LOGIN_ACTION = "GRID_LOGIN";
	public static String LOCAL_LOGIN_ACTION = "LOCAL_LOGIN";
	public static String REGISTER_ACTION = "REGISTER";
	
	private String selectedAction = GRID_LOGIN_ACTION;
	private String targetUrl;
	public String getSelectedAction() {
		return selectedAction;
	}
	public void setSelectedAction(String selectedAction) {
		this.selectedAction = selectedAction;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

}
