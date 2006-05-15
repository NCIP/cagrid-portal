package gov.nih.nci.cagrid.data.common;



/**
 * Created by the caGrid Team
 * User: kherm
 * Date: May 15, 2006
 * Time: 10:11:59 AM
 * To change this template use File | Settings | File Templates.
 */
public interface CQLValidatorI {

    /**
     * Validates a CQL query object
     *
     * @param cqlQueryObject
     * @return
     * @throws CQLValidationException
     */
    public boolean validate(Object cqlQueryObject) throws CQLValidationException;
}
