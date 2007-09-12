package gov.nih.nci.cagrid.events;

import java.util.Date;


public class Event {
	private String targetId;
	private String reportingPartyId;
	private String eventType;
	private String message;
	private Date occurredAt;


	public String getTargetId() {
		return targetId;
	}


	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}


	public String getReportingPartyId() {
		return reportingPartyId;
	}


	public void setReportingPartyId(String reportingPartyId) {
		this.reportingPartyId = reportingPartyId;
	}


	public String getEventType() {
		return eventType;
	}


	public void setEventType(String eventType) {
		this.eventType = eventType;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public Date getOccurredAt() {
		return occurredAt;
	}


	public void setOccurredAt(Date occurredAt) {
		this.occurredAt = occurredAt;
	}

}
