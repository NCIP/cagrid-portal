package gov.nih.nci.cagrid.dorian.service.ifs;

public class DisabledUser {

	private String gridIdentity;
	private long serialNumber;
	private int crlReason;


	public DisabledUser(String gridIdentity, long serialNumber, int crlReason) {
		this.gridIdentity = gridIdentity;
		this.serialNumber = serialNumber;
		this.crlReason = crlReason;
	}


	public String getGridIdentity() {
		return gridIdentity;
	}


	public long getSerialNumber() {
		return serialNumber;
	}


	public int getCRLReason() {
		return crlReason;
	}

}
