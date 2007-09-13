package org.cagrid.tools.events;

import java.util.Date;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A href="mailto:ervin@bmi.osu.edu">David Ervin</A>
 */
public class Event {
	private long eventId;
	private String targetId;
	private String reportingPartyId;
	private String eventType;
	private String message;
	private Date occurredAt;


	public long getEventId() {
		return eventId;
	}


	public void setEventId(long eventId) {
		this.eventId = eventId;
	}


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
