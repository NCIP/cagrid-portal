package gov.nih.nci.cagrid.portal.domain;


/**
 * @version 1.0
 * @created 19-Jun-2006 4:08:50 PM
 */
public class UMLAttribute {
    private java.lang.String description;
    private int id;
    private java.lang.String name;
    private UMLClass umlClass;
    public SemanticMetadata semanticMetadataCollection;

    public UMLAttribute() {
    }

    public void finalize() throws Throwable {
    }

    public java.lang.String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public java.lang.String getName() {
        return name;
    }

    /**
     * @param newVal
     */
    public void setDescription(java.lang.String newVal) {
        description = newVal;
    }

    /**
     * @param newVal
     */
    public void setId(int newVal) {
        id = newVal;
    }

    /**
     * @param newVal
     */
    public void setName(java.lang.String newVal) {
        name = newVal;
    }

    public UMLClass getUmlClass() {
        return umlClass;
    }

    public void setUmlClass(UMLClass umlClass) {
        this.umlClass = umlClass;
    }

    public SemanticMetadata getSemanticMetadataCollection() {
        return semanticMetadataCollection;
    }

    public void setSemanticMetadataCollection(
        SemanticMetadata semanticMetadataCollection) {
        this.semanticMetadataCollection = semanticMetadataCollection;
    }
}
