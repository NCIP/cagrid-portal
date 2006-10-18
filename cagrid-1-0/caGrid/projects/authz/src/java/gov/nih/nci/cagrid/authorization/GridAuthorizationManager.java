package gov.nih.nci.cagrid.authorization;

public interface GridAuthorizationManager {
	
	public boolean isAuthorized(String identity, String objectId, String privilege);

}
