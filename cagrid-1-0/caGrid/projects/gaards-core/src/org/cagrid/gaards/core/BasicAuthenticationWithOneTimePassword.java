package org.cagrid.gaards.core;


public class BasicAuthenticationWithOneTimePassword extends BasicAuthentication{

	private String oneTimePassword;


	public BasicAuthenticationWithOneTimePassword() {

	}

	public BasicAuthenticationWithOneTimePassword(String userId, String password, String oneTimePassword) {
		super(userId,password);
		this.oneTimePassword = oneTimePassword;
	}


	public boolean equals(Object obj) {
		if(super.equals(obj)){
			if(obj instanceof BasicAuthenticationWithOneTimePassword){
				BasicAuthenticationWithOneTimePassword cred = (BasicAuthenticationWithOneTimePassword)obj;
				if(getOneTimePassword().equals(cred.getOneTimePassword())){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	public String getOneTimePassword() {
		return oneTimePassword;
	}

	public void setOneTimePassword(String oneTimePassword) {
		this.oneTimePassword = oneTimePassword;
	}
	
	

}
