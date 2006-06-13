/*
 * Created on Feb 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nci.nih.cagrid.tests.core.types;

/**
 * Type for xs:boolean that allows true, false, 1, and 0
 * 
 * @author MCCON012
 */
public class BooleanType 
{
	/**
	 * Actual boolean value
	 */
	private boolean value;
	
	/**
	 * Construct a boolean from the string
	 * @param str true, false, 1, or 0
	 */
	public BooleanType(String str)
	{
		super();
		
		value = str.equals("true") || str.equals("1");
	}
	
	/**
	 * Test the equality of two BooleanType boolean values
	 */
	public boolean equals(Object obj)
	{
		return value == ((BooleanType) obj).value;
	}
}
