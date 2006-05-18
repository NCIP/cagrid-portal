package gov.nih.nci.cagrid.data.client;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cagrid.data.common.CQLValidationException;
import gov.nih.nci.cagrid.data.common.CQLValidatorI;


/**
 * This class will validate a CQL query against
 * a Data Services domain model metadata.
 * <p/>
 * This is a utility class used by a data service client
 * <p/>
 * Created by the caGrid Team
 * User: kherm
 * Date: May 15, 2006
 * Time: 10:43:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class CQLMetadataValidator implements CQLValidatorI {

    /**
     * Will validate CQL against the Project
     * metadata
     *
     * @param proj
     */
    public CQLMetadataValidator(Project proj) {

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
        return true;
    }
}
