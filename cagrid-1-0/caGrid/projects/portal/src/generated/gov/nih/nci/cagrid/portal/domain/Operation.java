package gov.nih.nci.cagrid.portal.domain;


/**
 * @version 1.0
 * @created 19-Jun-2006 4:05:30 PM
 * @hibernate.class table="OPERATION"
 */
public class Operation implements DomainObject {

    private java.lang.Integer pk;
    private java.lang.String description;
    private java.util.Collection faults;
    private java.util.Collection inputParamters;
    private java.lang.String name;
    private UMLClass outputClass;
    private RegisteredService registeredService;

    public Operation() {
    }

    /**
     * @hibernate.id column="OPERATION_ID_KEY"
     * generator-class="increment"
     * unsaved-value="null"
     */
    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    /**
     * @hibernate.property column="NAME"
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * @hibernate.property column="DESCRIPTION"
     */
    public java.lang.String getDescription() {
        return description;
    }

    public java.util.Collection getFaults() {
        return faults;
    }


    public java.util.Collection getInputParamters() {
        return inputParamters;
    }

    /**
     * @hibernate.many-to-one class="gov.nih.nci.cagrid.portal.domain.UMLClass"
     * column="OUTPUT_ID_KEY"
     * not-null="false"
     * cascade="all"
     */
    public UMLClass getOutput() {
        return outputClass;
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
    public void setFaults(java.util.Collection newVal) {
        faults = newVal;
    }

    /**
     * @param newVal
     */
    public void setInputParamters(java.util.Collection newVal) {
        inputParamters = newVal;
    }

    /**
     * @param newVal
     */
    public void setName(java.lang.String newVal) {
        name = newVal;
    }


    public void setOutput(UMLClass outputClass) {
        this.outputClass = outputClass;
    }

    /**
     * @hibernate.many-to-one column="SERVICE_ID_KEY"
     * class="gov.nih.nci.cagrid.portal.domain.RegisteredService"
     */
    public RegisteredService getRegisteredService() {
        return registeredService;
    }

    public void setRegisteredService(RegisteredService registeredService) {
        this.registeredService = registeredService;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Operation operation = (Operation) o;

        if (inputParamters != null ? !inputParamters.equals(operation.inputParamters) : operation.inputParamters != null)
            return false;
        if (name != null ? !name.equals(operation.name) : operation.name != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (inputParamters != null ? inputParamters.hashCode() : 0);
        result = 29 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

}
