package org.cagrid.gaards.core;

import gov.nih.nci.cagrid.common.Utils;

public class BasicAuthenticationCredential {

	private String userId;
	private String password;

	public BasicAuthenticationCredential() {

	}

	public BasicAuthenticationCredential(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (!(obj instanceof BasicAuthenticationCredential)) {
			return false;
		} else {
			BasicAuthenticationCredential cred = (BasicAuthenticationCredential) obj;
			if (Utils.equals(getUserId(), cred.getUserId())
					&& (Utils.equals(getPassword(), cred.getPassword()))) {
				return true;
			} else {
				return false;
			}
		}
	}

}
