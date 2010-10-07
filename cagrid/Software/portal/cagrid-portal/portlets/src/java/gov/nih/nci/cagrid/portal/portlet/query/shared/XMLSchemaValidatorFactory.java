package gov.nih.nci.cagrid.portal.portlet.query.shared;

import gov.nih.nci.cagrid.common.SchemaValidationException;
import gov.nih.nci.cagrid.common.SchemaValidator;

import java.net.URL;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class XMLSchemaValidatorFactory {

     

    public static SchemaValidator initialize(String schema) throws SchemaValidationException{
         URL schemaPath = XMLSchemaValidatorFactory.class.getClassLoader().getResource(schema);
        return new SchemaValidator(schemaPath.getFile());
    }

   
}
