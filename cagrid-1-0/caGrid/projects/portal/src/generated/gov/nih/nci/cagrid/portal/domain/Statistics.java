package gov.nih.nci.cagrid.portal.domain;

/**
 * @version 1.0
 * @created 19-Jun-2006 3:48:26 PM
 */
public class Statistics {

	private boolean active;
	private double responseTime;
	private double timestamp;

	public Statistics(){

	}

	public void finalize() throws Throwable {

	}

	public double getResponseTime(){
		return responseTime;
	}

	public double getTimestamp(){
		return timestamp;
	}

	public boolean isActive(){
		return active;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setActive(boolean newVal){
		active = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setResponseTime(double newVal){
		responseTime = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTimestamp(double newVal){
		timestamp = newVal;
	}

}