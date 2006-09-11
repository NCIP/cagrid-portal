package gov.nih.nci.cagrid.idp.plugin.domain;

public class Credential {
	
	private String userId;
	private String password;
	
	public Credential(String userId, String password){
		this.userId = userId;
		this.password=password;
	}
    
	public String getUserId(){
		return userId;
	}
	public String getPassword(){
		return password;
	}
}
