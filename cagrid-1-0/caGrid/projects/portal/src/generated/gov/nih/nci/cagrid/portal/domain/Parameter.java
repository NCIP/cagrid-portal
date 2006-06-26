package gov.nih.nci.cagrid.portal.domain;

/**
 * @version 1.0
 * @created 19-Jun-2006 4:08:50 PM
 */
public interface Parameter {

    public abstract int getDimensionality();

    public abstract UMLClass getUMLClass();

    public abstract boolean isArray();

    /**
     *
     * @param dimesionality
     */
    public void setDimensionality(int dimesionality);

    /**
     *
     * @param umlClass
     */
    public abstract void setUMLClass(UMLClass umlClass);

}