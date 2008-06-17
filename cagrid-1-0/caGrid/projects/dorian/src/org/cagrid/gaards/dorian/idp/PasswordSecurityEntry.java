package org.cagrid.gaards.dorian.idp;

public class PasswordSecurityEntry {
	private String uid;
	private long consecutiveInvalidLogins;
	private long lockOutExpiration;
	private long invalidLoginCount;


	public PasswordSecurityEntry() {

	}


	public String getUid() {
		return uid;
	}


	public long getConsecutiveInvalidLogins() {
		return consecutiveInvalidLogins;
	}


	public void setConsecutiveInvalidLogins(long consecutiveInvalidLogins) {
		this.consecutiveInvalidLogins = consecutiveInvalidLogins;
	}


	public long getTotalInvalidLogins() {
		return invalidLoginCount;
	}


	public void setTotalInvalidLogins(long invalidLoginCount) {
		this.invalidLoginCount = invalidLoginCount;
	}


	public long getLockOutExpiration() {
		return lockOutExpiration;
	}


	public void setLockOutExpiration(long lockOutExpiration) {
		this.lockOutExpiration = lockOutExpiration;
	}


	public void setUid(String uid) {
		this.uid = uid;
	}

}
