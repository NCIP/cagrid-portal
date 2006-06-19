package gov.nih.nci.cagrid.portal.domain;

/**
 * Represents a Index Service Instance
 * @version 1.0
 * @created 19-Jun-2006 3:22:45 PM
 */
public class IndexService implements GridService {

	private boolean active;
	private java.util.Collection registeredServicesCollection;

	public IndexService(){

	}

	public void finalize() throws Throwable {

	}

	public java.lang.String getDescription(){
		return description;
	}

	public EPR getHandle(){
		return handle;
	}

	public java.lang.String getName(){
		return name;
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
	public void setDescription(java.lang.String newVal){
		description = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setHandle(EPR newVal){
		handle = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setName(java.lang.String newVal){
		name = newVal;
	}

}