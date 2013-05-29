/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
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
