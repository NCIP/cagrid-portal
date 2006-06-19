package gov.nih.nci.cagrid.portal.domain;

/**
 * @version 1.0
 * @created 19-Jun-2006 3:22:45 PM
 */
public class RegisteredService implements GridService {

	private java.lang.String alias;
	private java.util.Collection indexServiceCollection;
	private ResearchCenter researchCenter;
	public java.util.Collection statisticsCollection;
	public Operation operations;
	private DomainModel domainModel;

	public RegisteredService(){

	}

	public void finalize() throws Throwable {

	}

	public java.lang.String getAlias(){
		return alias;
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

	/**
	 * 
	 * @param newVal
	 */
	public void setAlias(java.lang.String newVal){
		alias = newVal;
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