package gov.nih.nci.cagrid.portal.domain;

/**
 * @version 1.0
 * @created 19-Jun-2006 3:22:44 PM
 */
public class DomainModel {

	private java.lang.String longName;
	private java.lang.String projectDescription;
	private java.lang.String projectVersion;
	private java.lang.String serviceEPR;
	private java.lang.String shortName;
	private RegisteredService registeredService;
	private java.util.Collection umlClassCollection;

	public DomainModel(){

	}

	public void finalize() throws Throwable {

	}

	public java.lang.String getEPR(){
		return epr;
	}

	public int getId(){
		return id;
	}

	public java.lang.String getLongName(){
		return longName;
	}

	public java.lang.String getProjectDescription(){
		return projectDescription;
	}

	public java.lang.String getProjectVersion(){
		return projectVersion;
	}

	public java.lang.String getServiceEPR(){
		return ServiceEPR;
	}

	public java.lang.String getShortName(){
		return shortName;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setEPR(java.lang.String newVal){
		epr = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setId(int newVal){
		id = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setLongName(java.lang.String newVal){
		longName = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setProjectDescription(java.lang.String newVal){
		projectDescription = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setProjectVersion(java.lang.String newVal){
		projectVersion = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setServiceEPR(java.lang.String newVal){
		ServiceEPR = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setShortName(java.lang.String newVal){
		shortName = newVal;
	}

}