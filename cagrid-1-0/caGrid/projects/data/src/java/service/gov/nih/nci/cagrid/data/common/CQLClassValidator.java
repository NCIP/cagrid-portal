package gov.nih.nci.cagrid.data.common;



/**
 * This class implements the CQLValidatorI interface
 * This is a utility class to validate a CQL query
 * against a Java Class.
 * <p/>
 * This utility is intended to be used by the Data
 * Service implementation (engine) to validate incoming
 * CQL queries
 * <p/>
 * Created by the caGrid Team
 * User: kherm
 * Date: May 15, 2006
 * Time: 10:20:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class CQLClassValidator implements CQLValidatorI {

    private ClassLoader internalClassLoader;

    /**
     * Validator will use the default JVM classloader
     */
    public CQLClassValidator() {
        internalClassLoader = ClassLoader.getSystemClassLoader();
    }

    /**
     * Validator will use user defined class loader to validate
     * the CQL query
     *
     * @param loader
     */
    public CQLClassValidator(ClassLoader loader) {
        this.internalClassLoader = loader;
    }


    /**
     * Validates a CQL query object
     *
     * @param cqlQueryObject
     * @return
     * @throws gov.nih.nci.cagrid.data.common.CQLValidationException
     *
     */
    public boolean validate(Object cqlQueryObject) throws CQLValidationException {
        //ToDo implement method;
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
