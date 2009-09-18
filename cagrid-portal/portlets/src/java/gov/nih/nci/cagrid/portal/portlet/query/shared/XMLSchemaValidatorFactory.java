package gov.nih.nci.cagrid.portal.portlet.query.shared;

import gov.nih.nci.cagrid.common.SchemaValidator;
import gov.nih.nci.cagrid.common.SchemaValidationException;

import java.net.URL;

import org.springframework.beans.factory.annotation.Required;

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
