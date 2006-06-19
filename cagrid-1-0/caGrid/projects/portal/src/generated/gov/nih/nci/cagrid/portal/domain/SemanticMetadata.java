package gov.nih.nci.cagrid.portal.domain;

/**
 * @version 1.0
 * @created 19-Jun-2006 4:08:50 PM
 */
public class SemanticMetadata {

	private java.lang.String conceptCode;
	private java.lang.String conceptDefinition;
	private java.lang.String conceptName;
	private int order;
	private int orderLevel;
	private boolean primaryConcept;

	public SemanticMetadata(){

	}

	public void finalize() throws Throwable {

	}

	public java.lang.String getConceptCode(){
		return conceptCode;
	}

	public java.lang.String getConceptDefinition(){
		return conceptDefinition;
	}

	public java.lang.String getConceptName(){
		return conceptName;
	}

	public int getOrder(){
		return order;
	}

	public int getOrderLevel(){
		return orderLevel;
	}

	public boolean isPrimaryConcept(){
		return primaryConcept;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setConceptCode(java.lang.String newVal){
		conceptCode = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setConceptDefinition(java.lang.String newVal){
		conceptDefinition = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setConceptName(java.lang.String newVal){
		conceptName = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setOrder(int newVal){
		order = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setOrderLevel(int newVal){
		orderLevel = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPrimaryConcept(boolean newVal){
		primaryConcept = newVal;
	}


}