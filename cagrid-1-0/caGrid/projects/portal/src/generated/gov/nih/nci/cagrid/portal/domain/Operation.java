package gov.nih.nci.cagrid.portal.domain;

/**
 * @version 1.0
 * @created 19-Jun-2006 3:48:26 PM
 */
public class Operation {

	private java.lang.String description;
	private java.util.Collection faults;
	private java.util.Collection inputParamters;
	private java.lang.String name;
	private OperationOutput output;

	public Operation(){

	}

	public void finalize() throws Throwable {

	}

	public java.lang.String getDescription(){
		return description;
	}

	public java.util.Collection getFaults(){
		return faults;
	}

	public java.util.Collection getInputParamters(){
		return inputParamters;
	}

	public java.lang.String getName(){
		return name;
	}

	public java.lang.String getOutput(){
		return output;
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
	public void setFaults(java.util.Collection newVal){
		faults = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setInputParamters(java.util.Collection newVal){
		inputParamters = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setName(java.lang.String newVal){
		name = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setOutput(java.lang.String newVal){
		output = newVal;
	}

}