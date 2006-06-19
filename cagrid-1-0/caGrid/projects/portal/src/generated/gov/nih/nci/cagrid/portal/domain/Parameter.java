package gov.nih.nci.cagrid.portal.domain;

/**
 * @version 1.0
 * @created 19-Jun-2006 3:48:26 PM
 */
public interface Parameter {

	public UMLClass umlClass;

	public abstract int getDimensionality();

	public abstract UMLClass getUMLClass();

	public abstract boolean isArray();

}