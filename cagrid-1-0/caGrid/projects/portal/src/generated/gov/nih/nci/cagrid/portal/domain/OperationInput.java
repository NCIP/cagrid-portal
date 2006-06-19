package gov.nih.nci.cagrid.portal.domain;

/**
 * @created 13-Jun-2006 3:14:16 PM
 * @version 1.0
 */
public class OperationInput implements Parameter {

	public OperationInput(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 *
	 * @param class
	 */
	public void setUMLClass(UMLClass umlClass){

	}

	/**
	 *
	 * @param dimesionality
	 */
	public void setDimensionality(int dimesionality){

	}

	public int getDimensionality(){
		return 0;
	}

	public UMLClass getUMLClass(){
		return null;
	}

	public boolean isArray(){
		return false;
	}

}