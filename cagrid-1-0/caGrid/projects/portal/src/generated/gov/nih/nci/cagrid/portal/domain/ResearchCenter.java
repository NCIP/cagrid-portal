package gov.nih.nci.cagrid.portal.domain;

/**
 * @version 1.0
 * @created 19-Jun-2006 3:48:26 PM
 */
public class ResearchCenter {

	private java.lang.String description;
	private java.lang.String displayName;
	private java.lang.String homepageURL;
	private int id;
	private java.lang.String imageURL;
	private java.lang.String rssNewsURL;
	private java.lang.String shortName;
	private java.util.Collection registeredServicesCollection;

	public ResearchCenter(){

	}

	public void finalize() throws Throwable {

	}

	public java.lang.String getDescription(){
		return description;
	}

	public java.lang.String getDisplayName(){
		return displayName;
	}

	public java.lang.String getHomepageURL(){
		return homepageURL;
	}

	public int getId(){
		return id;
	}

	public java.lang.String getImageURL(){
		return imageURL;
	}

	public java.lang.String getrssNewsURL(){
		return rssNewsURL;
	}

	public java.lang.String getShortName(){
		return shortName;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDescription(java.lang.String newVal){
		description = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDisplayName(java.lang.String newVal){
		displayName = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setHomepageURL(java.lang.String newVal){
		homepageURL = newVal;
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
	public void setImageURL(java.lang.String newVal){
		imageURL = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setrssNewsURL(java.lang.String newVal){
		rssNewsURL = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setShortName(java.lang.String newVal){
		shortName = newVal;
	}

}