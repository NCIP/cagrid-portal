package gov.nih.nci.cagrid.portal.domain;

/**
 * @version 1.0
 * @created 19-Jun-2006 3:22:45 PM
 */
public interface GridService {

	private static java.lang.String description;
	private static EPR handle;
	private static java.lang.String name;

	public java.lang.String getDescription();

	public EPR getHandle();

	public java.lang.String getName();

	/**
	 * 
	 * @param newVal
	 */
	public void setDescription(java.lang.String newVal);

	/**
	 * 
	 * @param newVal
	 */
	public void setHandle(EPR newVal);

	/**
	 * 
	 * @param newVal
	 */
	public void setName(java.lang.String newVal);

}

/**
 * @version 1.0
 * @created 19-Jun-2006 3:22:45 PM
 */
public class GridService {

	private String epr;

	public GridService(){

	}

	public void finalize() throws Throwable {

	}

	public String getepr(){
		return epr;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setepr(String newVal){
		epr = newVal;
	}

}