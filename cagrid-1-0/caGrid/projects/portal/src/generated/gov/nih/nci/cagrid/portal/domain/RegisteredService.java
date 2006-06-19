package gov.nih.nci.cagrid.portal.domain;

/**
 * @version 1.0
 * @created 19-Jun-2006 3:48:26 PM
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

	/**
	 * 
	 * @param newVal
	 */
	public void setAlias(java.lang.String newVal){
		alias = newVal;
	}

	public EPR getHandle(){
		return null;
	}

	public java.lang.String getName(){
		return null;
	}

	public java.lang.String getDescription(){
		return null;
	}

	/**
	 * 
	 * @param desc
	 */
	public void setDescription(java.lang.String desc){

	}

	/**
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name){

	}

	/**
	 * 
	 * @param handle
	 */
	public void setHandle(java.lang.String handle){

	}

}